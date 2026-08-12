package com.thecode.aestheticdialogs.components.selection.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.thecode.aestheticdialogs.R
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.primitives.AestheticGlyph
import com.thecode.aestheticdialogs.primitives.DialogActionsRowPrimitive
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.SelectionRowPrimitive
import com.thecode.aestheticdialogs.primitives.TextFieldPrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/** One row of a selection list, as the primitive needs it: text and two booleans. */
internal class SelectionRowData(
    val id: String,
    val label: String,
    val supportingText: String?,
    val selected: Boolean,
    val enabled: Boolean,
)

/**
 * The list surface: a title, an optional search field, the rows, and the way out.
 *
 * The rows arrive as plain values rather than as the caller's model, so the
 * primitive never learns what a `SelectionItem` is. Which of them is selected was
 * decided before this point — this one only draws the answer.
 */
@Composable
internal fun SelectionPrimitive(
    title: String,
    rows: List<SelectionRowData>,
    multiSelect: Boolean,
    cancelLabel: String,
    cancelContentColor: Color,
    onRowClick: (String) -> Unit,
    onCancel: () -> Unit,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    modifier: Modifier = Modifier,
    searchQuery: String? = null,
    onSearchQueryChange: (String) -> Unit = {},
    emptyText: String? = null,
    cancelBorder: BorderStroke? = null,
    confirmLabel: String? = null,
    confirmContainerColor: Color = Color.Transparent,
    confirmContentColor: Color = Color.Transparent,
    confirmEnabled: Boolean = true,
    onConfirm: () -> Unit = {},
) {
    val colors = AestheticDialogsTheme.colors
    val searchLabel = stringResource(R.string.aesthetic_dialogs_search)

    DialogFramePrimitive(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        accessibilityPaneTitle = title,
        modifier = modifier,
        // The list scrolls itself; the frame must not wrap it in a second
        // scrolling container.
        scrollableContent = false,
        header = {
            DialogHeaderPrimitive(title = title)

            searchQuery?.let { query ->
                Box(
                    modifier = Modifier.padding(
                        start = AestheticSpacing.xxl,
                        end = AestheticSpacing.xxl,
                        top = AestheticSpacing.lg,
                    ),
                ) {
                    TextFieldPrimitive(
                        value = query,
                        onValueChange = onSearchQueryChange,
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
            DialogActionsRowPrimitive(
                primaryLabel = confirmLabel,
                primaryContainerColor = confirmContainerColor,
                primaryContentColor = confirmContentColor,
                onPrimaryClick = onConfirm,
                primaryEnabled = confirmEnabled,
                secondaryLabel = cancelLabel,
                secondaryContentColor = cancelContentColor,
                secondaryBorder = cancelBorder,
                onSecondaryClick = onCancel,
            )
        },
    ) {
        if (rows.isEmpty()) {
            Text(
                text = emptyText ?: stringResource(R.string.aesthetic_dialogs_no_results),
                style = AestheticDialogsTheme.typography.message,
                color = colors.content.muted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AestheticSpacing.xxl),
            )
        } else {
            LazyColumn(modifier = Modifier.padding(top = AestheticSpacing.md)) {
                items(items = rows, key = { it.id }) { row ->
                    SelectionRowPrimitive(
                        label = row.label,
                        supportingText = row.supportingText,
                        selected = row.selected,
                        multiSelect = multiSelect,
                        enabled = row.enabled,
                        onClick = { onRowClick(row.id) },
                    )
                }
            }
        }
    }
}
