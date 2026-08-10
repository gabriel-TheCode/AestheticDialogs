package com.thecode.aestheticdialogs.foundation

import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.tokens.AestheticPalette

/**
 * The two shipped colour schemes.
 *
 * Both are ordinary values, so an application that needs a branded variant does
 * `aestheticLightColors().copy(action = ...)` rather than reimplementing a
 * scheme. There is no dynamic colour: a design system exists to guarantee that
 * a warning looks like a warning, and a wallpaper-derived palette cannot make
 * that promise.
 *
 * @return the light colour scheme.
 */
public fun aestheticLightColors(): AestheticColors = AestheticColors(
    surface = AestheticColors.Surface(
        container = AestheticPalette.White,
        raised = AestheticPalette.White,
        sunken = AestheticPalette.Grey50,
        scrim = AestheticPalette.Black.copy(alpha = 0.32f),
    ),
    content = AestheticColors.Content(
        primary = AestheticPalette.Grey900,
        secondary = AestheticPalette.Grey700,
        muted = AestheticPalette.Grey500,
        disabled = AestheticPalette.Grey400,
        inverse = AestheticPalette.White,
    ),
    border = AestheticColors.Border(
        subtle = AestheticPalette.Grey100,
        default = AestheticPalette.Grey200,
        focus = AestheticPalette.BlueDeep,
    ),
    status = AestheticColors.Status(
        success = AestheticColors.Tone(
            accent = AestheticPalette.GreenDeep,
            onAccent = AestheticPalette.White,
            container = AestheticPalette.GreenTintLight,
            onContainer = AestheticPalette.Grey900,
        ),
        error = AestheticColors.Tone(
            accent = AestheticPalette.RedDeep,
            onAccent = AestheticPalette.White,
            container = AestheticPalette.RedTintLight,
            onContainer = AestheticPalette.Grey900,
        ),
        warning = AestheticColors.Tone(
            accent = AestheticPalette.AmberDeep,
            onAccent = AestheticPalette.White,
            container = AestheticPalette.AmberTintLight,
            onContainer = AestheticPalette.Grey900,
        ),
        info = AestheticColors.Tone(
            accent = AestheticPalette.BlueDeep,
            onAccent = AestheticPalette.White,
            container = AestheticPalette.BlueTintLight,
            onContainer = AestheticPalette.Grey900,
        ),
        neutral = AestheticColors.Tone(
            accent = AestheticPalette.Grey700,
            onAccent = AestheticPalette.White,
            container = AestheticPalette.Grey100,
            onContainer = AestheticPalette.Grey900,
        ),
    ),
    action = AestheticColors.Action(
        primary = AestheticPalette.BlueDeep,
        onPrimary = AestheticPalette.White,
        secondaryContent = AestheticPalette.Grey700,
        secondaryBorder = AestheticPalette.Grey200,
        disabledContainer = AestheticPalette.Grey100,
        disabledContent = AestheticPalette.Grey400,
    ),
    isDark = false,
)

/**
 * The dark counterpart of [aestheticLightColors], role for role.
 *
 * @return the dark colour scheme.
 */
public fun aestheticDarkColors(): AestheticColors = AestheticColors(
    surface = AestheticColors.Surface(
        container = AestheticPalette.Grey900,
        raised = AestheticPalette.Grey800,
        sunken = AestheticPalette.Grey950,
        scrim = AestheticPalette.Black.copy(alpha = 0.6f),
    ),
    content = AestheticColors.Content(
        primary = AestheticPalette.Grey50,
        secondary = AestheticPalette.Grey200,
        muted = AestheticPalette.Grey400,
        disabled = AestheticPalette.Grey500,
        inverse = AestheticPalette.Grey900,
    ),
    border = AestheticColors.Border(
        subtle = AestheticPalette.Grey800,
        default = AestheticPalette.Grey700,
        focus = AestheticPalette.BlueBright,
    ),
    status = AestheticColors.Status(
        success = AestheticColors.Tone(
            accent = AestheticPalette.GreenBright,
            onAccent = AestheticPalette.Grey900,
            container = AestheticPalette.GreenTintDark,
            onContainer = AestheticPalette.Grey50,
        ),
        error = AestheticColors.Tone(
            accent = AestheticPalette.RedBright,
            onAccent = AestheticPalette.Grey900,
            container = AestheticPalette.RedTintDark,
            onContainer = AestheticPalette.Grey50,
        ),
        warning = AestheticColors.Tone(
            accent = AestheticPalette.AmberBright,
            onAccent = AestheticPalette.Grey900,
            container = AestheticPalette.AmberTintDark,
            onContainer = AestheticPalette.Grey50,
        ),
        info = AestheticColors.Tone(
            accent = AestheticPalette.BlueBright,
            onAccent = AestheticPalette.Grey900,
            container = AestheticPalette.BlueTintDark,
            onContainer = AestheticPalette.Grey50,
        ),
        neutral = AestheticColors.Tone(
            accent = AestheticPalette.Grey200,
            onAccent = AestheticPalette.Grey900,
            container = AestheticPalette.Grey800,
            onContainer = AestheticPalette.Grey50,
        ),
    ),
    action = AestheticColors.Action(
        primary = AestheticPalette.BlueBright,
        onPrimary = AestheticPalette.Grey900,
        secondaryContent = AestheticPalette.Grey200,
        secondaryBorder = AestheticPalette.Grey700,
        disabledContainer = AestheticPalette.Grey800,
        disabledContent = AestheticPalette.Grey500,
    ),
    isDark = true,
)

/**
 * Re-brands a scheme from the handful of roles an application actually owns.
 *
 * Most applications arriving at AestheticDialogs already have a brand, and want
 * the dialogs to look like the rest of the product without rewriting a colour
 * scheme by hand. This is the one-liner for that. It takes plain [Color] values
 * rather than a Material `ColorScheme`, so Material 3 stays an implementation
 * detail of the library rather than leaking into its public API — a Material
 * host simply passes its own scheme's values in:
 *
 * ```
 * AestheticDialogsTheme(
 *     colors = aestheticLightColors().withBrand(
 *         primary = MaterialTheme.colorScheme.primary,
 *         onPrimary = MaterialTheme.colorScheme.onPrimary,
 *         surface = MaterialTheme.colorScheme.surface,
 *         onSurface = MaterialTheme.colorScheme.onSurface,
 *     ),
 * ) {
 *     AppNavHost()
 * }
 * ```
 *
 * The status tones are deliberately **not** re-brandable here: an error has to
 * look like an error in every application, and a brand that repainted it would
 * be removing the one thing a design system is for. Override
 * [AestheticColors.status] explicitly if you really mean to.
 *
 * @param primary the fill of the primary action, and the accent of selection
 *   controls, focus rings and text cursors.
 * @param onPrimary content drawn on [primary].
 * @param surface the dialog and banner container.
 * @param onSurface titles and primary body copy.
 * @return a copy of this scheme carrying the brand.
 */
public fun AestheticColors.withBrand(
    primary: Color,
    onPrimary: Color,
    surface: Color = this.surface.container,
    onSurface: Color = this.content.primary,
): AestheticColors = copy(
    surface = this.surface.copy(container = surface, raised = surface),
    content = content.copy(primary = onSurface),
    border = border.copy(focus = primary),
    action = action.copy(primary = primary, onPrimary = onPrimary),
)
