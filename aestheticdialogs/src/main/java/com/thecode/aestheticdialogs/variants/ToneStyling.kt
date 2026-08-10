package com.thecode.aestheticdialogs.variants

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone

/**
 * The gradient used by the colour-filled variants (Flash, Connectify).
 *
 * Derived from the tone accent rather than hand-drawn, so a rebranded theme
 * gets a matching gradient for every tone without touching this file.
 */
@Composable
internal fun toneGradient(tone: DialogTone): Brush = Brush.verticalGradient(toneRamp(tone))

/**
 * The same ramp laid out horizontally, for accents that are only a few dp tall.
 *
 * A vertical gradient across a 6dp rim is a solid colour with extra steps. The
 * Connectify banner's identity is the coloured strip along its top edge, so the
 * ramp runs along the strip instead of across it.
 */
@Composable
internal fun toneRimGradient(tone: DialogTone): Brush = Brush.horizontalGradient(toneRamp(tone))

@Composable
private fun toneRamp(tone: DialogTone): List<Color> {
    val accent = AestheticDialogsTheme.colors.status.forTone(tone).accent
    return listOf(
        lerp(accent, AestheticDialogsTheme.colors.content.inverse, GRADIENT_LIGHT_MIX),
        accent,
        lerp(accent, AestheticDialogsTheme.colors.content.primary, GRADIENT_DARK_MIX),
    )
}

private const val GRADIENT_LIGHT_MIX = 0.22f
private const val GRADIENT_DARK_MIX = 0.14f
