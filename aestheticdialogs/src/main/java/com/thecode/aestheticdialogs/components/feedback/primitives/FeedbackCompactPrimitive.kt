package com.thecode.aestheticdialogs.components.feedback.primitives

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.primitives.DialogButtonPrimitive
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.StatusBadgePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * The single-line feedback surface: mark, one line of copy, one text action, all
 * on the same row.
 *
 * The action sits in the row rather than in an action row of its own, which is
 * the whole reason this shape exists — a full-width button under one line of text
 * is what `FeedbackPrimitive` already draws. At 72dp a filled button would *be*
 * the dialog, so the label carries the tone in its colour instead.
 */
@Composable
internal fun FeedbackCompactPrimitive(
    title: String,
    titleColor: Color,
    containerColor: Color,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    modifier: Modifier = Modifier,
    mark: GlyphMark? = null,
    markColor: Color = titleColor,
    markContainerColor: Color = containerColor,
    actionLabel: String? = null,
    actionContentColor: Color = titleColor,
    onActionClick: () -> Unit = {},
) {
    DialogFramePrimitive(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        accessibilityPaneTitle = title,
        modifier = modifier,
        containerColor = containerColor,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = AestheticDimens.compactDialogHeight)
                .padding(
                    start = AestheticSpacing.xl,
                    end = if (actionLabel != null) AestheticSpacing.sm else AestheticSpacing.xl,
                ),
            horizontalArrangement = Arrangement.spacedBy(AestheticSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            mark?.let {
                StatusBadgePrimitive(
                    mark = it,
                    accentColor = markColor,
                    onAccentColor = markContainerColor,
                    containerColor = markContainerColor,
                    size = AestheticDimens.statusGlyphCompact,
                    filled = false,
                )
            }

            Text(
                text = title,
                style = AestheticDialogsTheme.typography.itemLabel,
                color = titleColor,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = AestheticSpacing.md),
            )

            actionLabel?.let {
                DialogButtonPrimitive(
                    label = it,
                    onClick = onActionClick,
                    containerColor = Color.Transparent,
                    contentColor = actionContentColor,
                    shape = AestheticDialogsTheme.shapes.button,
                )
            }
        }
    }
}
