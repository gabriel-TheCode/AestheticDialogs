package com.thecode.aestheticdialogs.components.confirmation.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.primitives.DialogActionsRowPrimitive
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.DialogMessagePrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.StatusBadgePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * The confirmation surface: an optional mark, a question, and two answers.
 *
 * It nests the frame, the header, the badge and the action row and takes raw
 * parameters for all of them, so a variant only has to initialise it. Which
 * button is destructive and which mark the tone maps to is decided before this
 * point.
 */
@Composable
internal fun ConfirmationPrimitive(
    title: String,
    message: String?,
    confirmLabel: String,
    confirmContainerColor: Color,
    confirmContentColor: Color,
    cancelLabel: String,
    cancelContentColor: Color,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    modifier: Modifier = Modifier,
    mark: GlyphMark? = null,
    markColor: Color = confirmContainerColor,
    markContainerColor: Color = Color.Transparent,
    cancelBorder: BorderStroke? = null,
    confirmEnabled: Boolean = true,
    confirmLoading: Boolean = false,
) {
    DialogFramePrimitive(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        accessibilityPaneTitle = title,
        modifier = modifier,
        header = {
            DialogHeaderPrimitive(
                title = title,
                badge = mark?.let {
                    {
                        StatusBadgePrimitive(
                            mark = it,
                            accentColor = markColor,
                            onAccentColor = markContainerColor,
                            containerColor = markContainerColor,
                            size = AestheticDimens.iconLg + AestheticSpacing.md,
                            filled = false,
                        )
                    }
                },
            )
        },
        actions = {
            DialogActionsRowPrimitive(
                primaryLabel = confirmLabel,
                primaryContainerColor = confirmContainerColor,
                primaryContentColor = confirmContentColor,
                onPrimaryClick = onConfirm,
                primaryEnabled = confirmEnabled,
                primaryLoading = confirmLoading,
                secondaryLabel = cancelLabel,
                secondaryContentColor = cancelContentColor,
                secondaryBorder = cancelBorder,
                // While the confirm action runs the way back must not be
                // pressable: cancelling a half-finished delete leaves the caller
                // with no defined outcome.
                secondaryEnabled = !confirmLoading,
                onSecondaryClick = onCancel,
            )
        },
    ) {
        message?.let { DialogMessagePrimitive(message = it) }
        Spacer(Modifier.height(AestheticSpacing.sm))
    }
}
