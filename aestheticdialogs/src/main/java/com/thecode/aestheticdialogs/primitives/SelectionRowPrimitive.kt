package com.thecode.aestheticdialogs.primitives

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * One row of a selection list.
 *
 * The whole row carries the selection semantics — `selectable` for single
 * choice, `toggleable` for multiple — and the control itself is passed
 * `onClick = null`. That is what makes TalkBack announce "Portuguese, radio
 * button, selected" once instead of announcing an unlabelled control next to an
 * unrelated piece of text, and it is what gives the user a full-width target
 * instead of a 20dp one.
 */
@Composable
internal fun SelectionRowPrimitive(
    label: String,
    selected: Boolean,
    multiSelect: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    enabled: Boolean = true,
) {
    val colors = AestheticDialogsTheme.colors
    val typography = AestheticDialogsTheme.typography

    val selectionModifier = if (multiSelect) {
        Modifier.toggleable(
            value = selected,
            enabled = enabled,
            role = Role.Checkbox,
            onValueChange = { onClick() },
        )
    } else {
        Modifier.selectable(
            selected = selected,
            enabled = enabled,
            role = Role.RadioButton,
            onClick = onClick,
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = AestheticDimens.minTouchTarget)
            .then(selectionModifier)
            .padding(horizontal = AestheticSpacing.xxl, vertical = AestheticSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (multiSelect) {
            Checkbox(
                checked = selected,
                onCheckedChange = null,
                enabled = enabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = colors.action.primary,
                    uncheckedColor = colors.border.default,
                    checkmarkColor = colors.action.onPrimary,
                    disabledCheckedColor = colors.action.disabledContainer,
                    disabledUncheckedColor = colors.border.subtle,
                    disabledIndeterminateColor = colors.action.disabledContainer,
                ),
            )
        } else {
            RadioButton(
                selected = selected,
                onClick = null,
                enabled = enabled,
                colors = RadioButtonDefaults.colors(
                    selectedColor = colors.action.primary,
                    unselectedColor = colors.border.default,
                    disabledSelectedColor = colors.action.disabledContainer,
                    disabledUnselectedColor = colors.border.subtle,
                ),
            )
        }

        Spacer(Modifier.width(AestheticSpacing.lg))

        Column {
            Text(
                text = label,
                style = typography.itemLabel,
                color = if (enabled) colors.content.primary else colors.content.disabled,
            )
            supportingText?.let {
                Text(
                    text = it,
                    style = typography.supporting,
                    color = if (enabled) colors.content.muted else colors.content.disabled,
                )
            }
        }
    }
}
