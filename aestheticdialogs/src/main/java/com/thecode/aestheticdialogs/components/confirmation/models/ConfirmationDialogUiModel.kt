package com.thecode.aestheticdialogs.components.confirmation.models

import androidx.compose.runtime.Immutable
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogDismissBehavior

/**
 * The visual state of a confirmation dialog.
 *
 * A confirmation asks one question and offers exactly two answers, so — unlike
 * the alert and content dialogs — it does not take
 * [com.thecode.aestheticdialogs.model.DialogAction] objects. The roles of the two
 * buttons are fixed, and encoding them here means a caller cannot accidentally
 * make "cancel" the primary action or give a destructive confirmation a neutral
 * button. This is the design system being opinionated where the answer is known,
 * and flexible where it is not.
 */
public sealed interface ConfirmationDialogUiModel {

    public val title: String
    public val message: String?
    public val confirmLabel: String
    public val cancelLabel: String
    public val isConfirmEnabled: Boolean

    /** Shows a spinner on the confirm button and blocks every other interaction. */
    public val isConfirming: Boolean
    public val dismissBehavior: DialogDismissBehavior

    /**
     * The everyday confirmation: "Send this?", "Leave without saving?".
     *
     * [tone] colours the confirm button and the header mark. Leave it
     * [DialogTone.Neutral] unless the question itself carries a status.
     */
    @Immutable
    public data class Default(
        override val title: String,
        override val message: String? = null,
        override val confirmLabel: String,
        override val cancelLabel: String,
        val tone: DialogTone = DialogTone.Neutral,
        override val isConfirmEnabled: Boolean = true,
        override val isConfirming: Boolean = false,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : ConfirmationDialogUiModel

    /**
     * A confirmation for something that cannot be undone: delete, revoke, sign
     * out of every device.
     *
     * The treatment is guaranteed rather than configured — error mark, error
     * confirm button, cancel presented as the safe way out — because getting
     * this wrong has a cost the user pays.
     */
    @Immutable
    public data class Destructive(
        override val title: String,
        override val message: String? = null,
        override val confirmLabel: String,
        override val cancelLabel: String,
        override val isConfirmEnabled: Boolean = true,
        override val isConfirming: Boolean = false,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : ConfirmationDialogUiModel
}
