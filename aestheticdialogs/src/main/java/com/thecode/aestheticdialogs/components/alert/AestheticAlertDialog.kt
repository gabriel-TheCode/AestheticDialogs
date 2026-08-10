package com.thecode.aestheticdialogs.components.alert

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.alert.models.AlertDialogSignal
import com.thecode.aestheticdialogs.components.alert.models.AlertDialogUiModel
import com.thecode.aestheticdialogs.components.alert.variants.AlertDialogDefault
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews

/**
 * Tells the user something they need to know, and offers a way forward.
 *
 * ```
 * AestheticAlertDialog(
 *     uiModel = AlertDialogUiModel.Default(
 *         title = "You are offline",
 *         message = "We will sync your changes as soon as you reconnect.",
 *         tone = DialogTone.Warning,
 *         primaryAction = DialogAction("Retry"),
 *         secondaryAction = DialogAction("Dismiss", DialogActionEmphasis.Text),
 *     ),
 *     onSignal = { signal ->
 *         when (signal) {
 *             AlertDialogSignal.PrimaryActionClicked -> viewModel.retry()
 *             AlertDialogSignal.SecondaryActionClicked,
 *             AlertDialogSignal.Dismissed -> viewModel.dismissAlert()
 *         }
 *     },
 * )
 * ```
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onSignal receives what the user did.
 * @param modifier applied to the dialog surface.
 */
@Composable
public fun AestheticAlertDialog(
    uiModel: AlertDialogUiModel,
    onSignal: (AlertDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiModel) {
        is AlertDialogUiModel.Default -> AlertDialogDefault(uiModel, onSignal, modifier)
    }
}

@ThemePreviews
@Composable
private fun AlertDialogInfoPreview() {
    AestheticPreviewSurface {
        AestheticAlertDialog(
            uiModel = AlertDialogUiModel.Default(
                title = "Update available",
                message = "Version 3.4 adds offline albums and fixes the sync stall.",
                tone = DialogTone.Info,
                primaryAction = DialogAction("Update now"),
                secondaryAction = DialogAction("Later", DialogActionEmphasis.Text),
            ),
            onSignal = {},
        )
    }
}

@ThemePreviews
@Composable
private fun AlertDialogErrorPreview() {
    AestheticPreviewSurface {
        AestheticAlertDialog(
            uiModel = AlertDialogUiModel.Default(
                title = "Upload failed",
                message = "The connection dropped after 12 of 24 photos.",
                tone = DialogTone.Error,
                primaryAction = DialogAction("Retry"),
                secondaryAction = DialogAction("Cancel", DialogActionEmphasis.Secondary),
            ),
            onSignal = {},
        )
    }
}

@ThemePreviews
@Composable
private fun AlertDialogSingleActionPreview() {
    AestheticPreviewSurface {
        AestheticAlertDialog(
            uiModel = AlertDialogUiModel.Default(
                title = "Backup complete",
                message = "1,204 photos are now safe.",
                tone = DialogTone.Success,
                primaryAction = DialogAction("Got it"),
            ),
            onSignal = {},
        )
    }
}
