package com.thecode.aestheticdialogs.components.sheet.variants

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.components.sheet.models.SheetDialogUiModel
import com.thecode.aestheticdialogs.components.sheet.primitives.SheetPrimitive
import com.thecode.aestheticdialogs.variants.actionColors

/** The sheet layout: handle, title, message, your content, up to two actions. */
@Composable
internal fun SheetDialogDefault(
    uiModel: SheetDialogUiModel.Default,
    visibleState: MutableTransitionState<Boolean>,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    surfaceDragModifier: Modifier = Modifier,
    handleDragModifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val primary = uiModel.primaryAction?.let { actionColors(it.emphasis, enabled = it.enabled) }
    val secondary = uiModel.secondaryAction?.let { actionColors(it.emphasis, enabled = it.enabled) }

    SheetPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        visibleState = visibleState,
        onDismissRequest = onDismiss,
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        modifier = modifier,
        surfaceDragModifier = surfaceDragModifier,
        handleDragModifier = handleDragModifier,
        showDragHandle = uiModel.showDragHandle,
        showCloseButton = uiModel.showCloseButton,
        primaryLabel = uiModel.primaryAction?.label,
        primaryContainerColor = primary?.container ?: Color.Transparent,
        primaryContentColor = primary?.content ?: Color.Transparent,
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
