package com.thecode.aestheticdialogs.components.notification.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.tokens.AestheticDimens

/**
 * A bar bonded to the bottom edge of a banner, filled from the start edge.
 *
 * Used for two things — how far a background job has got, and how much of an
 * auto-dismiss delay is left — which are the same drawing with different colours
 * and thicknesses. Neither carries semantics: progress is announced by the copy,
 * and a countdown announced to a screen reader would be noise on a timer the user
 * cannot see anyway.
 */
@Composable
internal fun BannerEdgeBarPrimitive(
    fraction: Float,
    color: Color,
    trackColor: Color,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(trackColor)
            .clearAndSetSemantics {},
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .background(color),
        )
    }
}

/**
 * The availability dot drawn over a banner's leading slot.
 *
 * It sits on top of an image the library has never seen, so it carries a ring in
 * the banner's own surface colour: without it, a green dot on a green shirt is
 * not a dot.
 */
@Composable
internal fun PresenceDotPrimitive(
    color: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(AestheticDimens.presenceDotSize)
            .clip(AestheticDialogsTheme.shapes.circle)
            .background(borderColor),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(AestheticDimens.presenceDotSize - AestheticDimens.presenceDotBorder * 2)
                .clip(AestheticDialogsTheme.shapes.circle)
                .background(color),
        )
    }
}
