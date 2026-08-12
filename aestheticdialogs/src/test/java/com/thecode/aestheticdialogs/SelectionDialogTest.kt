package com.thecode.aestheticdialogs

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.components.selection.AestheticSelectionDialog
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogUiModel
import com.thecode.aestheticdialogs.components.selection.models.SelectionItem
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The selection dialog's contract: it reports taps and never mutates the
 * selection itself, and its rows carry the right accessibility semantics.
 */
@RunWith(AndroidJUnit4::class)
class SelectionDialogTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val items = listOf(
        SelectionItem("en", "English"),
        SelectionItem("fr", "Français"),
        SelectionItem("sw", "Kiswahili", supportingText = "Coming soon", enabled = false),
    )

    @Test
    fun `a tap reports the id and leaves the selection untouched`() {
        val clicked = mutableListOf<String>()
        val model = SelectionDialogUiModel.Single(
            title = "App language",
            items = items,
            selectedId = "fr",
            cancelLabel = "Cancel",
        )

        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticSelectionDialog(
                    uiModel = model,
                    onItemClick = clicked::add,
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("English").performClick()

        assertThat(clicked).containsExactly("en")
        // The dialog still shows the caller's selection, because the caller has
        // not changed it yet. This is the whole point of a stateless component.
        composeRule.onNodeWithText("Français").assertIsSelected()
    }

    @Test
    fun `single choice rows are radio buttons`() {
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticSelectionDialog(
                    uiModel = SelectionDialogUiModel.Single(
                        title = "App language",
                        items = items,
                        selectedId = "en",
                        cancelLabel = "Cancel",
                    ),
                    onItemClick = {},
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("English")
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected))
    }

    @Test
    fun `multiple choice rows are checkboxes`() {
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticSelectionDialog(
                    uiModel = SelectionDialogUiModel.Multiple(
                        title = "Notify me about",
                        items = items,
                        selectedIds = setOf("en"),
                        confirmLabel = "Save",
                        cancelLabel = "Cancel",
                    ),
                    onItemClick = {},
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("English").assert(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ToggleableState,
                ToggleableState.On,
            ),
        )
    }

    @Test
    fun `a disabled item is not clickable`() {
        val clicked = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticSelectionDialog(
                    uiModel = SelectionDialogUiModel.Single(
                        title = "App language",
                        items = items,
                        selectedId = null,
                        cancelLabel = "Cancel",
                    ),
                    onItemClick = clicked::add,
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("Kiswahili").assertIsNotEnabled()
        assertThat(clicked).isEmpty()
    }

    @Test
    fun `typing in the search field reports the query without filtering anything`() {
        val queries = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticSelectionDialog(
                    uiModel = SelectionDialogUiModel.Multiple(
                        title = "Notify me about",
                        items = items,
                        selectedIds = emptySet(),
                        confirmLabel = "Save",
                        cancelLabel = "Cancel",
                        searchQuery = "",
                    ),
                    onItemClick = {},
                    onCancel = {},
                    onSearchQueryChange = queries::add,
                )
            }
        }

        composeRule.onNode(hasSetTextAction()).performTextInput("fr")

        assertThat(queries).contains("fr")
        // Filtering belongs to the caller, so the list is unchanged.
        composeRule.onNodeWithText("English").assertIsDisplayed()
    }

    @Test
    fun `an empty list shows the caller's empty text`() {
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticSelectionDialog(
                    uiModel = SelectionDialogUiModel.Single(
                        title = "App language",
                        items = emptyList(),
                        selectedId = null,
                        cancelLabel = "Cancel",
                        emptyText = "No language matches that.",
                    ),
                    onItemClick = {},
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("No language matches that.").assertIsDisplayed()
    }
}
