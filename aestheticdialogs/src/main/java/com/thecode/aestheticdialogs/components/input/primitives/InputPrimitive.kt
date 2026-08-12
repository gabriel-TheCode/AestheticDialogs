package com.thecode.aestheticdialogs.components.input.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.thecode.aestheticdialogs.primitives.DialogActionsRowPrimitive
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.DialogMessagePrimitive
import com.thecode.aestheticdialogs.primitives.TextFieldPrimitive
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * The surface that asks for one value: a title, a field, and two answers.
 *
 * The field's focus is driven from outside — the component owns the
 * [FocusRequester], because where focus lands on open is presentation state and
 * a primitive holds none.
 */
@Composable
internal fun InputPrimitive(
    title: String,
    message: String?,
    value: String,
    onValueChange: (String) -> Unit,
    confirmLabel: String,
    confirmContainerColor: Color,
    confirmContentColor: Color,
    cancelLabel: String,
    cancelContentColor: Color,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: @Composable (() -> Unit)? = null,
    cancelBorder: BorderStroke? = null,
    confirmEnabled: Boolean = true,
    confirmLoading: Boolean = false,
) {
    DialogFramePrimitive(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        accessibilityPaneTitle = title,
        modifier = modifier,
        header = { DialogHeaderPrimitive(title = title) },
        actions = {
            DialogActionsRowPrimitive(
                primaryLabel = confirmLabel,
                primaryContainerColor = confirmContainerColor,
                primaryContentColor = confirmContentColor,
                onPrimaryClick = onConfirm,
                primaryEnabled = confirmEnabled,
                primaryLoading = confirmLoading,
                secondaryLabel = cancelLabel,
                secondaryContentColor = cancelContentColor,
                secondaryBorder = cancelBorder,
                secondaryEnabled = !confirmLoading,
                onSecondaryClick = onCancel,
            )
        },
    ) {
        message?.let { DialogMessagePrimitive(message = it) }

        Box(
            modifier = Modifier.padding(
                start = AestheticSpacing.xxl,
                end = AestheticSpacing.xxl,
                top = AestheticSpacing.lg,
            ),
        ) {
            TextFieldPrimitive(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.focusRequester(focusRequester),
                label = label,
                placeholder = placeholder,
                supportingText = supportingText,
                isError = isError,
                enabled = enabled,
                singleLine = singleLine,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = if (singleLine) ImeAction.Done else ImeAction.Default,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { if (confirmEnabled && !confirmLoading) onConfirm() },
                ),
                visualTransformation = visualTransformation,
                trailingContent = trailingContent,
            )
        }

        Spacer(Modifier.height(AestheticSpacing.sm))
    }
}
