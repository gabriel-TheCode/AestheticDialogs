package com.thecode.aestheticdialogs

import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.primitives.dialogWidthFor
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import org.junit.Test

/**
 * The adaptive width rule, tested as the pure function it is.
 *
 * This is the piece of the layout that is easiest to get wrong and hardest to
 * see in a screenshot, so it is worth a unit test rather than a pixel diff.
 */
class AdaptiveWidthTest {

    @Test
    fun `compact window fills the width minus a margin on both sides`() {
        val available = 360.dp

        val width = dialogWidthFor(available)

        assertThat(width).isEqualTo(available - AestheticDimens.compactWindowMargin * 2)
    }

    @Test
    fun `medium window pins a comfortable measure instead of stretching`() {
        val width = dialogWidthFor(700.dp)

        assertThat(width).isEqualTo(AestheticDimens.dialogWidthMedium)
    }

    @Test
    fun `expanded window uses the widest bucket`() {
        val width = dialogWidthFor(1280.dp)

        assertThat(width).isEqualTo(AestheticDimens.dialogWidthExpanded)
    }

    @Test
    fun `breakpoints are inclusive at the lower bound of the next bucket`() {
        assertThat(dialogWidthFor(AestheticDimens.breakpointMedium))
            .isEqualTo(AestheticDimens.dialogWidthMedium)
        assertThat(dialogWidthFor(AestheticDimens.breakpointExpanded))
            .isEqualTo(AestheticDimens.dialogWidthExpanded)
    }

    @Test
    fun `a phone in landscape is treated as a medium window, not as a phone`() {
        // 360x800 rotated. The old `isTablet` style check would have kept the
        // compact treatment here and produced an 800dp-wide dialog.
        val width = dialogWidthFor(800.dp)

        assertThat(width).isEqualTo(AestheticDimens.dialogWidthMedium)
    }
}
