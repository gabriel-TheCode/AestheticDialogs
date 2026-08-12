package com.thecode.aestheticdialogs

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertRangeInfoEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.components.progress.AestheticProgressDialog
import com.thecode.aestheticdialogs.components.progress.models.ProgressDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** The dialog that says "wait", and refuses to be waved away. */
@RunWith(AndroidJUnit4::class)
class ProgressDialogTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val progressBar = SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo)

    @Test
    fun `the cancel action reaches the caller, and leaves the dialog on screen`() {
        val calls = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticProgressDialog(
                    uiModel = ProgressDialogUiModel.Default(
                        title = "Signing you in",
                        cancelLabel = "Cancel",
                    ),
                    onCancel = { calls += "cancel" },
                )
            }
        }

        composeRule.onNodeWithText("Cancel").performClick()

        assertThat(calls).containsExactly("cancel")
        // Stopping the work is the caller's job, so the dialog stays until they
        // take it away.
        composeRule.onNodeWithText("Signing you in").assertIsDisplayed()
    }

    @Test
    fun `a determinate dialog announces how far along it is`() {
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticProgressDialog(
                    uiModel = ProgressDialogUiModel.Determinate(
                        title = "Uploading",
                        progress = 0.5f,
                        progressLabel = "12 of 24",
                    ),
                )
            }
        }

        composeRule.onNodeWithText("12 of 24").assertIsDisplayed()
        composeRule.onNode(progressBar).assertRangeInfoEquals(
            ProgressBarRangeInfo(current = 0.5f, range = 0f..1f),
        )
    }

    @Test
    fun `an out-of-range value is clamped rather than thrown`() {
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticProgressDialog(
                    uiModel = ProgressDialogUiModel.Determinate(title = "Uploading", progress = 1.4f),
                )
            }
        }

        composeRule.onNode(progressBar).assertRangeInfoEquals(
            ProgressBarRangeInfo(current = 1f, range = 0f..1f),
        )
    }
}
