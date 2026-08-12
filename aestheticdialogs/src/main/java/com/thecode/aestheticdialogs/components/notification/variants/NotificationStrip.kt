package com.thecode.aestheticdialogs.components.notification.variants

import androidx.compose.foundation.layout.WindowInsets
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
import com.thecode.aestheticdialogs.tokens.AestheticRadius
import com.thecode.aestheticdialogs.utils.liveRegionMode
import com.thecode.aestheticdialogs.variants.toneGradient
import com.thecode.aestheticdialogs.variants.toneRimGradient

@Composable
internal fun NotificationStrip(
    uiModel: NotificationUiModel.Strip,
    onClick: () -> Unit,
    onAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
) {
    val colors = AestheticDialogsTheme.colors
    val tone = colors.status.forTone(uiModel.tone)

    BannerPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        titleColor = tone.onAccent,
        messageColor = tone.onAccent,
        containerColor = tone.accent,
        shape = RoundedCornerShape(AestheticRadius.none),
        onClick = onClick,
        onDismiss = onDismiss,
        modifier = modifier,
        mark = GlyphMark.forTone(uiModel.tone),
        markColor = tone.accent,
        markContainerColor = tone.onAccent,
        showCloseButton = uiModel.showCloseButton,
        affordanceTint = tone.onAccent,
        liveRegionMode = uiModel.tone.liveRegionMode(),
        actionLabel = uiModel.action?.label,
        onActionClick = onAction,
        maxWidth = null,
        shadowElevation = AestheticElevation.none,
        contentWindowInsets = contentWindowInsets,
    )
}
