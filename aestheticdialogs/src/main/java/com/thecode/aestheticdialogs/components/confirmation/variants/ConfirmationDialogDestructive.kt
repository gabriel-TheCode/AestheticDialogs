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
internal fun ConfirmationDialogDestructive(
    uiModel: ConfirmationDialogUiModel.Destructive,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val toneColors = AestheticDialogsTheme.colors.status.error
    val confirm = actionColors(
        DialogActionEmphasis.Destructive,
        DialogTone.Error,
        uiModel.isConfirmEnabled,
    )
    val cancel = actionColors(
        DialogActionEmphasis.Secondary,
        DialogTone.Error,
        !uiModel.isConfirming,
    )

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
        mark = GlyphMark.Cross,
        markColor = toneColors.accent,
        markContainerColor = toneColors.container,
        cancelBorder = cancel.border,
        confirmEnabled = uiModel.isConfirmEnabled,
        confirmLoading = uiModel.isConfirming,
    )
}
