package com.thecode.aestheticdialogs.components.input.variants

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.thecode.aestheticdialogs.R
import com.thecode.aestheticdialogs.components.input.models.InputDialogSignal
import com.thecode.aestheticdialogs.components.input.models.InputDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.primitives.AestheticGlyph
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.DialogMessagePrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.TextFieldPrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import com.thecode.aestheticdialogs.variants.DialogActionRow

/** A plain text entry. */
@Composable
internal fun InputDialogText(
    uiModel: InputDialogUiModel.Text,
    onValueChange: (String) -> Unit,
    onSignal: (InputDialogSignal) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    InputDialogFrame(
        uiModel = uiModel,
        onValueChange = onValueChange,
        onSignal = onSignal,
        focusRequester = focusRequester,
        modifier = modifier,
        keyboardType = uiModel.keyboardType,
        singleLine = uiModel.singleLine,
        visualTransformation = VisualTransformation.None,
        trailingContent = null,
    )
}

/** A masked entry with a reveal toggle. */
@Composable
internal fun InputDialogPassword(
    uiModel: InputDialogUiModel.Password,
    onValueChange: (String) -> Unit,
    onSignal: (InputDialogSignal) -> Unit,
    focusRequester: FocusRequester,
    revealed: Boolean,
    onRevealToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val showLabel = stringResource(R.string.aesthetic_dialogs_show_password)
    val hideLabel = stringResource(R.string.aesthetic_dialogs_hide_password)
    val colors = AestheticDialogsTheme.colors

    InputDialogFrame(
        uiModel = uiModel,
        onValueChange = onValueChange,
        onSignal = onSignal,
        focusRequester = focusRequester,
        modifier = modifier,
        keyboardType = KeyboardType.Password,
        singleLine = true,
        visualTransformation = if (revealed) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingContent = {
            val toggleLabel = if (revealed) hideLabel else showLabel
            Box(
                modifier = Modifier
                    .size(AestheticDimens.minTouchTarget)
                    .clickable(role = Role.Button, onClick = onRevealToggle)
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
    )
}

@Composable
private fun InputDialogFrame(
    uiModel: InputDialogUiModel,
    onValueChange: (String) -> Unit,
    onSignal: (InputDialogSignal) -> Unit,
    keyboardType: KeyboardType,
    singleLine: Boolean,
    visualTransformation: VisualTransformation,
    trailingContent: @Composable (() -> Unit)?,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val canConfirm = uiModel.isConfirmEnabled && !uiModel.isConfirming

    DialogFramePrimitive(
        onDismissRequest = { onSignal(InputDialogSignal.Dismissed) },
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        accessibilityPaneTitle = uiModel.title,
        modifier = modifier,
        header = { DialogHeaderPrimitive(title = uiModel.title) },
        actions = {
            DialogActionRow(
                primary = DialogAction(
                    label = uiModel.confirmLabel,
                    emphasis = DialogActionEmphasis.Primary,
                    enabled = uiModel.isConfirmEnabled,
                    loading = uiModel.isConfirming,
                ),
                secondary = DialogAction(
                    label = uiModel.cancelLabel,
                    emphasis = DialogActionEmphasis.Secondary,
                ),
                onPrimaryClick = { onSignal(InputDialogSignal.Confirmed) },
                onSecondaryClick = { onSignal(InputDialogSignal.Cancelled) },
            )
        },
    ) {
        uiModel.message?.let { DialogMessagePrimitive(message = it) }

        Box(
            modifier = Modifier.padding(
                start = AestheticSpacing.xxl,
                end = AestheticSpacing.xxl,
                top = AestheticSpacing.lg,
            ),
        ) {
            TextFieldPrimitive(
                value = uiModel.value,
                onValueChange = onValueChange,
                modifier = Modifier.focusRequester(focusRequester),
                label = uiModel.label,
                placeholder = uiModel.placeholder,
                supportingText = uiModel.errorText ?: uiModel.supportingText,
                isError = uiModel.errorText != null,
                enabled = !uiModel.isConfirming,
                singleLine = singleLine,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = if (singleLine) ImeAction.Done else ImeAction.Default,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { if (canConfirm) onSignal(InputDialogSignal.Confirmed) },
                ),
                visualTransformation = visualTransformation,
                trailingContent = trailingContent,
            )
        }

        Spacer(Modifier.height(AestheticSpacing.sm))
    }
}
