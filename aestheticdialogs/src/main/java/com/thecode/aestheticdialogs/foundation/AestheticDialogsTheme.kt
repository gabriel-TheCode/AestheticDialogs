package com.thecode.aestheticdialogs.foundation

import android.provider.Settings
import androidx.compose.foundation.isSystemInDarkTheme
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
 * Wrap your application content once — inside your own theme, not instead of it
 * — and every dialog picks up the same colours, type, shapes and motion:
 *
 * ```
 * MyAppTheme {                 // yours: untouched
 *     AestheticDialogsTheme {  // ours: four CompositionLocals
 *         AppNavHost()
 *     }
 * }
 * ```
 *
 * ### It does not restyle your application
 *
 * This composable provides four CompositionLocals and nothing else. It installs
 * no `MaterialTheme`, so your colour scheme, type scale and shapes survive it,
 * and any of your own components rendered inside a
 * [com.thecode.aestheticdialogs.components.content.AestheticContentDialog] still
 * look like yours. Wrapping the whole application is safe; wrapping only the
 * screens that show dialogs is equally fine.
 *
 * ### Branding
 *
 * Branding is done by copying a scheme rather than by implementing one. To make
 * dialogs follow a brand you already have, see [withBrand]:
 *
 * ```
 * AestheticDialogsTheme(
 *     colors = aestheticLightColors().withBrand(
 *         primary = MaterialTheme.colorScheme.primary,
 *         onPrimary = MaterialTheme.colorScheme.onPrimary,
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
 *
 * @param darkTheme whether to resolve the dark scheme. Defaults to the system
 *   setting; pass it explicitly if your application has its own theme switch.
 * @param colors the semantic colour roles. Copy [aestheticLightColors] or
 *   [aestheticDarkColors] rather than building one from scratch.
 * @param typography the six type roles the library renders.
 * @param shapes the silhouette of dialogs, banners and controls.
 * @param motion durations, easing, and the reduced-motion switch. Defaults to
 *   [AestheticDialogsTheme.defaultMotion], which reads the platform animation
 *   scale.
 * @param content your application content. Every AestheticDialogs component
 *   composed inside it resolves against these values.
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
        content = content,
    )
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
     *
     * @return the default motion tokens, with `enabled = false` when the user has
     *   asked the system to remove animations.
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
