package com.thecode.aestheticdialogs.components.sheet.models

import androidx.compose.runtime.Immutable
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogDismissBehavior

/**
 * The visual state of a sheet — the dialog that docks to the bottom of the window
 * instead of floating in the middle of it.
 *
 * A sheet is the one surface in the library whose *position* is the point: on a
 * phone its actions sit under the thumb, which is why applications reach for a
 * bottom sheet and leave their design system behind. Above the medium breakpoint
 * there is no thumb argument left to make, and the same model renders as a
 * centred dialog.
 */
public sealed interface SheetDialogUiModel {

    /**
     * @property title announced as the dialog's accessibility pane.
     * @property message supporting copy under the title.
     * @property primaryAction the action you want pressed, or `null` for a sheet
     *   dismissed by gesture alone.
     * @property secondaryAction the way back.
     * @property showDragHandle whether the grab bar is drawn. Keep it: the handle
     *   is what tells the user the sheet can be dragged away, and hiding it while
     *   leaving the gesture on is a hidden control.
     * @property showCloseButton draws a close affordance next to the title.
     * @property dismissBehavior what counts as a request to dismiss.
     */
    @Immutable
    public data class Default(
        val title: String,
        val message: String? = null,
        val primaryAction: DialogAction? = null,
        val secondaryAction: DialogAction? = null,
        val showDragHandle: Boolean = true,
        val showCloseButton: Boolean = false,
        val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : SheetDialogUiModel
}
