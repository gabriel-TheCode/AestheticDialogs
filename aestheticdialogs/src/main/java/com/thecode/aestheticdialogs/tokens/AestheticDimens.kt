package com.thecode.aestheticdialogs.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Component dimensions shared by the dialog primitives.
 *
 * Anything that appears more than once — an icon size, a control height, a
 * dialog width bucket — lives here. A raw `dp` literal inside a primitive or a
 * variant is a review failure.
 */
public object AestheticDimens {
    /** Minimum size of any interactive target, per the Android accessibility guidance. */
    public val minTouchTarget: Dp = 48.dp

    public val buttonHeight: Dp = 48.dp
    public val buttonMinWidth: Dp = 88.dp
    public val fieldHeight: Dp = 56.dp

    public val iconSm: Dp = 16.dp
    public val iconMd: Dp = 24.dp
    public val iconLg: Dp = 32.dp

    /** The large status glyph at the top of the modal feedback dialogs. */
    public val statusGlyph: Dp = 72.dp

    /** The compact status glyph used by notification banners. */
    public val statusGlyphCompact: Dp = 32.dp

    public val borderWidth: Dp = 1.dp

    /** Thickness of the tone bar down the leading edge of a banner. */
    public val accentBarWidth: Dp = 4.dp

    /**
     * Thickness of the tone rim along the top edge of a banner.
     *
     * Slightly heavier than [accentBarWidth] because the rim follows the corner
     * radius and thins out towards the sides; at 4dp it read as a hairline.
     */
    public val accentBarHeight: Dp = 6.dp
    public val progressIndicatorSize: Dp = 20.dp
    public val progressIndicatorStroke: Dp = 2.dp

    // Width buckets are chosen from the space the dialog was given, never from a
    // device class: a phone in landscape, a foldable and a freeform window all
    // resolve correctly that way.
    public val breakpointMedium: Dp = 600.dp
    public val breakpointExpanded: Dp = 840.dp

    /** Horizontal inset kept between a compact-width dialog and the screen edge. */
    public val compactWindowMargin: Dp = 24.dp
    public val dialogWidthMedium: Dp = 480.dp
    public val dialogWidthExpanded: Dp = 560.dp

    /** Maximum width of a notification banner; wider screens keep it centred. */
    public val notificationMaxWidth: Dp = 520.dp

    /** Minimum height of a notification banner, so a one-line message still reads as a surface. */
    public val bannerMinHeight: Dp = 64.dp

    /**
     * Width a banner reserves for its trailing affordance.
     *
     * Mirrored on the leading edge when a banner centres its copy, so "centred"
     * means centred in the card rather than in the space left over next to the
     * close button.
     */
    public val bannerAffordanceSlot: Dp = minTouchTarget + AestheticSpacing.sm
}
