package com.thecode.aestheticdialogs.components.feedback.variants

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogUiModel
import com.thecode.aestheticdialogs.components.feedback.primitives.FeedbackPrimitive
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.variants.toneGradient

@Composable
internal fun FeedbackDialogGradient(
    uiModel: FeedbackDialogUiModel.Gradient,
    onAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tone = AestheticDialogsTheme.colors.status.forTone(uiModel.tone)

    FeedbackPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        titleColor = tone.onAccent,
        messageColor = tone.onAccent,
        containerColor = Color.Transparent,
        onDismissRequest = onDismiss,
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        modifier = modifier,
        containerBrush = toneGradient(uiModel.tone),
        mark = GlyphMark.forTone(uiModel.tone).takeIf { uiModel.tone != DialogTone.Neutral },
        markColor = tone.onAccent,
        markContainerColor = tone.accent,
        actionLabel = uiModel.actionLabel,
        actionContentColor = tone.onAccent,
        actionBorder = BorderStroke(AestheticDimens.borderWidth, tone.onAccent),
        onActionClick = onAction,
    )
}
