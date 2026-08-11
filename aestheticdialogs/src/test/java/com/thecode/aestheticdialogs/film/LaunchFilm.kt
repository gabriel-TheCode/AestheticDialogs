package com.thecode.aestheticdialogs.film

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import kotlin.math.pow

/** The film canvas, in dp. Rendered at mdpi so one dp is one pixel. */
internal val FILM_WIDTH = 1920.dp
internal val FILM_HEIGHT = 1080.dp
internal const val FILM_DURATION_MS = 12_000

private val INK = Color(0xFF1B1C1E)
private val INK_SOFT = Color(0xFF6B7683)
private val INK_FAINT = Color(0xFF9AA3AE)
private val ACCENT = Color(0xFF1257A8)

/** A component plate: the real screenshot, cut out of its background. */
internal class Plate(val image: ImageBitmap) {
    val aspect: Float = image.width.toFloat() / image.height
}

internal fun loadPlates(dir: String): Map<String, Plate> =
    File(dir).listFiles { f -> f.extension == "png" }
        .orEmpty()
        .associate { file ->
            file.nameWithoutExtension to Plate(
                BitmapFactory.decodeFile(file.path).asImageBitmap(),
            )
        }

private fun span(t: Int, from: Int, to: Int): Float =
    ((t - from).toFloat() / (to - from)).coerceIn(0f, 1f)

private fun easeOut(x: Float): Float = 1f - (1f - x).pow(3)

private fun easeInOut(x: Float): Float =
    if (x < 0.5f) 4f * x * x * x else 1f - (-2f * x + 2f).pow(3) / 2f

private fun lerp(a: Float, b: Float, f: Float) = a + (b - a) * f

/**
 * A section's opacity across the film.
 *
 * Sections overlap by [CUT] and the ramps are linear, so an outgoing section and
 * an incoming one always sum to 1. Easing them independently, or butting them
 * end to end, leaves a frame where both are near zero — which reads as a flash.
 */
private fun section(t: Int, from: Int, to: Int): Float {
    val up = if (from == 0) 1f else span(t, from, from + CUT)
    val down = 1f - span(t, to - CUT, to)
    return (up * down).coerceIn(0f, 1f)
}

private const val CUT = 400

/**
 * One frame of the launch film, as a pure function of [timeMs].
 *
 * Nothing animates itself: every value is derived from the timeline, so the
 * renderer steps time by hand and each frame is reproducible. The plates are
 * drawn, never redrawn — the film moves, scales and fades finished pixels and
 * touches nothing inside them.
 */
@Composable
internal fun LaunchFilm(timeMs: Int, plates: Map<String, Plate>) {
    val whiteOut = easeInOut(span(timeMs, 11_400, 12_000))

    Box(
        modifier = Modifier
            .size(FILM_WIDTH, FILM_HEIGHT)
            // The plates are cut out of their backgrounds, so a wash behind them
            // reads as depth instead of outlining a matte.
            .background(
                Brush.linearGradient(
                    0f to Color.White,
                    0.5f to Color(0xFFFAFBFC),
                    1f to Color(0xFFF1F3F7),
                    start = Offset.Zero,
                    end = Offset(2400f, 1500f),
                ),
            ),
    ) {
        Section(section(timeMs, 0, 5_200)) { Converge(timeMs, plates) }
        Section(section(timeMs, 4_800, 8_600)) { CodeToUi(timeMs, plates) }
        Section(section(timeMs, 8_200, FILM_DURATION_MS + CUT)) { Hero(timeMs, plates) }

        if (whiteOut > 0f) {
            Box(Modifier.fillMaxSize().alpha(whiteOut).background(Color.White))
        }
    }
}

@Composable
private fun BoxScope.Section(alpha: Float, content: @Composable BoxScope.() -> Unit) {
    if (alpha <= 0f) return
    Box(Modifier.fillMaxSize().alpha(alpha), content = content)
}

// --- 0.0–5.2s — the surfaces converge -----------------------------------------

/**
 * Where a surface lands, and the offset it flies in from.
 *
 * The entry vectors point outward, so the surfaces converge on the group rather
 * than appearing one after another in place. The back row clears the front one: a
 * surface half-covering another reads as a mistake, not as depth.
 */
private class Surface(
    val key: String,
    val x: Float,
    val y: Float,
    val width: Dp,
    val entryX: Float,
    val entryY: Float,
    val depth: Float = 1f,
)

private val GROUP = listOf(
    Surface("alert", -440f, 244f, 300.dp, -620f, -360f, depth = 0.6f),
    Surface("content", 440f, 232f, 300.dp, 620f, -360f, depth = 0.6f),
    Surface("selection", -600f, 566f, 400.dp, -940f, 160f),
    Surface("confirmation", -200f, 590f, 400.dp, -340f, 420f),
    Surface("input", 200f, 572f, 400.dp, 340f, 420f),
    Surface("feedback", 600f, 584f, 400.dp, 940f, 160f),
    // Banners are not dialogs, so they arrive where a banner actually lives:
    // down from the top edge, into the gap the dialog rows leave in the middle.
    Surface("banner-toaster", 0f, 170f, 430.dp, 0f, -420f),
    Surface("banner-rainbow", 0f, 268f, 430.dp, 0f, -560f),
)

@Composable
private fun BoxScope.Converge(t: Int, plates: Map<String, Plate>) {
    val push = lerp(1.06f, 1f, easeInOut(span(t, 0, 3_400)))

    Box(Modifier.fillMaxSize().graphicsLayer(scaleX = push, scaleY = push)) {
        GROUP.forEachIndexed { index, surface ->
            val arrive = easeOut(span(t, index * 100, 1_700 + index * 100))
            PlateAt(
                plate = plates[surface.key],
                width = surface.width,
                centerX = (960f + lerp(surface.x + surface.entryX, surface.x, arrive)).dp,
                centerY = lerp(surface.y + surface.entryY, surface.y, arrive).dp,
                alpha = arrive * surface.depth,
                scale = lerp(0.88f, 1f, arrive),
            )
        }
    }

    Text(
        text = "One frame. One theme. One system.",
        style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Light, color = INK),
        modifier = Modifier
            .align(Alignment.TopCenter)
            .offset(y = 836.dp)
            .alpha(easeOut(span(t, 2_600, 3_400))),
    )
}

// --- 4.8–8.6s — a call site becomes the surface it describes -------------------

@Composable
private fun BoxScope.CodeToUi(t: Int, plates: Map<String, Plate>) {
    val handover = easeInOut(span(t, 6_400, 7_600))

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .offset(y = (-90).dp)
            .alpha(1f - handover)
            .graphicsLayer(
                scaleX = lerp(1f, 0.96f, handover),
                scaleY = lerp(1f, 0.96f, handover),
            )
            .background(Color(0xFFF4F6F8), RoundedCornerShape(22.dp))
            .padding(horizontal = 56.dp, vertical = 44.dp),
    ) {
        Column {
            CODE.forEach { line ->
                Text(
                    text = line,
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontFamily = FontFamily.Monospace,
                        color = INK_SOFT,
                    ),
                )
            }
        }
    }

    PlateAt(
        plate = plates["confirmation"],
        width = 520.dp,
        centerX = 960.dp,
        centerY = lerp(430f, 396f, handover).dp,
        alpha = handover,
        scale = lerp(0.94f, 1f, handover),
    )

    Column(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .offset(y = 786.dp)
            .alpha(easeOut(span(t, 7_300, 8_000))),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Less boilerplate.",
            style = TextStyle(fontSize = 46.sp, fontWeight = FontWeight.Light, color = INK),
        )
        Text(
            text = "More polished UI.",
            style = TextStyle(fontSize = 46.sp, fontWeight = FontWeight.Light, color = INK),
        )
    }
}

// --- 8.2–12.0s — everything settles, the title lands --------------------------

@Composable
private fun BoxScope.Hero(t: Int, plates: Map<String, Plate>) {
    val lift = easeInOut(span(t, 8_200, 9_700))
    val zoom = lerp(1.03f, 0.95f, lift)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = lerp(0f, -56f, lift).dp)
            .graphicsLayer(scaleX = zoom, scaleY = zoom),
    ) {
        GROUP.forEachIndexed { index, surface ->
            val settle = easeOut(span(t, 8_200 + index * 80, 9_700 + index * 80))
            PlateAt(
                plate = plates[surface.key],
                width = surface.width,
                centerX = (960f + surface.x).dp,
                centerY = lerp(surface.y + 26f, surface.y, settle).dp,
                alpha = surface.depth,
                scale = lerp(0.97f, 1f, settle),
            )
        }
    }

    Column(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .offset(y = 782.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.alpha(easeOut(span(t, 8_800, 9_700))),
        ) {
            Text("Aesthetic", style = wordmark(FontWeight.Light))
            Text("Dialogs", style = wordmark(FontWeight.Bold))
            Spacer(Modifier.width(24.dp))
            Box(Modifier.alpha(easeOut(span(t, 9_100, 10_000)))) { VersionChip() }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Beautiful dialogs. Designed right.",
            style = TextStyle(fontSize = 30.sp, color = INK_SOFT),
            modifier = Modifier.alpha(easeOut(span(t, 9_700, 10_500))),
        )
        Spacer(Modifier.height(28.dp))
        Text(
            text = "Jetpack Compose",
            style = TextStyle(fontSize = 21.sp, color = INK_FAINT, letterSpacing = 5.sp),
            modifier = Modifier.alpha(easeOut(span(t, 10_100, 10_800))),
        )
        Spacer(Modifier.height(18.dp))
        Text(
            text = "github.com/gabriel-TheCode/AestheticDialogs",
            style = TextStyle(fontSize = 24.sp, color = INK_SOFT),
            modifier = Modifier.alpha(easeOut(span(t, 10_500, 11_200))),
        )
    }
}

// --- pieces -------------------------------------------------------------------

@Composable
private fun BoxScope.PlateAt(
    plate: Plate?,
    width: Dp,
    centerX: Dp,
    centerY: Dp,
    alpha: Float,
    scale: Float,
) {
    if (plate == null || alpha <= 0f) return
    val height = width / plate.aspect
    Image(
        bitmap = plate.image,
        contentDescription = null,
        modifier = Modifier
            .offset(x = centerX - width / 2, y = centerY - height / 2)
            .size(width, height)
            .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha),
    )
}

@Composable
private fun VersionChip() {
    Box(
        modifier = Modifier
            .background(Color(0xFFE7F1FD), RoundedCornerShape(100.dp))
            .padding(horizontal = 26.dp, vertical = 8.dp),
    ) {
        Text(
            text = "2.0",
            style = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.SemiBold, color = ACCENT),
        )
    }
}

private fun wordmark(weight: FontWeight) =
    TextStyle(fontSize = 76.sp, fontWeight = weight, color = INK)

/** The call site the film hands over to, exactly as a consumer writes it. */
private val CODE = listOf(
    "AestheticConfirmationDialog(",
    "    uiModel = ConfirmationDialogUiModel.Destructive(",
    "        title = \"Delete this album?\",",
    "        message = \"The 24 photos inside it will be deleted too. This cannot be undone.\",",
    "        confirmLabel = \"Delete album\",",
    "        cancelLabel = \"Keep it\"",
    "    ),",
    "    onSignal = { },",
    ")",
)
