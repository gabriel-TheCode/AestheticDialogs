package com.thecode.aestheticdialogs.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme

/**
 * The row of at most two buttons every dialog ends with.
 *
 * Raw colours only: which button is "confirm" and what its emphasis means is
 * resolved before it gets here. The row holds at most two actions on purpose —
 * three-button dialogs force a stacking decision at every width, and a dialog
 * with three choices is usually a menu wearing a dialog's clothes.
 */
@Composable
internal fun DialogActionsRowPrimitive(
    primaryLabel: String?,
    primaryContainerColor: Color,
    primaryContentColor: Color,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryBorder: BorderStroke? = null,
    primaryEnabled: Boolean = true,
    primaryLoading: Boolean = false,
    secondaryLabel: String? = null,
    secondaryContainerColor: Color = Color.Transparent,
    secondaryContentColor: Color = primaryContentColor,
    secondaryBorder: BorderStroke? = null,
    secondaryEnabled: Boolean = true,
    onSecondaryClick: () -> Unit = {},
) {
    if (primaryLabel == null && secondaryLabel == null) return

    DialogActionsPrimitive(modifier = modifier) {
        secondaryLabel?.let { label ->
            DialogButtonPrimitive(
                label = label,
                onClick = onSecondaryClick,
                containerColor = secondaryContainerColor,
                contentColor = secondaryContentColor,
                shape = AestheticDialogsTheme.shapes.button,
                modifier = Modifier.weight(1f),
                enabled = secondaryEnabled,
                border = secondaryBorder,
            )
        }
        primaryLabel?.let { label ->
            DialogButtonPrimitive(
                label = label,
                onClick = onPrimaryClick,
                containerColor = primaryContainerColor,
                contentColor = primaryContentColor,
                shape = AestheticDialogsTheme.shapes.button,
                modifier = Modifier.weight(1f),
                enabled = primaryEnabled,
                loading = primaryLoading,
                border = primaryBorder,
            )
        }
    }
}
