package com.thecode.aestheticdialogs

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.components.sheet.AestheticSheetDialog
import com.thecode.aestheticdialogs.components.sheet.AestheticSheetHost
import com.thecode.aestheticdialogs.components.sheet.models.SheetDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.tokens.AestheticMotion
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The sheet, and the host that lets it leave.
 *
 * The host is the only place in the library where a modal outlives the caller's
 * state, so the test that matters is the one that pins how long it does so.
 */
@RunWith(AndroidJUnit4::class)
class SheetDialogTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val sheet = SheetDialogUiModel.Default(
        title = "Share this album",
        message = "Anyone with the link can see the 24 photos inside it.",
        primaryAction = DialogAction("Copy link"),
        secondaryAction = DialogAction("Cancel", DialogActionEmphasis.Text),
    )

    @Test
    fun `an action reaches its own callback`() {
        val calls = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticSheetDialog(
                    uiModel = sheet,
                    onDismiss = { calls += "dismiss" },
                    onPrimaryAction = { calls += "primary" },
                    onSecondaryAction = { calls += "secondary" },
                )
            }
        }

        composeRule.onNodeWithText("Copy link").performClick()

        assertThat(calls).containsExactly("primary")
    }

    @Test
    fun `the drag handle offers dismissal to users who cannot drag`() {
        val calls = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticSheetDialog(uiModel = sheet, onDismiss = { calls += "dismiss" })
            }
        }

        val handle = composeRule.onNodeWithContentDescription("Drag handle").fetchSemanticsNode()
        val dismiss = handle.config[SemanticsActions.CustomActions].single { it.label == "Dismiss" }
        composeRule.runOnUiThread { dismiss.action() }

        assertThat(calls).containsExactly("dismiss")
    }

    @Test
    fun `the host draws the sheet it is given`() {
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticSheetHost(sheet = sheet, onDismiss = {})
            }
        }

        composeRule.onNodeWithText("Share this album").assertIsDisplayed()
    }

    @Test
    fun `the host keeps the sheet composed until it has finished leaving`() {
        val current = mutableStateOf<SheetDialogUiModel?>(sheet)
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            AestheticDialogsTheme(motion = AestheticMotion(enabled = true)) {
                AestheticSheetHost(sheet = current.value, onDismiss = {})
            }
        }

        composeRule.mainClock.advanceTimeBy(SETTLE)
        composeRule.onNodeWithText("Share this album").assertExists()

        composeRule.runOnUiThread { current.value = null }
        composeRule.mainClock.advanceTimeBy(ONE_FRAME)
        composeRule.onNodeWithText("Share this album").assertExists()

        composeRule.mainClock.advanceTimeBy(SETTLE)
        composeRule.onNodeWithText("Share this album").assertDoesNotExist()
    }
}

private const val SETTLE = 1_000L
private const val ONE_FRAME = 16L
