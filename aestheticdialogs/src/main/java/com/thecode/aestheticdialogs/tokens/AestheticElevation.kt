package com.thecode.aestheticdialogs.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Elevation scale.
 *
 * Deliberately short. AestheticDialogs separates surfaces with hairline borders
 * and tonal contrast; shadows are used only where the platform expects them (a
 * floating dialog, a banner detached from the content beneath it). Long
 * elevation scales look muddy in dark themes and cost more to render.
 */
public object AestheticElevation {
    public val none: Dp = 0.dp
    public val banner: Dp = 6.dp
    public val dialog: Dp = 12.dp
}
