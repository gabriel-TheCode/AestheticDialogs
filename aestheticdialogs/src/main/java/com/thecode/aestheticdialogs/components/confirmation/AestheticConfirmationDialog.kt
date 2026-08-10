package com.thecode.aestheticdialogs.components.confirmation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.confirmation.models.ConfirmationDialogSignal
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
 * [ConfirmationDialogSignal.Dismissed] is a request rather than a fact.
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
 *         onSignal = { signal ->
 *             when (signal) {
 *                 ConfirmationDialogSignal.Confirmed -> viewModel.deleteAlbum()
 *                 ConfirmationDialogSignal.Cancelled,
 *                 ConfirmationDialogSignal.Dismissed -> viewModel.dismissDeleteConfirmation()
 *             }
 *         },
 *     )
 * }
 * ```
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onSignal receives what the user did. The caller decides what it means.
 * @param modifier applied to the dialog surface, not to the window.
 */
@Composable
public fun AestheticConfirmationDialog(
    uiModel: ConfirmationDialogUiModel,
    onSignal: (ConfirmationDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiModel) {
        is ConfirmationDialogUiModel.Default ->
            ConfirmationDialogDefault(uiModel, onSignal, modifier)

        is ConfirmationDialogUiModel.Destructive ->
            ConfirmationDialogDestructive(uiModel, onSignal, modifier)
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
            onSignal = {},
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
            onSignal = {},
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
            onSignal = {},
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
            onSignal = {},
        )
    }
}
