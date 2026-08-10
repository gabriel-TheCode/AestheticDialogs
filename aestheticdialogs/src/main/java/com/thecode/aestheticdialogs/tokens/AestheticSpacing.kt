package com.thecode.aestheticdialogs.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The layout grid: a 4dp scale used for every gap and padding in the library.
 *
 * Spacing is deliberately *not* part of [com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme].
 * Colour, type, shape and motion express a brand; the layout grid expresses the
 * structure of the components themselves, and letting callers rewrite it would
 * turn every dialog into an untestable layout. Callers who need different
 * padding change the component, not the grid.
 */
public object AestheticSpacing {
    public val none: Dp = 0.dp
    public val xxs: Dp = 2.dp
    public val xs: Dp = 4.dp
    public val sm: Dp = 8.dp
    public val md: Dp = 12.dp
    public val lg: Dp = 16.dp
    public val xl: Dp = 20.dp
    public val xxl: Dp = 24.dp
    public val xxxl: Dp = 32.dp
}
