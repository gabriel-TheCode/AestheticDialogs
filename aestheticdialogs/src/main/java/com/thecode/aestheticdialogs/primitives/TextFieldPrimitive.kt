package com.thecode.aestheticdialogs.primitives

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme

/**
 * The text field used by the search and input dialogs.
 *
 * Built on Material 3's `OutlinedTextField` rather than on `BasicTextField`:
 * the platform component already gets IME actions, text selection handles,
 * autofill hints, right-to-left layout and TalkBack editing behaviour right, and
 * a design system that reimplements those loses more than it gains. What
 * AestheticDialogs contributes is the shape and the surrounding layout, and the
 * colours come from the theme bridge, so the field looks like the rest of the
 * dialog without a colour mapping of its own.
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
    )
}

private const val MULTILINE_MAX_LINES = 6
