package com.thecode.aestheticdialogs.components.input.variants

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.thecode.aestheticdialogs.R
import com.thecode.aestheticdialogs.components.input.models.InputDialogUiModel
import com.thecode.aestheticdialogs.components.input.primitives.InputPrimitive
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.primitives.AestheticGlyph
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.variants.actionColors

@Composable
internal fun InputDialogText(
    uiModel: InputDialogUiModel.Text,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val confirm = actionColors(DialogActionEmphasis.Primary, enabled = uiModel.isConfirmEnabled)
    val cancel = actionColors(DialogActionEmphasis.Secondary, enabled = !uiModel.isConfirming)

    InputPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        value = uiModel.value,
        onValueChange = onValueChange,
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
        focusRequester = focusRequester,
        modifier = modifier,
        label = uiModel.label,
        placeholder = uiModel.placeholder,
        supportingText = uiModel.errorText ?: uiModel.supportingText,
        isError = uiModel.errorText != null,
        enabled = !uiModel.isConfirming,
        singleLine = uiModel.singleLine,
        keyboardType = uiModel.keyboardType,
        cancelBorder = cancel.border,
        confirmEnabled = uiModel.isConfirmEnabled,
        confirmLoading = uiModel.isConfirming,
    )
}
