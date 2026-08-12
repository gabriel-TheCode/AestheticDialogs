package com.thecode.aestheticdialogs.components.selection.variants

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogUiModel
import com.thecode.aestheticdialogs.components.selection.models.SelectionItem
import com.thecode.aestheticdialogs.components.selection.primitives.SelectionPrimitive
import com.thecode.aestheticdialogs.components.selection.primitives.SelectionRowData
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.variants.actionColors

internal fun SelectionItem.toRow(selected: Boolean) = SelectionRowData(
    id = id,
    label = label,
    supportingText = supportingText,
    selected = selected,
    enabled = enabled,
)

/** What the two variants share: everything except which rows are selected. */
@Composable
internal fun SelectionSurface(
    uiModel: SelectionDialogUiModel,
    rows: List<SelectionRowData>,
    multiSelect: Boolean,
    confirmLabel: String?,
    confirmEnabled: Boolean,
    onItemClick: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val confirm = actionColors(DialogActionEmphasis.Primary, enabled = confirmEnabled)
    val cancel = actionColors(DialogActionEmphasis.Secondary)

    SelectionPrimitive(
        title = uiModel.title,
        rows = rows,
        multiSelect = multiSelect,
        cancelLabel = uiModel.cancelLabel,
        cancelContentColor = cancel.content,
        onRowClick = onItemClick,
        onCancel = onCancel,
        onDismissRequest = onDismiss,
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        modifier = modifier,
        searchQuery = uiModel.searchQuery,
        onSearchQueryChange = onSearchQueryChange,
        emptyText = uiModel.emptyText,
        cancelBorder = cancel.border,
        confirmLabel = confirmLabel,
        confirmContainerColor = confirm.container,
        confirmContentColor = confirm.content,
        confirmEnabled = confirmEnabled,
        onConfirm = onConfirm,
    )
}
