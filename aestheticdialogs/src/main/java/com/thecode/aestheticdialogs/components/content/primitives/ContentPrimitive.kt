package com.thecode.aestheticdialogs.components.content.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.primitives.DialogActionsRowPrimitive
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.DialogMessagePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * Header, caller-owned content, action row.
 *
 * The content slot is the one thing this surface cannot take as a value: a
 * composable is not data, which is why the escape hatch exists at all.
 */
@Composable
internal fun ContentPrimitive(
    title: String,
    subtitle: String?,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    modifier: Modifier = Modifier,
    showCloseButton: Boolean = false,
    scrollableContent: Boolean = true,
    primaryLabel: String? = null,
    primaryContainerColor: Color = Color.Transparent,
    primaryContentColor: Color = Color.Transparent,
    primaryEnabled: Boolean = true,
    primaryLoading: Boolean = false,
    onPrimaryClick: () -> Unit = {},
    secondaryLabel: String? = null,
    secondaryContainerColor: Color = Color.Transparent,
    secondaryContentColor: Color = primaryContentColor,
    secondaryBorder: BorderStroke? = null,
    onSecondaryClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    val hasActions = primaryLabel != null || secondaryLabel != null

    DialogFramePrimitive(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        accessibilityPaneTitle = title,
        modifier = modifier,
        scrollableContent = scrollableContent,
        header = {
            DialogHeaderPrimitive(
                title = title,
                onCloseClick = if (showCloseButton) onDismissRequest else null,
            )
            subtitle?.let {
                DialogMessagePrimitive(
                    message = it,
                    color = AestheticDialogsTheme.colors.content.muted,
                )
            }
        },
        actions = if (hasActions) {
            {
                DialogActionsRowPrimitive(
                    primaryLabel = primaryLabel,
                    primaryContainerColor = primaryContainerColor,
                    primaryContentColor = primaryContentColor,
                    onPrimaryClick = onPrimaryClick,
                    primaryEnabled = primaryEnabled,
                    primaryLoading = primaryLoading,
                    secondaryLabel = secondaryLabel,
                    secondaryContainerColor = secondaryContainerColor,
                    secondaryContentColor = secondaryContentColor,
                    secondaryBorder = secondaryBorder,
                    secondaryEnabled = !primaryLoading,
                    onSecondaryClick = onSecondaryClick,
                )
            }
        } else {
            null
        },
    ) {
        Spacer(Modifier.height(AestheticSpacing.lg))

        content()
        // Without an action row the content would otherwise sit on the rounded
        // bottom edge of the surface.
        Spacer(Modifier.height(if (hasActions) AestheticSpacing.sm else AestheticSpacing.xxl))
    }
}
