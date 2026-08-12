package com.thecode.aestheticdialogs.components.feedback.variants

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogUiModel
import com.thecode.aestheticdialogs.components.feedback.primitives.FeedbackCompactPrimitive
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.primitives.GlyphMark

/** One line, one action: the smallest surface the library draws. */
@Composable
internal fun FeedbackDialogCompact(
    uiModel: FeedbackDialogUiModel.Compact,
    onAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val tone = colors.status.forTone(uiModel.tone)
    val neutral = uiModel.tone == DialogTone.Neutral

    FeedbackCompactPrimitive(
        title = uiModel.title,
        titleColor = colors.content.primary,
        containerColor = colors.surface.container,
        onDismissRequest = onDismiss,
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        modifier = modifier,
        mark = GlyphMark.forTone(uiModel.tone).takeIf { !neutral },
        markColor = tone.accent,
        markContainerColor = tone.container,
        actionLabel = uiModel.actionLabel,
        actionContentColor = if (neutral) colors.action.primary else tone.accent,
        onActionClick = onAction,
    )
}
