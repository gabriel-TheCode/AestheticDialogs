package com.thecode.aestheticdialogs.components.feedback

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogUiModel
import com.thecode.aestheticdialogs.components.feedback.variants.FeedbackDialogCompact
import com.thecode.aestheticdialogs.components.feedback.variants.FeedbackDialogDefault
import com.thecode.aestheticdialogs.components.feedback.variants.FeedbackDialogGradient
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews

/**
 * The modal "here is what happened" dialog.
 *
 * ```
 * AestheticFeedbackDialog(
 *     uiModel = FeedbackDialogUiModel.Default(
 *         title = "Message sent",
 *         message = "It will arrive even if you close the app.",
 *         tone = DialogTone.Success,
 *         actionLabel = "Nice",
 *     ),
 *     onDismiss = { viewModel.dismissFeedback() },
 * )
 * ```
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onDismiss the scrim was tapped or back was pressed.
 * @param modifier applied to the dialog surface.
 * @param onAction the single action was pressed. Defaults to [onDismiss],
 *   because a feedback dialog's one button almost always means "I have read
 *   it" — override it when yours means something else.
 */
@Composable
public fun AestheticFeedbackDialog(
    uiModel: FeedbackDialogUiModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onAction: () -> Unit = onDismiss,
) {
    when (uiModel) {
        is FeedbackDialogUiModel.Default ->
            FeedbackDialogDefault(uiModel, onAction, onDismiss, modifier)

        is FeedbackDialogUiModel.Gradient ->
            FeedbackDialogGradient(uiModel, onAction, onDismiss, modifier)

        is FeedbackDialogUiModel.Compact ->
            FeedbackDialogCompact(uiModel, onAction, onDismiss, modifier)
    }
}

@ThemePreviews
@Composable
private fun FeedbackDialogFlatPreview() {
    AestheticPreviewSurface {
        AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Default(
                title = "Something went wrong",
                message = "We could not reach the server. Try again in a moment.",
                tone = DialogTone.Error,
                actionLabel = "OK",
            ),
            onDismiss = {},
        )
    }
}

@ThemePreviews
@Composable
private fun FeedbackDialogFlashPreview() {
    AestheticPreviewSurface {
        AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Default(
                title = "Message sent",
                message = "It will arrive even if you close the app.",
                tone = DialogTone.Success,
                actionLabel = "Nice",
            ),
            onDismiss = {},
        )
    }
}

@ThemePreviews
@Composable
private fun FeedbackDialogWarningPreview() {
    AestheticPreviewSurface {
        AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Default(
                title = "Check your fields",
                message = "Two of them are still empty.",
                tone = DialogTone.Warning,
                actionLabel = "Go back",
            ),
            onDismiss = {},
        )
    }
}

@ThemePreviews
@Composable
private fun FeedbackDialogCompactPreview() {
    AestheticPreviewSurface {
        AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Compact(
                title = "Album archived",
                tone = DialogTone.Success,
                actionLabel = "Undo",
            ),
            onDismiss = {},
        )
    }
}
