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
internal fun FeedbackDialogDefault(
    uiModel: FeedbackDialogUiModel.Default,
    onAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val tone = colors.status.forTone(uiModel.tone)
    val neutral = uiModel.tone == DialogTone.Neutral

    FeedbackPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        titleColor = colors.content.primary,
        messageColor = colors.content.secondary,
        containerColor = colors.surface.container,
        onDismissRequest = onDismiss,
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        modifier = modifier,
        // A neutral dialog carries no status, so it gets no status mark: the
        // glyph for `Neutral` is the info mark, and drawing it would say the one
        // thing the tone exists to not say.
        mark = GlyphMark.forTone(uiModel.tone).takeIf { !neutral },
        markColor = tone.accent,
        markContainerColor = tone.onAccent,
        actionLabel = uiModel.actionLabel,
        actionContainerColor = if (neutral) colors.action.primary else tone.accent,
        actionContentColor = if (neutral) colors.action.onPrimary else tone.onAccent,
        onActionClick = onAction,
    )
}
