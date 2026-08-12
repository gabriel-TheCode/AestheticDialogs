package com.thecode.aestheticdialogs.components.input.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.KeyboardType
import com.thecode.aestheticdialogs.model.DialogDismissBehavior

/**
 * The visual state of an input dialog.
 *
 * The value lives in your state and arrives back through `onValueChange`, the
 * same contract as `TextField`. Validation lives in your state too: the dialog
 * renders [errorText] and disables the confirm button, it never decides that an
 * email address is malformed.
 */
public sealed interface InputDialogUiModel {

    public val title: String
    public val message: String?
    public val value: String
    public val label: String?
    public val placeholder: String?

    /** Helper text shown under the field. Replaced by [errorText] when that is set. */
    public val supportingText: String?

    /** Non-null puts the field in its error state and announces the message. */
    public val errorText: String?
    public val confirmLabel: String
    public val cancelLabel: String
    public val isConfirmEnabled: Boolean
    public val isConfirming: Boolean
    public val dismissBehavior: DialogDismissBehavior

    /**
     * A single- or multi-line text entry.
     *
     * [keyboardType] is the one place the library exposes a platform type in a UI
     * model, because "this field holds an email address" is a UI semantic and
     * reinventing the enum would only make it harder to pass through.
     */
    @Immutable
    public data class Text(
        override val title: String,
        override val value: String,
        override val confirmLabel: String,
        override val cancelLabel: String,
        override val message: String? = null,
        override val label: String? = null,
        override val placeholder: String? = null,
        override val supportingText: String? = null,
        override val errorText: String? = null,
        val keyboardType: KeyboardType = KeyboardType.Text,
        val singleLine: Boolean = true,
        override val isConfirmEnabled: Boolean = true,
        override val isConfirming: Boolean = false,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : InputDialogUiModel

    /**
     * A masked entry with a reveal toggle.
     *
     * Whether the value is currently revealed is *not* in this model: it is
     * presentation state with no meaning outside the dialog, so the component
     * keeps it in `rememberSaveable` and it survives rotation without the caller
     * carrying a boolean that means nothing to their feature.
     */
    @Immutable
    public data class Password(
        override val title: String,
        override val value: String,
        override val confirmLabel: String,
        override val cancelLabel: String,
        override val message: String? = null,
        override val label: String? = null,
        override val placeholder: String? = null,
        override val supportingText: String? = null,
        override val errorText: String? = null,
        override val isConfirmEnabled: Boolean = true,
        override val isConfirming: Boolean = false,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : InputDialogUiModel
}
