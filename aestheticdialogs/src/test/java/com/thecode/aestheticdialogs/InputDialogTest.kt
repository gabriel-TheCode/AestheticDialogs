package com.thecode.aestheticdialogs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.components.input.AestheticInputDialog
import com.thecode.aestheticdialogs.components.input.models.InputDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** The input dialog's contract: the value is the caller's, the errors are too. */
@RunWith(AndroidJUnit4::class)
class InputDialogTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `edits are reported instead of being applied`() {
        val edits = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticInputDialog(
                    uiModel = InputDialogUiModel.Text(
                        title = "Rename album",
                        value = "Lisbon",
                        label = "Album name",
                        confirmLabel = "Rename",
                        cancelLabel = "Cancel",
                    ),
                    onValueChange = edits::add,
                    onConfirm = {},
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("Lisbon").performTextInput("!")

        assertThat(edits).isNotEmpty()
        // The field still shows the caller's value: nothing was applied locally.
        composeRule.onNodeWithText("Lisbon").assertIsDisplayed()
    }

    @Test
    fun `error text replaces the helper text and blocks confirmation`() {
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticInputDialog(
                    uiModel = InputDialogUiModel.Text(
                        title = "Add a recipient",
                        value = "not-an-address",
                        supportingText = "We will only use it for this invite.",
                        errorText = "That does not look like an email address.",
                        confirmLabel = "Add",
                        cancelLabel = "Cancel",
                        isConfirmEnabled = false,
                    ),
                    onValueChange = {},
                    onConfirm = {},
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("That does not look like an email address.")
            .assertIsDisplayed()
        composeRule.onNodeWithText("We will only use it for this invite.")
            .assertDoesNotExist()
        composeRule.onNodeWithText("Add").assertIsNotEnabled()
    }

    @Test
    fun `cancel emits Cancelled`() {
        val calls = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticInputDialog(
                    uiModel = InputDialogUiModel.Password(
                        title = "Confirm your password",
                        value = "",
                        confirmLabel = "Confirm",
                        cancelLabel = "Cancel",
                    ),
                    onValueChange = {},
                    onConfirm = { calls += "confirm" },
                    onCancel = { calls += "cancel" },
                )
            }
        }

        composeRule.onNodeWithText("Cancel").performClick()

        assertThat(calls).containsExactly("cancel")
    }
}
