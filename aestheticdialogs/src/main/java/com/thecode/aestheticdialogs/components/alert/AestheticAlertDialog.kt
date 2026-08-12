package com.thecode.aestheticdialogs.components.alert

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
 *     onPrimaryAction = { viewModel.retry() },
 *     onDismiss = { viewModel.dismissAlert() },
 *     onSecondaryAction = { viewModel.dismissAlert() },
 * )
 * ```
 *
 * [onDismiss] carries no default here, unlike a confirmation: an alert's second
 * action is optional, so there is no way back to fall through to. A back gesture
 * that reaches nobody is how a dialog becomes impossible to close.
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onPrimaryAction the primary action was pressed.
 * @param onDismiss the scrim was tapped, back was pressed, or the close
 *   affordance was used.
 * @param modifier applied to the dialog surface.
 * @param onSecondaryAction the secondary action was pressed. Only reachable when
 *   the model carries one.
 */
@Composable
public fun AestheticAlertDialog(
    uiModel: AlertDialogUiModel,
    onPrimaryAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onSecondaryAction: () -> Unit = {},
) {
    when (uiModel) {
        is AlertDialogUiModel.Default ->
            AlertDialogDefault(uiModel, onPrimaryAction, onSecondaryAction, onDismiss, modifier)
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
            onPrimaryAction = {},
            onDismiss = {},
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
            onPrimaryAction = {},
            onDismiss = {},
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
            onPrimaryAction = {},
            onDismiss = {},
        )
    }
}
