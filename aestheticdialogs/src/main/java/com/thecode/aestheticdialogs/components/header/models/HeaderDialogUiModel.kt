package com.thecode.aestheticdialogs.components.header.models

import androidx.compose.runtime.Immutable
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogDismissBehavior

/**
 * The visual state of a header dialog: a band across the top, then the copy and
 * the actions.
 *
 * This is the onboarding, paywall and "what's new" shape. The band is the whole
 * point of it — without one, callers who need a picture above a title drop to a
 * raw `Dialog {}` and lose the frame, the insets, the adaptive width and the
 * accessibility pane along with it.
 */
public sealed interface HeaderDialogUiModel {

    /**
     * @property title the headline under the band, announced as the dialog's
     *   accessibility pane.
     * @property message supporting copy.
     * @property tone paints the band when the caller supplies no image, and
     *   colours the primary action.
     * @property primaryAction the action you want pressed.
     * @property secondaryAction the way back.
     * @property showCloseButton draws a close affordance on the band. On by
     *   default: a dialog that fills its top third with a picture needs a visible
     *   way out, not only a gesture.
     * @property dismissBehavior what counts as a request to dismiss.
     */
    @Immutable
    public data class Default(
        val title: String,
        val message: String? = null,
        val tone: DialogTone = DialogTone.Info,
        val primaryAction: DialogAction,
        val secondaryAction: DialogAction? = null,
        val showCloseButton: Boolean = true,
        val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : HeaderDialogUiModel
}
