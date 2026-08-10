package com.thecode.aestheticdialogs.foundation

import android.provider.Settings
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.thecode.aestheticdialogs.tokens.AestheticMotion
import com.thecode.aestheticdialogs.tokens.AestheticShapes
import com.thecode.aestheticdialogs.tokens.AestheticTypography

internal val LocalAestheticColors = staticCompositionLocalOf<AestheticColors?> { null }
internal val LocalAestheticTypography = staticCompositionLocalOf { AestheticTypography.Default }
internal val LocalAestheticShapes = staticCompositionLocalOf { AestheticShapes.Default }
internal val LocalAestheticMotion = staticCompositionLocalOf<AestheticMotion?> { null }

/**
 * The AestheticDialogs theme.
 *
 * Wrap your application content once — usually next to your own `MaterialTheme`
 * — and every dialog picks up the same colours, type, shapes and motion:
 *
 * ```
 * AestheticDialogsTheme {
 *     AppNavHost()
 * }
 * ```
 *
 * Branding is done by copying a scheme rather than by implementing one:
 *
 * ```
 * AestheticDialogsTheme(
 *     colors = aestheticLightColors().copy(
 *         action = aestheticLightColors().action.copy(primary = BrandBlue),
 *     ),
 *     shapes = AestheticShapes(dialog = RoundedCornerShape(4.dp)),
 * ) { ... }
 * ```
 *
 * ### Override precedence
 *
 * ```
 * library defaults  ->  AestheticDialogsTheme(...)  ->  the component's UI model
 * ```
 *
 * Three levels, and each one owns something different: the library owns the
 * defaults, the theme owns the brand, and the UI model owns the semantics of a
 * single dialog (which tone it carries, whether an action is destructive). There
 * is deliberately no per-instance colour or typography parameter: a dialog that
 * needs its own palette is a design decision that belongs in the theme, not at
 * the call site.
 *
 * ### Wrapping is optional
 *
 * Components resolve sensible values when no theme is present — the light or
 * dark scheme is chosen from the system setting. Nothing crashes, nothing
 * renders light-on-light. Wrapping is still recommended, because that is the
 * only place branding can be applied.
 */
@Composable
public fun AestheticDialogsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: AestheticColors = if (darkTheme) aestheticDarkColors() else aestheticLightColors(),
    typography: AestheticTypography = AestheticTypography.Default,
    shapes: AestheticShapes = AestheticShapes.Default,
    motion: AestheticMotion = AestheticDialogsTheme.defaultMotion(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAestheticColors provides colors,
        LocalAestheticTypography provides typography,
        LocalAestheticShapes provides shapes,
        LocalAestheticMotion provides motion,
    ) {
        // Material 3 powers a handful of internal primitives (text fields,
        // checkboxes, progress indicators). Bridging the scheme here means those
        // primitives inherit Aesthetic colours instead of the host app's, so a
        // dialog looks the same in every application that embeds it.
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(),
            typography = typography.toMaterialTypography(),
            content = content,
        )
    }
}

/** Accessor for the current theme values. */
public object AestheticDialogsTheme {

    public val colors: AestheticColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAestheticColors.current
            ?: if (isSystemInDarkTheme()) aestheticDarkColors() else aestheticLightColors()

    public val typography: AestheticTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAestheticTypography.current

    public val shapes: AestheticShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAestheticShapes.current

    public val motion: AestheticMotion
        @Composable
        get() = LocalAestheticMotion.current ?: defaultMotion()

    /**
     * Motion tokens with [AestheticMotion.enabled] resolved from the platform.
     *
     * Android exposes "remove animations" as a global animator duration scale of
     * zero. Reading it here means a user who turned animations off gets instant
     * dialogs everywhere, without every application having to plumb the setting
     * through. Pass an explicit [AestheticMotion] to [AestheticDialogsTheme] to
     * override the decision.
     */
    @Composable
    public fun defaultMotion(): AestheticMotion {
        val context = LocalContext.current
        val isPreview = LocalInspectionMode.current
        val animationsEnabled = remember(context, isPreview) {
            if (isPreview) {
                true
            } else {
                runCatching {
                    Settings.Global.getFloat(
                        context.contentResolver,
                        Settings.Global.ANIMATOR_DURATION_SCALE,
                        1f,
                    )
                }.getOrDefault(1f) != 0f
            }
        }
        return remember(animationsEnabled) { AestheticMotion(enabled = animationsEnabled) }
    }
}

private fun AestheticColors.toMaterialColorScheme() = if (isDark) {
    darkColorScheme(
        primary = action.primary,
        onPrimary = action.onPrimary,
        surface = surface.container,
        onSurface = content.primary,
        surfaceVariant = surface.sunken,
        onSurfaceVariant = content.secondary,
        background = surface.container,
        onBackground = content.primary,
        outline = border.default,
        outlineVariant = border.subtle,
        error = status.error.accent,
        onError = status.error.onAccent,
        errorContainer = status.error.container,
        onErrorContainer = status.error.onContainer,
        scrim = surface.scrim,
    )
} else {
    lightColorScheme(
        primary = action.primary,
        onPrimary = action.onPrimary,
        surface = surface.container,
        onSurface = content.primary,
        surfaceVariant = surface.sunken,
        onSurfaceVariant = content.secondary,
        background = surface.container,
        onBackground = content.primary,
        outline = border.default,
        outlineVariant = border.subtle,
        error = status.error.accent,
        onError = status.error.onAccent,
        errorContainer = status.error.container,
        onErrorContainer = status.error.onContainer,
        scrim = surface.scrim,
    )
}

private fun AestheticTypography.toMaterialTypography() = Typography(
    titleLarge = title,
    titleMedium = itemLabel,
    bodyLarge = message,
    bodyMedium = message,
    bodySmall = supporting,
    labelLarge = action,
    labelMedium = caption,
    labelSmall = caption,
)
