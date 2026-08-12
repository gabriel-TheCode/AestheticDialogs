package com.thecode.aestheticdialogs.components.progress.variants

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.progress.models.ProgressDialogUiModel
import com.thecode.aestheticdialogs.components.progress.primitives.ProgressPrimitive
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.utils.indicatorColor
import com.thecode.aestheticdialogs.variants.actionColors

/** Work with no predictable end: a spinning ring, the copy, an optional way out. */
@Composable
internal fun ProgressDialogDefault(
    uiModel: ProgressDialogUiModel.Default,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val cancel = actionColors(DialogActionEmphasis.Secondary, uiModel.tone)

    ProgressPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        progress = null,
        indicatorColor = colors.indicatorColor(uiModel.tone),
        trackColor = colors.border.subtle,
        titleColor = colors.content.primary,
        messageColor = colors.content.secondary,
        modifier = modifier,
        cancelLabel = uiModel.cancelLabel,
        cancelContentColor = cancel.content,
        cancelBorder = cancel.border,
        onCancel = onCancel,
    )
}
