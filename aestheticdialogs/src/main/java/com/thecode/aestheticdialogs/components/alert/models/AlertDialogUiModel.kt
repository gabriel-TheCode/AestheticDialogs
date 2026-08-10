package com.thecode.aestheticdialogs.components.alert.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogDismissBehavior

/**
 * The visual state of an alert dialog.
 *
 * An alert tells the user something and offers a way forward. Unlike a
 * confirmation, the roles of its buttons are not fixed — "Retry"/"Cancel",
 * "Update now"/"Later", or a single "Got it" — so it takes
 * [DialogAction] objects and lets the caller choose the emphasis.
 *
 * This is also the component that covers the error, offline and
 * permission-required patterns. They are the same dialog with a different tone,
 * icon and action label, and shipping three near-identical components would have
 * been three ways to get the same thing subtly wrong.
 */
public sealed interface AlertDialogUiModel {

    /**
     * The only variant, and deliberately so: an alert has one shape.
     *
     * It is still a sealed hierarchy rather than a bare data class, so a future
     * variant (an alert with an illustration, say) can be added without changing
     * a single call site.
     */
    @Immutable
    public data class Default(
        val title: String,
        val message: String? = null,
        val tone: DialogTone = DialogTone.Info,
        /**
         * Replaces the tone mark the library would otherwise draw.
         *
         * Supply one when the alert is about a specific thing — a payment, a
         * device — rather than about a status.
         */
        val icon: ImageVector? = null,
        val primaryAction: DialogAction,
        val secondaryAction: DialogAction? = null,
        val showCloseButton: Boolean = false,
        val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : AlertDialogUiModel
}

/** What the user did with an alert dialog. */
public sealed interface AlertDialogSignal {
    public data object PrimaryActionClicked : AlertDialogSignal
    public data object SecondaryActionClicked : AlertDialogSignal
    public data object Dismissed : AlertDialogSignal
}
