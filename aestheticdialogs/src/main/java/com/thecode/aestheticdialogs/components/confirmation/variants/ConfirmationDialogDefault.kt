package com.thecode.aestheticdialogs.components.confirmation.variants

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.confirmation.models.ConfirmationDialogUiModel
import com.thecode.aestheticdialogs.components.confirmation.primitives.ConfirmationPrimitive
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.variants.actionColors

@Composable
internal fun ConfirmationDialogDefault(
    uiModel: ConfirmationDialogUiModel.Default,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tone = uiModel.tone
    val toneColors = AestheticDialogsTheme.colors.status.forTone(tone)
    val confirm = actionColors(DialogActionEmphasis.Primary, tone, uiModel.isConfirmEnabled)
    val cancel = actionColors(DialogActionEmphasis.Secondary, tone, !uiModel.isConfirming)

    ConfirmationPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        confirmLabel = uiModel.confirmLabel,
        confirmContainerColor = confirm.container,
        confirmContentColor = confirm.content,
        cancelLabel = uiModel.cancelLabel,
        cancelContentColor = cancel.content,
        onConfirm = onConfirm,
        onCancel = onCancel,
        onDismissRequest = onDismiss,
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        modifier = modifier,
        mark = GlyphMark.forTone(tone).takeIf { tone != DialogTone.Neutral },
        markColor = toneColors.accent,
        markContainerColor = toneColors.container,
        cancelBorder = cancel.border,
        confirmEnabled = uiModel.isConfirmEnabled,
        confirmLoading = uiModel.isConfirming,
    )
}
