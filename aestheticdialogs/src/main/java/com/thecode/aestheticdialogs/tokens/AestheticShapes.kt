package com.thecode.aestheticdialogs.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Corner radius scale. Shapes are built from these, never from raw values. */
public object AestheticRadius {
    public val none: Dp = 0.dp
    public val sm: Dp = 8.dp
    public val md: Dp = 16.dp
    public val lg: Dp = 24.dp
    public val full: Dp = 1000.dp
}

/**
 * Shapes used by the dialog surfaces and controls.
 *
 * A value class rather than an object because shape is part of a brand: passing
 * a copy to [com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme]
 * re-skins every dialog at once.
 */
@Immutable
public data class AestheticShapes(
    /** The modal dialog container. */
    val dialog: Shape = RoundedCornerShape(AestheticRadius.lg),
    /** Notification banners, which sit against a screen edge. */
    val banner: Shape = RoundedCornerShape(AestheticRadius.md),
    /**
     * A sheet docked to the bottom of the window.
     *
     * Only the top corners are rounded: the bottom two sit off the screen, and
     * rounding them would draw a gap between the sheet and the edge it is
     * anchored to.
     */
    val sheet: Shape = RoundedCornerShape(
        topStart = AestheticRadius.lg,
        topEnd = AestheticRadius.lg,
        bottomStart = AestheticRadius.none,
        bottomEnd = AestheticRadius.none,
    ),
    /** Buttons and other pill-shaped controls. */
    val button: Shape = RoundedCornerShape(AestheticRadius.full),
    /** Text fields, selection rows and other rectangular controls. */
    val control: Shape = RoundedCornerShape(AestheticRadius.sm),
    /** Circular containers such as the status glyph backdrop. */
    val circle: Shape = RoundedCornerShape(AestheticRadius.full),
) {
    public companion object {
        /** The AestheticDialogs default silhouette. */
        public val Default: AestheticShapes = AestheticShapes()
    }
}
