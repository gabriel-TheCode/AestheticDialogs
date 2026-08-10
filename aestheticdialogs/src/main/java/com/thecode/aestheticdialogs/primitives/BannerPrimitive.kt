package com.thecode.aestheticdialogs.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticElevation
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * The banner every notification variant is built from.
 *
 * A banner is not a dialog: it opens no window, takes no focus and blocks no
 * input. It is an ordinary composable the host places in its own layout.
 *
 * The whole banner is one live region, so a screen reader announces the message
 * when it appears instead of waiting for the user to find it.
 */
@Composable
internal fun BannerPrimitive(
    title: String,
    message: String?,
    titleColor: Color,
    messageColor: Color,
    containerColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    containerBrush: Brush? = null,
    leadingAccentColor: Color? = null,
    topAccentBrush: Brush? = null,
    liveRegionMode: LiveRegionMode = LiveRegionMode.Polite,
    centered: Boolean = false,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = AestheticDimens.notificationMaxWidth)
            .semantics { liveRegion = liveRegionMode },
        shape = shape,
        color = containerColor,
        contentColor = messageColor,
        shadowElevation = AestheticElevation.banner,
    ) {
        Column(
            modifier = Modifier
                .clip(shape)
                .then(
                    if (containerBrush != null) Modifier.background(containerBrush) else Modifier,
                )
                .then(
                    if (topAccentBrush != null) Modifier.topAccentRim(shape, topAccentBrush) else Modifier,
                )
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        ) {
            if (topAccentBrush != null) {
                Spacer(Modifier.height(AestheticDimens.accentBarHeight))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = AestheticDimens.bannerMinHeight),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingAccentColor?.let { color ->
                    Box(
                        Modifier
                            .width(AestheticDimens.accentBarWidth)
                            .height(AestheticDimens.minTouchTarget)
                            .background(color),
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = AestheticSpacing.lg,
                            vertical = AestheticSpacing.md,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (centered) {
                        Arrangement.Center
                    } else {
                        Arrangement.Start
                    },
                ) {
                    // Without this mirror the copy centres in the space left of
                    // the close button rather than in the card.
                    if (centered && trailing != null) {
                        Spacer(Modifier.width(AestheticDimens.bannerAffordanceSlot))
                    }

                    leading?.let {
                        it()
                        Spacer(Modifier.width(AestheticSpacing.lg))
                    }

                    Column(
                        modifier = Modifier.weight(1f, fill = !centered),
                        horizontalAlignment = if (centered) {
                            Alignment.CenterHorizontally
                        } else {
                            Alignment.Start
                        },
                    ) {
                        Text(
                            text = title,
                            style = AestheticDialogsTheme.typography.itemLabel,
                            color = titleColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = if (centered) TextAlign.Center else TextAlign.Start,
                        )
                        message?.let {
                            Text(
                                text = it,
                                style = AestheticDialogsTheme.typography.supporting,
                                color = messageColor,
                                maxLines = BANNER_MESSAGE_MAX_LINES,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = if (centered) TextAlign.Center else TextAlign.Start,
                            )
                        }
                    }
                }

                trailing?.invoke()
            }
        }
    }
}

/**
 * Paints the accent along the top edge of [shape] instead of across a rectangle.
 *
 * A straight `accentBarHeight`-tall strip is the obvious implementation and it is
 * the wrong one: the banner clips to a 16dp corner radius, so the strip is eaten
 * away at both ends and reads as a short line floating above the card rather than
 * as part of it. The rim is the difference between the banner outline and the
 * same outline pushed down by the bar height, so it keeps a constant thickness
 * across the top and tapers into the corners as the silhouette turns vertical.
 *
 * Deriving both paths from [shape] rather than from a radius constant means a
 * rebranded [com.thecode.aestheticdialogs.tokens.AestheticShapes] — square
 * corners, or a much rounder banner — stays correct with no further work.
 */
private fun Modifier.topAccentRim(shape: Shape, brush: Brush): Modifier = drawWithCache {
    val thickness = AestheticDimens.accentBarHeight.toPx()
    if (size.height <= thickness || size.width <= 0f) return@drawWithCache onDrawBehind {}

    val outer = Path().apply {
        addOutline(shape.createOutline(size, layoutDirection, this@drawWithCache))
    }
    val inner = Path().apply {
        addOutline(
            shape.createOutline(
                Size(size.width, size.height - thickness),
                layoutDirection,
                this@drawWithCache,
            ),
        )
        translate(Offset(0f, thickness))
    }
    val rim = Path.combine(PathOperation.Difference, outer, inner)

    onDrawBehind { drawPath(rim, brush) }
}

private const val BANNER_MESSAGE_MAX_LINES = 2
