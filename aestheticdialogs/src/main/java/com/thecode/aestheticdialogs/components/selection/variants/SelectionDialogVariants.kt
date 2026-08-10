package com.thecode.aestheticdialogs.components.selection.variants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.thecode.aestheticdialogs.R
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogSignal
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogUiModel
import com.thecode.aestheticdialogs.components.selection.models.SelectionItem
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.primitives.AestheticGlyph
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.SelectionRowPrimitive
import com.thecode.aestheticdialogs.primitives.TextFieldPrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import com.thecode.aestheticdialogs.variants.DialogActionRow

/** Single choice: radio rows, optional commit button. */
@Composable
internal fun SelectionDialogSingle(
    uiModel: SelectionDialogUiModel.Single,
    onSignal: (SelectionDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    SelectionDialogFrame(
        uiModel = uiModel,
        multiSelect = false,
        isSelected = { it.id == uiModel.selectedId },
        confirmLabel = uiModel.confirmLabel,
        isConfirmEnabled = uiModel.isConfirmEnabled,
        onSignal = onSignal,
        modifier = modifier,
    )
}

/** Multiple choice: checkbox rows, always confirmed. */
@Composable
internal fun SelectionDialogMultiple(
    uiModel: SelectionDialogUiModel.Multiple,
    onSignal: (SelectionDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    SelectionDialogFrame(
        uiModel = uiModel,
        multiSelect = true,
        isSelected = { it.id in uiModel.selectedIds },
        confirmLabel = uiModel.confirmLabel,
        isConfirmEnabled = uiModel.isConfirmEnabled,
        onSignal = onSignal,
        modifier = modifier,
    )
}

@Composable
private fun SelectionDialogFrame(
    uiModel: SelectionDialogUiModel,
    multiSelect: Boolean,
    isSelected: (SelectionItem) -> Boolean,
    confirmLabel: String?,
    isConfirmEnabled: Boolean,
    onSignal: (SelectionDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val searchLabel = stringResource(R.string.aesthetic_dialogs_search)

    DialogFramePrimitive(
        onDismissRequest = { onSignal(SelectionDialogSignal.Dismissed) },
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        accessibilityPaneTitle = uiModel.title,
        modifier = modifier,
        // The list scrolls itself; the frame must not wrap it in a second
        // scrolling container.
        scrollableContent = false,
        header = {
            DialogHeaderPrimitive(title = uiModel.title)

            uiModel.searchQuery?.let { query ->
                Box(
                    modifier = Modifier.padding(
                        start = AestheticSpacing.xxl,
                        end = AestheticSpacing.xxl,
                        top = AestheticSpacing.lg,
                    ),
                ) {
                    TextFieldPrimitive(
                        value = query,
                        onValueChange = { onSignal(SelectionDialogSignal.SearchQueryChanged(it)) },
                        placeholder = searchLabel,
                        leadingContent = {
                            AestheticGlyph(
                                mark = GlyphMark.Search,
                                color = colors.content.muted,
                                size = AestheticDimens.iconMd,
                            )
                        },
                    )
                }
            }
        },
        actions = {
            DialogActionRow(
                primary = confirmLabel?.let {
                    DialogAction(
                        label = it,
                        emphasis = DialogActionEmphasis.Primary,
                        enabled = isConfirmEnabled,
                    )
                },
                secondary = DialogAction(
                    label = uiModel.cancelLabel,
                    emphasis = DialogActionEmphasis.Secondary,
                ),
                onPrimaryClick = { onSignal(SelectionDialogSignal.Confirmed) },
                onSecondaryClick = { onSignal(SelectionDialogSignal.Cancelled) },
            )
        },
    ) {
        if (uiModel.items.isEmpty()) {
            Text(
                text = uiModel.emptyText ?: stringResource(R.string.aesthetic_dialogs_no_results),
                style = AestheticDialogsTheme.typography.message,
                color = colors.content.muted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AestheticSpacing.xxl),
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(top = AestheticSpacing.md),
            ) {
                items(items = uiModel.items, key = { it.id }) { item ->
                    SelectionRowPrimitive(
                        label = item.label,
                        supportingText = item.supportingText,
                        selected = isSelected(item),
                        multiSelect = multiSelect,
                        enabled = item.enabled,
                        onClick = { onSignal(SelectionDialogSignal.ItemClicked(item.id)) },
                    )
                }
            }
        }
    }
}
