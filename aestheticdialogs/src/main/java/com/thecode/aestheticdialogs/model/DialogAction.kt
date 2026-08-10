package com.thecode.aestheticdialogs.model

import androidx.compose.runtime.Immutable

/**
 * A button in a dialog's action row.
 *
 * Shared by every dialog that has actions, so "confirm" looks and behaves the
 * same whether it sits in a confirmation, an alert or a form.
 *
 * Like every UI model in the library it holds no lambda: which action was
 * pressed is reported through the dialog's signal, and the caller decides what
 * that means. That is what keeps the model previewable, unit-testable and free
 * of the identity churn that lambdas cause in recomposition.
 */
@Immutable
public data class DialogAction(
    /** The label shown on the button. Supplied by the caller, so it is localised by the caller. */
    val label: String,
    /** How much visual weight the button carries, and whether it reads as destructive. */
    val emphasis: DialogActionEmphasis = DialogActionEmphasis.Primary,
    /** Whether the button accepts input. A disabled button is still announced, and still readable. */
    val enabled: Boolean = true,
    /**
     * Replaces the label with a progress indicator and blocks input on this
     * button. The rest of the dialog is disabled while an action is loading, so
     * a slow "delete" cannot be pressed twice or cancelled halfway.
     */
    val loading: Boolean = false,
)

/**
 * How much visual weight an action carries.
 *
 * Four values, one per real use: the action you want pressed, the way back, the
 * quiet alternative, and the one that destroys something.
 */
public enum class DialogActionEmphasis {
    /** Filled with the theme's action colour. At most one per dialog. */
    Primary,

    /** Outlined. The usual "cancel". */
    Secondary,

    /** No container. For tertiary choices such as "learn more". */
    Text,

    /**
     * Filled with the error tone, for actions that remove or overwrite something.
     *
     * The tone is a visual cue only; screen reader users get their warning from
     * the label, so write "Delete photo" rather than "OK".
     */
    Destructive,
}

/**
 * What counts as a request to dismiss.
 *
 * The back gesture and a tap outside are separate decisions. Blocking both is a
 * deliberate choice, and a blocked dialog must still offer a visible way out.
 */
@Immutable
public data class DialogDismissBehavior(
    /** Whether the back gesture requests dismissal. */
    val dismissOnBackPress: Boolean = true,
    /** Whether a tap on the scrim requests dismissal. */
    val dismissOnClickOutside: Boolean = true,
) {
    public companion object {
        /** Back and outside taps both request dismissal. */
        public val Default: DialogDismissBehavior = DialogDismissBehavior()

        /** Neither gesture dismisses; the dialog must expose an explicit action. */
        public val Blocking: DialogDismissBehavior = DialogDismissBehavior(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    }
}
