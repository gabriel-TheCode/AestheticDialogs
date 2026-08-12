package com.thecode.aestheticdialogs.components.confirmation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.confirmation.models.ConfirmationDialogUiModel
import com.thecode.aestheticdialogs.components.confirmation.variants.ConfirmationDialogDefault
import com.thecode.aestheticdialogs.components.confirmation.variants.ConfirmationDialogDestructive
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.FontScalePreviews
import com.thecode.aestheticdialogs.preview.ThemePreviews

/**
 * Asks the user to confirm an action.
 *
 * The dialog is stateless: it is in the composition when you decide it is, and
 * it leaves when you remove it. It never dismisses itself, which is why
 * [onDismiss] is a request rather than a fact.
 *
 * ```
 * if (uiState.showDeleteConfirmation) {
 *     AestheticConfirmationDialog(
 *         uiModel = ConfirmationDialogUiModel.Destructive(
 *             title = "Delete this album?",
 *             message = "The 24 photos inside it will be deleted too.",
 *             confirmLabel = "Delete album",
 *             cancelLabel = "Keep it",
 *             isConfirming = uiState.isDeleting,
 *         ),
 *         onConfirm = { viewModel.deleteAlbum() },
 *         onCancel = { viewModel.dismissDeleteConfirmation() },
 *         onDismiss = { viewModel.dismissDeleteConfirmation() },
 *     )
 * }
 * ```
 *
 * Pressing "Cancel" is a decision; tapping the scrim or pressing back is a
 * retreat. They are separate callbacks because analytics and "are you sure you
 * want to leave" flows care about the difference — and [onDismiss] defaults to
 * [onCancel], because a confirmation always has a way back and treating a
 * retreat as one is the right answer until a caller says otherwise.
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onConfirm the confirm button was pressed.
 * @param onCancel the cancel button was pressed.
 * @param modifier applied to the dialog surface, not to the window.
 * @param onDismiss the scrim was tapped or back was pressed. Defaults to
 *   [onCancel]. The dialog stays on screen until you remove it.
 */
@Composable
public fun AestheticConfirmationDialog(
    uiModel: ConfirmationDialogUiModel,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = onCancel,
) {
    when (uiModel) {
        is ConfirmationDialogUiModel.Default ->
            ConfirmationDialogDefault(uiModel, onConfirm, onCancel, onDismiss, modifier)

        is ConfirmationDialogUiModel.Destructive ->
            ConfirmationDialogDestructive(uiModel, onConfirm, onCancel, onDismiss, modifier)
    }
}

@ThemePreviews
@Composable
private fun ConfirmationDialogDefaultPreview() {
    AestheticPreviewSurface {
        AestheticConfirmationDialog(
            uiModel = ConfirmationDialogUiModel.Default(
                title = "Leave without saving?",
                message = "Your draft has unsaved changes. They will be lost.",
                confirmLabel = "Leave",
                cancelLabel = "Keep editing",
            ),
            onConfirm = {},
            onCancel = {},
        )
    }
}

@ThemePreviews
@Composable
private fun ConfirmationDialogDestructivePreview() {
    AestheticPreviewSurface {
        AestheticConfirmationDialog(
            uiModel = ConfirmationDialogUiModel.Destructive(
                title = "Delete this album?",
                message = "The 24 photos inside it will be deleted too. " +
                    "This cannot be undone.",
                confirmLabel = "Delete album",
                cancelLabel = "Keep it",
            ),
            onConfirm = {},
            onCancel = {},
        )
    }
}

/** The layout that breaks first: a long question at a doubled font size. */
@FontScalePreviews
@Composable
private fun ConfirmationDialogFontScalePreview() {
    AestheticPreviewSurface {
        AestheticConfirmationDialog(
            uiModel = ConfirmationDialogUiModel.Destructive(
                title = "Delete this album and everything inside it?",
                message = "The 24 photos inside it will be deleted too, on every " +
                    "device you are signed in to. This cannot be undone.",
                confirmLabel = "Delete album",
                cancelLabel = "Keep it",
            ),
            onConfirm = {},
            onCancel = {},
        )
    }
}

@ThemePreviews
@Composable
private fun ConfirmationDialogConfirmingPreview() {
    AestheticPreviewSurface {
        AestheticConfirmationDialog(
            uiModel = ConfirmationDialogUiModel.Destructive(
                title = "Delete this album?",
                message = "The 24 photos inside it will be deleted too.",
                confirmLabel = "Delete album",
                cancelLabel = "Keep it",
                isConfirming = true,
            ),
            onConfirm = {},
            onCancel = {},
        )
    }
}
