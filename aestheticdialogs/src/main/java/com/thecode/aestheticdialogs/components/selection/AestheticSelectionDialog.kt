package com.thecode.aestheticdialogs.components.selection

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogUiModel
import com.thecode.aestheticdialogs.components.selection.models.SelectionItem
import com.thecode.aestheticdialogs.components.selection.variants.SelectionDialogMultiple
import com.thecode.aestheticdialogs.components.selection.variants.SelectionDialogSingle
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews
import com.thecode.aestheticdialogs.preview.WindowSizePreviews

/**
 * Lets the user choose from a list.
 *
 * The dialog is a renderer. The selected value, the search text and the filtered
 * list all live in your state, which means the same dialog handles a static list
 * of five languages and a remote search over ten thousand rows with no extra API:
 *
 * ```
 * AestheticSelectionDialog(
 *     uiModel = SelectionDialogUiModel.Single(
 *         title = "Sort photos by",
 *         items = uiState.sortOptions,
 *         selectedId = uiState.sortId,
 *         cancelLabel = "Cancel",
 *     ),
 *     onItemClick = { id -> viewModel.toggleTopic(id) },
 *     onCancel = { viewModel.dismissDialog() },
 *     onSearchQueryChange = { viewModel.search(it) },
 *     onConfirm = { viewModel.saveTopics() },
 * )
 * ```
 *
 * Long lists are rendered lazily and the action row stays pinned, so a picker
 * with a thousand rows behaves the same as one with three.
 *
 * @param uiModel the visual state; the subclass selects single or multiple choice.
 * @param onItemClick a row was tapped. The dialog never mutates the selection:
 *   the list you pass back is the list it draws.
 * @param onCancel the cancel button was pressed.
 * @param onSearchQueryChange the search field was edited. Filtering is yours.
 * @param onConfirm the confirm button was pressed, when the model carries one.
 * @param onDismiss the scrim was tapped or back was pressed. Defaults to
 *   [onCancel].
 * @param modifier applied to the dialog surface.
 */
@Composable
public fun AestheticSelectionDialog(
    uiModel: SelectionDialogUiModel,
    onItemClick: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    onSearchQueryChange: (String) -> Unit = {},
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = onCancel,
) {
    when (uiModel) {
        is SelectionDialogUiModel.Single -> SelectionDialogSingle(
            uiModel,
            onItemClick,
            onSearchQueryChange,
            onConfirm,
            onCancel,
            onDismiss,
            modifier,
        )

        is SelectionDialogUiModel.Multiple -> SelectionDialogMultiple(
            uiModel,
            onItemClick,
            onSearchQueryChange,
            onConfirm,
            onCancel,
            onDismiss,
            modifier,
        )
    }
}

private val previewItems = listOf(
    SelectionItem(id = "en", label = "English", supportingText = "United Kingdom"),
    SelectionItem(id = "fr", label = "Français", supportingText = "France"),
    SelectionItem(id = "pt", label = "Português", supportingText = "Brasil"),
    SelectionItem(id = "sw", label = "Kiswahili", supportingText = "Coming soon", enabled = false),
)

@ThemePreviews
@Composable
private fun SelectionDialogSinglePreview() {
    AestheticPreviewSurface {
        AestheticSelectionDialog(
            uiModel = SelectionDialogUiModel.Single(
                title = "App language",
                items = previewItems,
                selectedId = "fr",
                cancelLabel = "Cancel",
            ),
            onItemClick = {},
            onCancel = {},
        )
    }
}

@ThemePreviews
@Composable
private fun SelectionDialogMultiplePreview() {
    AestheticPreviewSurface {
        AestheticSelectionDialog(
            uiModel = SelectionDialogUiModel.Multiple(
                title = "Notify me about",
                items = previewItems,
                selectedIds = setOf("en", "pt"),
                confirmLabel = "Save",
                cancelLabel = "Cancel",
                searchQuery = "",
            ),
            onItemClick = {},
            onCancel = {},
        )
    }
}

/** The adaptive width buckets, seen at all three sizes. */
@WindowSizePreviews
@Composable
private fun SelectionDialogWindowSizePreview() {
    AestheticPreviewSurface {
        AestheticSelectionDialog(
            uiModel = SelectionDialogUiModel.Single(
                title = "App language",
                items = previewItems,
                selectedId = "fr",
                cancelLabel = "Cancel",
            ),
            onItemClick = {},
            onCancel = {},
        )
    }
}

@ThemePreviews
@Composable
private fun SelectionDialogEmptyPreview() {
    AestheticPreviewSurface {
        AestheticSelectionDialog(
            uiModel = SelectionDialogUiModel.Single(
                title = "App language",
                items = emptyList(),
                selectedId = null,
                cancelLabel = "Cancel",
                searchQuery = "klingon",
                emptyText = "No language matches “klingon”.",
            ),
            onItemClick = {},
            onCancel = {},
        )
    }
}
