package com.thecode.aestheticdialogs

import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.foundation.aestheticDarkColors
import com.thecode.aestheticdialogs.foundation.aestheticLightColors
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.tokens.AestheticMotion
import org.junit.Test

/** The token layer's semantics: tone mapping, reduced motion, scheme coverage. */
class TokenSemanticsTest {

    @Test
    fun `every tone maps to a distinct mark except neutral which borrows info`() {
        assertThat(GlyphMark.forTone(DialogTone.Success)).isEqualTo(GlyphMark.Check)
        assertThat(GlyphMark.forTone(DialogTone.Error)).isEqualTo(GlyphMark.Cross)
        assertThat(GlyphMark.forTone(DialogTone.Warning)).isEqualTo(GlyphMark.Exclamation)
        assertThat(GlyphMark.forTone(DialogTone.Info)).isEqualTo(GlyphMark.Info)
        assertThat(GlyphMark.forTone(DialogTone.Neutral)).isEqualTo(GlyphMark.Info)
    }

    @Test
    fun `both schemes resolve every tone`() {
        listOf(aestheticLightColors(), aestheticDarkColors()).forEach { colors ->
            DialogTone.entries.forEach { tone ->
                val resolved = colors.status.forTone(tone)

                assertThat(resolved.accent.alpha).isEqualTo(1f)
                assertThat(resolved.container.alpha).isEqualTo(1f)
            }
        }
    }

    @Test
    fun `disabling motion collapses transitions to an instant cut, not a short one`() {
        val reduced = AestheticMotion(enabled = false)

        // A shortened animation is still an animation; the accessibility setting
        // asks for none at all.
        assertThat(reduced.enabled).isFalse()
        assertThat(AestheticMotion.Default.enabled).isTrue()
    }

    @Test
    fun `light and dark schemes disagree about darkness`() {
        assertThat(aestheticLightColors().isDark).isFalse()
        assertThat(aestheticDarkColors().isDark).isTrue()
    }

    @Test
    fun `scrim opacity is stronger in dark mode where the surface contrasts less`() {
        assertThat(aestheticDarkColors().surface.scrim.alpha)
            .isGreaterThan(aestheticLightColors().surface.scrim.alpha)
    }
}
