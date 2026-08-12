package com.thecode.aestheticdialogs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.components.confirmation.AestheticConfirmationDialog
import com.thecode.aestheticdialogs.components.confirmation.models.ConfirmationDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Behaviour, not pixels: which signal a gesture produces, and what the dialog
 * refuses to do while an action is running.
 */
@RunWith(AndroidJUnit4::class)
class ConfirmationDialogTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val model = ConfirmationDialogUiModel.Default(
        title = "Leave without saving?",
        message = "Your draft has unsaved changes.",
        confirmLabel = "Leave",
        cancelLabel = "Keep editing",
    )

    @Test
    fun `renders its title, message and both actions`() {
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticConfirmationDialog(
                    uiModel = model,
                    onConfirm = {},
                    onCancel = {},
                    onDismiss = {},
                )
            }
        }

        composeRule.onNodeWithText("Leave without saving?").assertIsDisplayed()
        composeRule.onNodeWithText("Your draft has unsaved changes.").assertIsDisplayed()
        composeRule.onNodeWithText("Leave").assertIsDisplayed()
        composeRule.onNodeWithText("Keep editing").assertIsDisplayed()
    }

    @Test
    fun `confirm and cancel reach their own callbacks`() {
        val calls = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticConfirmationDialog(
                    uiModel = model,
                    onConfirm = { calls += "confirm" },
                    onCancel = { calls += "cancel" },
                    onDismiss = { calls += "dismiss" },
                )
            }
        }

        composeRule.onNodeWithText("Leave").performClick()
        composeRule.onNodeWithText("Keep editing").performClick()

        assertThat(calls).containsExactly("confirm", "cancel").inOrder()
    }

    @Test
    fun `a disabled confirm action cannot be pressed`() {
        val calls = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticConfirmationDialog(
                    uiModel = model.copy(isConfirmEnabled = false),
                    onConfirm = { calls += "confirm" },
                    onCancel = { calls += "cancel" },
                    onDismiss = { calls += "dismiss" },
                )
            }
        }

        composeRule.onNodeWithText("Leave").assertIsNotEnabled()
        assertThat(calls).isEmpty()
    }

    @Test
    fun `while confirming, the cancel action is locked out too`() {
        // A half-finished delete that can still be cancelled is the bug this
        // rule exists to prevent.
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticConfirmationDialog(
                    uiModel = model.copy(isConfirming = true),
                    onConfirm = {},
                    onCancel = {},
                    onDismiss = {},
                )
            }
        }

        composeRule.onNodeWithText("Keep editing").assertIsNotEnabled()
    }

    @Test
    fun `the destructive variant keeps the same two-action contract`() {
        val calls = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticConfirmationDialog(
                    uiModel = ConfirmationDialogUiModel.Destructive(
                        title = "Delete this album?",
                        confirmLabel = "Delete album",
                        cancelLabel = "Keep it",
                    ),
                    onConfirm = { calls += "confirm" },
                    onCancel = { calls += "cancel" },
                    onDismiss = { calls += "dismiss" },
                )
            }
        }

        composeRule.onNodeWithText("Delete album").assertIsEnabled().performClick()

        assertThat(calls).containsExactly("confirm")
    }
}
