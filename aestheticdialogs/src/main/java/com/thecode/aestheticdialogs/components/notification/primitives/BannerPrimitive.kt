package com.thecode.aestheticdialogs.components.notification.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.unit.Dp
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.primitives.CloseButtonPrimitive
import com.thecode.aestheticdialogs.primitives.DialogButtonPrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.StatusBadgePrimitive
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
 *
 * It draws the badge, the emoji, the timestamp and the close affordance itself,
 * from raw parameters. A variant initialises it and nothing more: assembling
 * those pieces in the variant layer would put the same four decisions in four
 * places.
 */
@Composable
internal fun BannerPrimitive(
    title: String,
    message: String?,
    titleColor: Color,
    messageColor: Color,
    containerColor: Color,
    shape: Shape,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    containerBrush: Brush? = null,
    /** Drawn in the leading slot, unless [emoji] replaces it. `null` draws nothing. */
    mark: GlyphMark? = null,
    markColor: Color = titleColor,
    markContainerColor: Color = containerColor,
    /** A character drawn in place of [mark]. */
    emoji: String? = null,
    /** Drawn at the trailing edge in place of the close affordance. */
    timestamp: String? = null,
    showCloseButton: Boolean = false,
    affordanceTint: Color = messageColor,
    leadingAccentColor: Color? = null,
    topAccentBrush: Brush? = null,
    liveRegionMode: LiveRegionMode = LiveRegionMode.Polite,
    centered: Boolean = false,
    /** Drawn in the leading slot in place of [mark] and [emoji]: an avatar, usually. */
    leading: @Composable (() -> Unit)? = null,
    /** A dot drawn over [leading]. `null` draws none. */
    presenceColor: Color? = null,
    /** A single trailing text action. Takes the place of the timestamp and the close cross. */
    actionLabel: String? = null,
    onActionClick: () -> Unit = {},
    /** Determinate progress bonded to the bottom edge, from `0f` to `1f`. */
    progress: Float? = null,
    /** Remaining fraction of an auto-dismiss delay, drawn as a hairline. */
    countdown: Float? = null,
    progressTrackColor: Color = Color.Transparent,
    /** `null` lets the card fill its parent, for a docked strip. */
    maxWidth: Dp? = AestheticDimens.notificationMaxWidth,
    shadowElevation: Dp = AestheticElevation.banner,
    /**
     * Insets kept clear of the copy while the container still paints through
     * them. A banner docked against the top edge has to reach under the status
     * bar to look docked, and must not print its title on top of the clock.
     */
    contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(if (maxWidth != null) Modifier.widthIn(max = maxWidth) else Modifier)
            .semantics { liveRegion = liveRegionMode },
        shape = shape,
        color = containerColor,
        contentColor = messageColor,
        shadowElevation = shadowElevation,
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
                .clickable(onClick = onClick)
                .windowInsetsPadding(contentWindowInsets),
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
                    if (centered && (showCloseButton || timestamp != null)) {
                        Spacer(Modifier.width(AestheticDimens.bannerAffordanceSlot))
                    }

                    val hasLeading = leading != null || emoji != null || mark != null
                    if (hasLeading) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            when {
                                leading != null -> leading()

                                emoji != null -> Text(
                                    // The character is decoration next to text
                                    // that says the same thing, so it stays out
                                    // of the accessibility tree.
                                    text = emoji,
                                    style = AestheticDialogsTheme.typography.title,
                                )

                                mark != null -> StatusBadgePrimitive(
                                    mark = mark,
                                    accentColor = markColor,
                                    onAccentColor = markContainerColor,
                                    containerColor = markContainerColor,
                                    size = AestheticDimens.statusGlyphCompact,
                                    filled = false,
                                )
                            }

                            presenceColor?.let {
                                PresenceDotPrimitive(color = it, borderColor = containerColor)
                            }
                        }
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

                when {
                    actionLabel != null -> DialogButtonPrimitive(
                        label = actionLabel,
                        onClick = onActionClick,
                        // Borderless: a filled button in a banner competes with
                        // the primary action of the screen behind it.
                        containerColor = Color.Transparent,
                        contentColor = titleColor,
                        shape = AestheticDialogsTheme.shapes.button,
                        modifier = Modifier.padding(end = AestheticSpacing.sm),
                    )

                    timestamp != null -> Text(
                        text = timestamp,
                        style = AestheticDialogsTheme.typography.caption,
                        color = affordanceTint,
                        modifier = Modifier.padding(end = AestheticSpacing.lg),
                    )

                    showCloseButton -> CloseButtonPrimitive(
                        onClick = onDismiss,
                        tint = affordanceTint,
                        modifier = Modifier.padding(end = AestheticSpacing.sm),
                    )
                }
            }

            // Progress wins over the countdown: both are the same three pixels,
            // and a bar that means two things at once means neither.
            when {
                progress != null -> BannerEdgeBarPrimitive(
                    fraction = progress,
                    color = titleColor,
                    trackColor = progressTrackColor,
                    height = AestheticDimens.bannerProgressHeight,
                )

                countdown != null -> BannerEdgeBarPrimitive(
                    fraction = countdown,
                    color = titleColor,
                    trackColor = Color.Transparent,
                    height = AestheticDimens.bannerCountdownHeight,
                )
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
