package com.thecode.aestheticdialogs.components.progress

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.progress.models.ProgressDialogUiModel
import com.thecode.aestheticdialogs.components.progress.variants.ProgressDialogDefault
import com.thecode.aestheticdialogs.components.progress.variants.ProgressDialogDeterminate
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews

/**
 * The dialog that says "wait".
 *
 * ```
 * AestheticProgressDialog(
 *     uiModel = ProgressDialogUiModel.Determinate(
 *         title = "Uploading",
 *         message = "Keep the app open until this finishes.",
 *         progress = uiState.uploaded / uiState.total.toFloat(),
 *         progressLabel = "${'$'}{uiState.uploaded} of ${'$'}{uiState.total}",
 *         cancelLabel = "Cancel upload",
 *     ),
 *     onCancel = { viewModel.cancelUpload() },
 * )
 * ```
 *
 * It blocks the back gesture and taps outside it, so the only way out is the
 * cancel action or your own state changing. That is the one dialog in the library
 * whose dismiss behaviour is not the caller's to choose.
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param modifier applied to the dialog surface.
 * @param onCancel the cancel action was pressed. Cancelling the work is yours to
 *   do — the dialog stays until you take it away. Only reachable when the model
 *   carries a `cancelLabel`.
 */
@Composable
public fun AestheticProgressDialog(
    uiModel: ProgressDialogUiModel,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
) {
    when (uiModel) {
        is ProgressDialogUiModel.Default -> ProgressDialogDefault(uiModel, onCancel, modifier)
        is ProgressDialogUiModel.Determinate ->
            ProgressDialogDeterminate(uiModel, onCancel, modifier)
    }
}

@ThemePreviews
@Composable
private fun ProgressDialogDefaultPreview() {
    AestheticPreviewSurface {
        AestheticProgressDialog(
            uiModel = ProgressDialogUiModel.Default(
                title = "Signing you in",
                message = "This takes a moment on a slow connection.",
            ),
        )
    }
}

@ThemePreviews
@Composable
private fun ProgressDialogDeterminatePreview() {
    AestheticPreviewSurface {
        AestheticProgressDialog(
            uiModel = ProgressDialogUiModel.Determinate(
                title = "Uploading",
                message = "Keep the app open until this finishes.",
                progress = 0.5f,
                progressLabel = "12 of 24",
                tone = DialogTone.Info,
                cancelLabel = "Cancel upload",
            ),
            onCancel = {},
        )
    }
}
