package com.thecode.aestheticdialogs.components.input.variants

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
internal fun InputDialogPassword(
    uiModel: InputDialogUiModel.Password,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    focusRequester: FocusRequester,
    revealed: Boolean,
    onRevealToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val showLabel = stringResource(R.string.aesthetic_dialogs_show_password)
    val hideLabel = stringResource(R.string.aesthetic_dialogs_hide_password)
    val toggleLabel = if (revealed) hideLabel else showLabel
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
        keyboardType = KeyboardType.Password,
        visualTransformation = if (revealed) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingContent = {
            Box(
                modifier = Modifier
                    .size(AestheticDimens.minTouchTarget)
                    .clip(AestheticDialogsTheme.shapes.circle)
                    .clickable(role = Role.Button, onClick = onRevealToggle)
                    // The glyph is drawn, not written, so the node needs a name.
                    .semantics { contentDescription = toggleLabel },
                contentAlignment = Alignment.Center,
            ) {
                AestheticGlyph(
                    mark = if (revealed) GlyphMark.Conceal else GlyphMark.Reveal,
                    color = colors.content.muted,
                    size = AestheticDimens.iconMd,
                )
            }
        },
        cancelBorder = cancel.border,
        confirmEnabled = uiModel.isConfirmEnabled,
        confirmLoading = uiModel.isConfirming,
    )
}
