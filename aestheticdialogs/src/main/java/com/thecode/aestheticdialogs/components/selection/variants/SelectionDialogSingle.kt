package com.thecode.aestheticdialogs.components.selection.variants

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogUiModel
import com.thecode.aestheticdialogs.components.selection.models.SelectionItem
import com.thecode.aestheticdialogs.components.selection.primitives.SelectionPrimitive
import com.thecode.aestheticdialogs.components.selection.primitives.SelectionRowData
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.variants.actionColors

@Composable
internal fun SelectionDialogSingle(
    uiModel: SelectionDialogUiModel.Single,
    onItemClick: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SelectionSurface(
        uiModel = uiModel,
        rows = uiModel.items.map { it.toRow(it.id == uiModel.selectedId) },
        multiSelect = false,
        confirmLabel = uiModel.confirmLabel,
        confirmEnabled = true,
        onItemClick = onItemClick,
        onSearchQueryChange = onSearchQueryChange,
        onConfirm = onConfirm,
        onCancel = onCancel,
        onDismiss = onDismiss,
        modifier = modifier,
    )
}
