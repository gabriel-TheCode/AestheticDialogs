package com.thecode.aestheticdialogs.components.alert.variants

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.alert.models.AlertDialogUiModel
import com.thecode.aestheticdialogs.components.alert.primitives.AlertPrimitive
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.variants.actionColors

/** The alert layout: tone mark, title, message, one or two actions. */
@Composable
internal fun AlertDialogDefault(
    uiModel: AlertDialogUiModel.Default,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tone = uiModel.tone
    val toneColors = AestheticDialogsTheme.colors.status.forTone(tone)
    val primary = actionColors(uiModel.primaryAction.emphasis, tone, uiModel.primaryAction.enabled)
    val secondary = uiModel.secondaryAction?.let {
        actionColors(it.emphasis, tone, it.enabled)
    }

    AlertPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        primaryLabel = uiModel.primaryAction.label,
        primaryContainerColor = primary.container,
        primaryContentColor = primary.content,
        onPrimaryClick = onPrimaryAction,
        onDismissRequest = onDismiss,
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        modifier = modifier,
        mark = GlyphMark.forTone(tone).takeIf {
            uiModel.icon == null && tone != DialogTone.Neutral
        },
        markColor = toneColors.accent,
        markContainerColor = toneColors.container,
        icon = uiModel.icon,
        iconTint = toneColors.accent,
        showCloseButton = uiModel.showCloseButton,
        secondaryLabel = uiModel.secondaryAction?.label,
        secondaryContainerColor = secondary?.container ?: primary.container,
        secondaryContentColor = secondary?.content ?: primary.content,
        secondaryBorder = secondary?.border,
        onSecondaryClick = onSecondaryAction,
    )
}
