package com.thecode.aestheticdialogs.components.feedback

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogSignal
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogUiModel
import com.thecode.aestheticdialogs.components.feedback.variants.FeedbackDialogFlash
import com.thecode.aestheticdialogs.components.feedback.variants.FeedbackDialogFlat
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews

/**
 * The modal "here is what happened" dialog.
 *
 * ```
 * AestheticFeedbackDialog(
 *     uiModel = FeedbackDialogUiModel.Flash(
 *         title = "Message sent",
 *         message = "It will arrive even if you close the app.",
 *         tone = DialogTone.Success,
 *         actionLabel = "Nice",
 *     ),
 *     onSignal = { viewModel.dismissFeedback() },
 * )
 * ```
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onSignal receives the action tap and dismiss requests.
 * @param modifier applied to the dialog surface.
 */
@Composable
public fun AestheticFeedbackDialog(
    uiModel: FeedbackDialogUiModel,
    onSignal: (FeedbackDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiModel) {
        is FeedbackDialogUiModel.Flat -> FeedbackDialogFlat(uiModel, onSignal, modifier)
        is FeedbackDialogUiModel.Flash -> FeedbackDialogFlash(uiModel, onSignal, modifier)
    }
}

@ThemePreviews
@Composable
private fun FeedbackDialogFlatPreview() {
    AestheticPreviewSurface {
        AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Flat(
                title = "Something went wrong",
                message = "We could not reach the server. Try again in a moment.",
                tone = DialogTone.Error,
                actionLabel = "OK",
            ),
            onSignal = {},
        )
    }
}

@ThemePreviews
@Composable
private fun FeedbackDialogFlashPreview() {
    AestheticPreviewSurface {
        AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Flash(
                title = "Message sent",
                message = "It will arrive even if you close the app.",
                tone = DialogTone.Success,
                actionLabel = "Nice",
            ),
            onSignal = {},
        )
    }
}

@ThemePreviews
@Composable
private fun FeedbackDialogWarningPreview() {
    AestheticPreviewSurface {
        AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Flat(
                title = "Check your fields",
                message = "Two of them are still empty.",
                tone = DialogTone.Warning,
                actionLabel = "Go back",
            ),
            onSignal = {},
        )
    }
}
