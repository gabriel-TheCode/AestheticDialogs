package com.thecode.aestheticdialogs.components.notification.variants

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import com.thecode.aestheticdialogs.components.notification.models.NotificationPresence
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.components.notification.primitives.BannerPrimitive
import com.thecode.aestheticdialogs.foundation.AestheticColors
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.tokens.AestheticElevation
import com.thecode.aestheticdialogs.tokens.AestheticOpacity
import com.thecode.aestheticdialogs.tokens.AestheticRadius
import com.thecode.aestheticdialogs.utils.liveRegionMode
import com.thecode.aestheticdialogs.utils.presenceColor
import com.thecode.aestheticdialogs.variants.toneGradient
import com.thecode.aestheticdialogs.variants.toneRimGradient

@Composable
internal fun NotificationGradient(
    uiModel: NotificationUiModel.Gradient,
    onClick: () -> Unit,
    onAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    leading: @Composable (() -> Unit)? = null,
    countdown: Float? = null,
) {
    val colors = AestheticDialogsTheme.colors
    val tone = colors.status.forTone(uiModel.tone)

    BannerPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        titleColor = tone.onAccent,
        messageColor = tone.onAccent,
        containerColor = Color.Transparent,
        shape = AestheticDialogsTheme.shapes.banner,
        onClick = onClick,
        onDismiss = onDismiss,
        modifier = modifier,
        containerBrush = toneGradient(uiModel.tone),
        mark = GlyphMark.forTone(uiModel.tone),
        markColor = tone.accent,
        markContainerColor = tone.onAccent,
        emoji = uiModel.emoji,
        timestamp = uiModel.timestamp,
        showCloseButton = uiModel.showCloseButton,
        affordanceTint = tone.onAccent,
        liveRegionMode = uiModel.tone.liveRegionMode(),
        leading = leading,
        presenceColor = colors.presenceColor(uiModel.presence),
        actionLabel = uiModel.action?.label,
        onActionClick = onAction,
        progress = uiModel.progress,
        countdown = countdown,
        progressTrackColor = tone.onAccent.copy(alpha = AestheticOpacity.track),
    )
}
