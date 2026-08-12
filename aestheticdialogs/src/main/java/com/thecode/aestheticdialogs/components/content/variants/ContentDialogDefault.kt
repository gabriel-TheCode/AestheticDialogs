package com.thecode.aestheticdialogs.components.content.variants

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.components.content.models.ContentDialogUiModel
import com.thecode.aestheticdialogs.components.content.primitives.ContentPrimitive
import com.thecode.aestheticdialogs.variants.actionColors

/** Header, caller-owned content, action row. */
@Composable
internal fun ContentDialogDefault(
    uiModel: ContentDialogUiModel.Default,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val primary = uiModel.primaryAction?.let { actionColors(it.emphasis, enabled = it.enabled) }
    val secondary = uiModel.secondaryAction?.let { actionColors(it.emphasis, enabled = it.enabled) }

    ContentPrimitive(
        title = uiModel.title,
        subtitle = uiModel.subtitle,
        onDismissRequest = onDismiss,
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        modifier = modifier,
        showCloseButton = uiModel.showCloseButton,
        scrollableContent = uiModel.scrollContent,
        primaryLabel = uiModel.primaryAction?.label,
        primaryContainerColor = primary?.container ?: Color.Transparent,
        primaryContentColor = primary?.content ?: Color.Transparent,
        primaryEnabled = uiModel.primaryAction?.enabled ?: true,
        primaryLoading = uiModel.primaryAction?.loading ?: false,
        onPrimaryClick = onPrimaryAction,
        secondaryLabel = uiModel.secondaryAction?.label,
        secondaryContainerColor = secondary?.container
            ?: Color.Transparent,
        secondaryContentColor = secondary?.content
            ?: Color.Transparent,
        secondaryBorder = secondary?.border,
        onSecondaryClick = onSecondaryAction,
        content = content,
    )
}
