package com.thecode.aestheticdialogs.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * The semantic colour roles of AestheticDialogs.
 *
 * Two levels deep, never more: `colors.content.primary`, `colors.status.error`.
 * Deeply nested schemes make every call site unreadable and every review slow.
 *
 * Only the semantic layer is visible to components. Raw hues live in
 * [com.thecode.aestheticdialogs.tokens.AestheticPalette] and are `internal`,
 * which is what lets the dark theme be a pure remapping instead of a second
 * implementation.
 */
@Immutable
public data class AestheticColors(
    /** Container and overlay roles. */
    val surface: Surface,
    /** Text and icon roles. */
    val content: Content,
    /** Outline and focus-ring roles. */
    val border: Border,
    /** The five status tones. */
    val status: Status,
    /** Button roles. */
    val action: Action,
    /** Whether this scheme is meant for a dark background. Used to pick assets and scrim opacity. */
    val isDark: Boolean,
) {

    /** Background roles. */
    @Immutable
    public data class Surface(
        /** The dialog and banner container. */
        val container: Color,
        /** A container raised above [container], used for nested content. */
        val raised: Color,
        /** A recessed area, used for search fields and inactive rows. */
        val sunken: Color,
        /** The overlay painted behind a modal dialog. */
        val scrim: Color,
    )

    /** Foreground roles. */
    @Immutable
    public data class Content(
        /** Titles and primary body copy. */
        val primary: Color,
        /** Supporting copy and secondary rows. */
        val secondary: Color,
        /** Captions, timestamps and other low-emphasis text. */
        val muted: Color,
        /** Text and icons of a disabled control. */
        val disabled: Color,
        /** Content drawn on top of an inverted surface. */
        val inverse: Color,
    )

    /** Outline roles. */
    @Immutable
    public data class Border(
        /** Hairlines between rows, where a full-strength line would be noise. */
        val subtle: Color,
        /** The default outline: secondary buttons, text fields, cards. */
        val default: Color,
        /** Focus ring, drawn for keyboard and D-pad navigation. */
        val focus: Color,
    )

    /**
     * The four status tones plus a neutral one.
     *
     * Use [forTone] rather than reading a field directly: it keeps variants free
     * of `when` blocks over the tone enum.
     */
    @Immutable
    public data class Status(
        /** Something completed. */
        val success: Tone,
        /** Something failed, and the user has to know. */
        val error: Tone,
        /** Something needs attention but has not failed. */
        val warning: Tone,
        /** Something is worth knowing and carries no urgency. */
        val info: Tone,
        /** No status at all: confirmations, pickers, forms. */
        val neutral: Tone,
    ) {
        /**
         * Resolves the colours for [tone].
         *
         * @param tone the semantic tone carried by the dialog or banner.
         * @return the four colour roles for that tone in the current scheme.
         */
        public fun forTone(tone: DialogTone): Tone = when (tone) {
            DialogTone.Success -> success
            DialogTone.Error -> error
            DialogTone.Warning -> warning
            DialogTone.Info -> info
            DialogTone.Neutral -> neutral
        }
    }

    /**
     * One status tone, in the four roles a dialog needs.
     *
     * [accent] is guaranteed to clear 3:1 against [Surface.container], the
     * graphical-object contrast requirement; the shipped schemes are well above
     * it. Dialog titles still use [Content.primary] rather than the accent, so a
     * rebranded scheme cannot make a title unreadable by moving one hue.
     */
    @Immutable
    public data class Tone(
        /** The hue itself: marks, bars, filled buttons. */
        val accent: Color,
        /** Content drawn on top of [accent]. */
        val onAccent: Color,
        /** A tinted backdrop for the tone, such as the badge disc. */
        val container: Color,
        /** Content drawn on top of [container]. */
        val onContainer: Color,
    )

    /** Action (button) roles. */
    @Immutable
    public data class Action(
        /** Fill of the primary button. */
        val primary: Color,
        /** Label of the primary button. */
        val onPrimary: Color,
        /** Label of the secondary (outlined) and text buttons. */
        val secondaryContent: Color,
        /** Outline of the secondary button. */
        val secondaryBorder: Color,
        /** Fill of a disabled primary button. */
        val disabledContainer: Color,
        /** Label of any disabled button. */
        val disabledContent: Color,
    )
}

/**
 * The semantic meaning carried by a dialog.
 *
 * The only "type" concept in the public API. [Neutral] is for dialogs that
 * carry no status at all — a confirmation, a picker, a form.
 */
public enum class DialogTone {
    Success,
    Error,
    Warning,
    Info,
    Neutral,
}
