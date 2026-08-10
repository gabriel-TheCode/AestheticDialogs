package com.thecode.aestheticdialogs.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import com.thecode.aestheticdialogs.foundation.DialogTone

/**
 * The marks AestheticDialogs draws itself.
 *
 * Every glyph is geometry on a unit square, scaled at draw time, so the library
 * ships no icon dependency and no drawable resources: consumers do not inherit a
 * few thousand vectors for the four marks a dialog needs, and the marks scale to
 * any size without a density bucket.
 *
 * Callers who want a different icon pass an `ImageVector` through the
 * component's UI model; those are rendered with the platform `Icon`.
 */
internal enum class GlyphMark {
    Check,
    Cross,
    Exclamation,
    Info,
    Search,
    Close,
    Reveal,
    Conceal,
    ;

    internal companion object {
        fun forTone(tone: DialogTone): GlyphMark = when (tone) {
            DialogTone.Success -> Check
            DialogTone.Error -> Cross
            DialogTone.Warning -> Exclamation
            DialogTone.Info -> Info
            DialogTone.Neutral -> Info
        }
    }
}

/**
 * Draws [mark] centred in a [size] box, in [color].
 *
 * Purely visual: it carries no semantics of its own. Callers that need the mark
 * to be announced attach a `contentDescription` to their own node.
 */
@Composable
internal fun AestheticGlyph(
    mark: GlyphMark,
    color: Color,
    size: Dp,
    modifier: Modifier = Modifier,
    strokeFraction: Float = DEFAULT_STROKE_FRACTION,
) {
    Canvas(modifier = modifier.size(size)) {
        drawGlyph(mark = mark, color = color, strokeFraction = strokeFraction)
    }
}

private const val DEFAULT_STROKE_FRACTION = 0.10f

/**
 * Shared drawing routine so the glyph can also be painted inside a larger
 * canvas (the filled status badge) without composing a second node.
 */
internal fun DrawScope.drawGlyph(
    mark: GlyphMark,
    color: Color,
    strokeFraction: Float = DEFAULT_STROKE_FRACTION,
    bounds: Size = size,
    topLeft: Offset = Offset.Zero,
) {
    val extent = minOf(bounds.width, bounds.height)
    val stroke = Stroke(width = extent * strokeFraction, cap = StrokeCap.Round)

    fun point(x: Float, y: Float) = Offset(
        x = topLeft.x + bounds.width * x,
        y = topLeft.y + bounds.height * y,
    )

    fun line(fromX: Float, fromY: Float, toX: Float, toY: Float) {
        drawLine(
            color = color,
            start = point(fromX, fromY),
            end = point(toX, toY),
            strokeWidth = stroke.width,
            cap = StrokeCap.Round,
        )
    }

    fun dot(x: Float, y: Float) {
        drawCircle(color = color, radius = stroke.width / 2f, center = point(x, y))
    }

    when (mark) {
        GlyphMark.Check -> {
            line(0.24f, 0.53f, 0.43f, 0.71f)
            line(0.43f, 0.71f, 0.77f, 0.31f)
        }

        GlyphMark.Cross, GlyphMark.Close -> {
            line(0.29f, 0.29f, 0.71f, 0.71f)
            line(0.71f, 0.29f, 0.29f, 0.71f)
        }

        GlyphMark.Exclamation -> {
            line(0.5f, 0.24f, 0.5f, 0.60f)
            dot(0.5f, 0.76f)
        }

        GlyphMark.Info -> {
            dot(0.5f, 0.26f)
            line(0.5f, 0.42f, 0.5f, 0.76f)
        }

        GlyphMark.Reveal, GlyphMark.Conceal -> {
            drawCircle(
                color = color,
                radius = extent * 0.26f,
                center = point(0.5f, 0.5f),
                style = stroke,
            )
            drawCircle(color = color, radius = extent * 0.09f, center = point(0.5f, 0.5f))
            if (mark == GlyphMark.Conceal) {
                line(0.22f, 0.78f, 0.78f, 0.22f)
            }
        }

        GlyphMark.Search -> {
            drawCircle(
                color = color,
                radius = extent * 0.23f,
                center = point(0.44f, 0.44f),
                style = stroke,
            )
            line(0.62f, 0.62f, 0.79f, 0.79f)
        }
    }
}
