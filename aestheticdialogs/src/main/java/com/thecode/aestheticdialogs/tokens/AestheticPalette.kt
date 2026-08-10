package com.thecode.aestheticdialogs.tokens

import androidx.compose.ui.graphics.Color

/**
 * Raw, non-semantic colour values.
 *
 * This is the *primitive* level of the token architecture. Nothing outside the
 * library reads it, and nothing inside the library reads it either except the
 * light and dark semantic palettes. Components always go through
 * [com.thecode.aestheticdialogs.foundation.AestheticColors], which is what makes
 * theming a pure remapping exercise.
 *
 * Every status hue comes as a bright and a deep value: the deep one is used on
 * light surfaces and the bright one on dark surfaces, so both clear contrast.
 */
internal object AestheticPalette {

    val White: Color = Color(0xFFFFFFFF)
    val Grey50: Color = Color(0xFFF6F7F9)
    val Grey100: Color = Color(0xFFEDEFF2)
    val Grey200: Color = Color(0xFFDCE0E6)
    val Grey400: Color = Color(0xFF9AA3AE)
    val Grey500: Color = Color(0xFF6B7683)
    val Grey700: Color = Color(0xFF3C444E)
    val Grey800: Color = Color(0xFF2A2F36)
    val Grey900: Color = Color(0xFF1B1C1E)
    val Grey950: Color = Color(0xFF121316)
    val Black: Color = Color(0xFF000000)

    val GreenBright: Color = Color(0xFF48D865)
    val GreenDeep: Color = Color(0xFF2F7D3C)
    val GreenTintLight: Color = Color(0xFFE6F7EA)
    val GreenTintDark: Color = Color(0xFF14301B)

    val RedBright: Color = Color(0xFFFF6B5F)
    val RedDeep: Color = Color(0xFFC22B22)
    val RedTintLight: Color = Color(0xFFFDECEA)
    val RedTintDark: Color = Color(0xFF3A1613)

    val AmberBright: Color = Color(0xFFFFC122)
    val AmberDeep: Color = Color(0xFFB45309)
    val AmberTintLight: Color = Color(0xFFFFF4DB)
    val AmberTintDark: Color = Color(0xFF33240A)

    val BlueBright: Color = Color(0xFF3086EB)
    val BlueDeep: Color = Color(0xFF1257A8)
    val BlueTintLight: Color = Color(0xFFE7F1FD)
    val BlueTintDark: Color = Color(0xFF10233C)
}
