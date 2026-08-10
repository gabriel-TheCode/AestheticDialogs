package com.thecode.aestheticdialogs.components.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.KeyboardType
import com.thecode.aestheticdialogs.components.input.models.InputDialogSignal
import com.thecode.aestheticdialogs.components.input.models.InputDialogUiModel
import com.thecode.aestheticdialogs.components.input.variants.InputDialogPassword
import com.thecode.aestheticdialogs.components.input.variants.InputDialogText
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews

/**
 * Asks for one value.
 *
 * Renaming an album, entering a code, confirming a password before a dangerous
 * action — the cases where opening a screen would be heavier than the task.
 *
 * The field takes focus when the dialog opens, the keyboard's done action
 * confirms, and the dialog moves above the keyboard on its own.
 *
 * ```
 * AestheticInputDialog(
 *     uiModel = InputDialogUiModel.Text(
 *         title = "Rename album",
 *         value = uiState.name,
 *         label = "Album name",
 *         errorText = uiState.nameError,
 *         confirmLabel = "Rename",
 *         cancelLabel = "Cancel",
 *         isConfirmEnabled = uiState.nameError == null && uiState.name.isNotBlank(),
 *     ),
 *     onValueChange = viewModel::onNameChanged,
 *     onSignal = { ... },
 * )
 * ```
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onValueChange called for every edit. The value lives in your state.
 * @param onSignal receives the confirm, cancel and dismiss decisions.
 * @param modifier applied to the dialog surface.
 */
@Composable
public fun AestheticInputDialog(
    uiModel: InputDialogUiModel,
    onValueChange: (String) -> Unit,
    onSignal: (InputDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    // The component owns the presentation state, so the variants stay stateless:
    // where focus goes when the dialog opens, and whether the value is revealed.
    val focusRequester = remember { FocusRequester() }
    var revealed by rememberSaveable { mutableStateOf(false) }

    // The frame wait is load-bearing: a FocusRequester throws if asked for focus
    // before its node is placed, and the enter transition makes that race real.
    LaunchedEffect(focusRequester) {
        withFrameNanos { }
        runCatching { focusRequester.requestFocus() }
    }

    when (uiModel) {
        is InputDialogUiModel.Text ->
            InputDialogText(uiModel, onValueChange, onSignal, focusRequester, modifier)

        is InputDialogUiModel.Password -> InputDialogPassword(
            uiModel = uiModel,
            onValueChange = onValueChange,
            onSignal = onSignal,
            focusRequester = focusRequester,
            revealed = revealed,
            onRevealToggle = { revealed = !revealed },
            modifier = modifier,
        )
    }
}

@ThemePreviews
@Composable
private fun InputDialogTextPreview() {
    AestheticPreviewSurface {
        AestheticInputDialog(
            uiModel = InputDialogUiModel.Text(
                title = "Rename album",
                value = "Lisbon, spring",
                label = "Album name",
                supportingText = "Visible to anyone you share the album with.",
                confirmLabel = "Rename",
                cancelLabel = "Cancel",
            ),
            onValueChange = {},
            onSignal = {},
        )
    }
}

@ThemePreviews
@Composable
private fun InputDialogErrorPreview() {
    AestheticPreviewSurface {
        AestheticInputDialog(
            uiModel = InputDialogUiModel.Text(
                title = "Add a recipient",
                value = "not-an-address",
                label = "Email",
                errorText = "That does not look like an email address.",
                keyboardType = KeyboardType.Email,
                confirmLabel = "Add",
                cancelLabel = "Cancel",
                isConfirmEnabled = false,
            ),
            onValueChange = {},
            onSignal = {},
        )
    }
}

@ThemePreviews
@Composable
private fun InputDialogPasswordPreview() {
    AestheticPreviewSurface {
        AestheticInputDialog(
            uiModel = InputDialogUiModel.Password(
                title = "Confirm your password",
                message = "This deletes every backup on this device.",
                value = "hunter2hunter2",
                label = "Password",
                confirmLabel = "Confirm",
                cancelLabel = "Cancel",
            ),
            onValueChange = {},
            onSignal = {},
        )
    }
}
