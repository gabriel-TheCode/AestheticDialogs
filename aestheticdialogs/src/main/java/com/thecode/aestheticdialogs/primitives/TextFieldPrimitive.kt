package com.thecode.aestheticdialogs.primitives

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme

/**
 * The text field used by the search and input dialogs.
 *
 * Built on Material 3's `OutlinedTextField` rather than on `BasicTextField`:
 * the platform component already gets IME actions, selection handles, autofill
 * hints, right-to-left layout and TalkBack editing right.
 *
 * Every colour is passed explicitly, because the library installs no
 * `MaterialTheme` for the field to inherit from.
 */
@Composable
internal fun TextFieldPrimitive(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else MULTILINE_MAX_LINES,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        isError = isError,
        singleLine = singleLine,
        maxLines = maxLines,
        shape = AestheticDialogsTheme.shapes.control,
        textStyle = AestheticDialogsTheme.typography.message,
        label = label?.let { { Text(text = it, style = AestheticDialogsTheme.typography.supporting) } },
        placeholder = placeholder?.let {
            { Text(text = it, style = AestheticDialogsTheme.typography.message) }
        },
        supportingText = supportingText?.let {
            { Text(text = it, style = AestheticDialogsTheme.typography.supporting) }
        },
        leadingIcon = leadingContent,
        trailingIcon = trailingContent,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        colors = aestheticTextFieldColors(),
    )
}

/** Maps the semantic colour roles onto every state Material exposes for a field. */
@Composable
private fun aestheticTextFieldColors(): TextFieldColors {
    val colors = AestheticDialogsTheme.colors
    val error = colors.status.error.accent
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = colors.content.primary,
        unfocusedTextColor = colors.content.primary,
        disabledTextColor = colors.content.disabled,
        errorTextColor = colors.content.primary,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        cursorColor = colors.action.primary,
        errorCursorColor = error,
        selectionColors = TextSelectionColors(
            handleColor = colors.action.primary,
            backgroundColor = colors.action.primary.copy(alpha = SELECTION_ALPHA),
        ),
        focusedBorderColor = colors.border.focus,
        unfocusedBorderColor = colors.border.default,
        disabledBorderColor = colors.border.subtle,
        errorBorderColor = error,
        focusedLeadingIconColor = colors.content.secondary,
        unfocusedLeadingIconColor = colors.content.muted,
        disabledLeadingIconColor = colors.content.disabled,
        errorLeadingIconColor = colors.content.secondary,
        focusedTrailingIconColor = colors.content.secondary,
        unfocusedTrailingIconColor = colors.content.muted,
        disabledTrailingIconColor = colors.content.disabled,
        errorTrailingIconColor = error,
        focusedLabelColor = colors.action.primary,
        unfocusedLabelColor = colors.content.secondary,
        disabledLabelColor = colors.content.disabled,
        errorLabelColor = error,
        focusedPlaceholderColor = colors.content.muted,
        unfocusedPlaceholderColor = colors.content.muted,
        disabledPlaceholderColor = colors.content.disabled,
        errorPlaceholderColor = colors.content.muted,
        focusedSupportingTextColor = colors.content.secondary,
        unfocusedSupportingTextColor = colors.content.secondary,
        disabledSupportingTextColor = colors.content.disabled,
        errorSupportingTextColor = error,
    )
}

private const val MULTILINE_MAX_LINES = 6
private const val SELECTION_ALPHA = 0.3f
