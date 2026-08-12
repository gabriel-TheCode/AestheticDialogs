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

/** Work with a known end: the same layout with the ring filled and a count under it. */
@Composable
internal fun ProgressDialogDeterminate(
    uiModel: ProgressDialogUiModel.Determinate,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val cancel = actionColors(DialogActionEmphasis.Secondary, uiModel.tone)

    ProgressPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        progress = uiModel.progress,
        indicatorColor = colors.indicatorColor(uiModel.tone),
        trackColor = colors.border.subtle,
        titleColor = colors.content.primary,
        messageColor = colors.content.secondary,
        modifier = modifier,
        progressLabel = uiModel.progressLabel,
        progressLabelColor = colors.content.muted,
        cancelLabel = uiModel.cancelLabel,
        cancelContentColor = cancel.content,
        cancelBorder = cancel.border,
        onCancel = onCancel,
    )
}
