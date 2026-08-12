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
import com.thecode.aestheticdialogs.tokens.AestheticRadius
import com.thecode.aestheticdialogs.utils.liveRegionMode
import com.thecode.aestheticdialogs.utils.presenceColor
import com.thecode.aestheticdialogs.variants.toneGradient
import com.thecode.aestheticdialogs.variants.toneRimGradient

@Composable
internal fun NotificationDefault(
    uiModel: NotificationUiModel.Default,
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
        titleColor = tone.accent,
        messageColor = colors.content.secondary,
        containerColor = colors.surface.container,
        shape = AestheticDialogsTheme.shapes.banner,
        onClick = onClick,
        onDismiss = onDismiss,
        modifier = modifier,
        mark = GlyphMark.forTone(uiModel.tone),
        markColor = tone.accent,
        markContainerColor = tone.container,
        emoji = uiModel.emoji,
        timestamp = uiModel.timestamp,
        showCloseButton = uiModel.showCloseButton,
        affordanceTint = colors.content.muted,
        // The bar is how this card carries its tone. A card whose mark is a
        // character or a face is not making a status statement, so it gets none.
        leadingAccentColor = tone.accent.takeIf { uiModel.emoji == null && leading == null },
        liveRegionMode = uiModel.tone.liveRegionMode(),
        leading = leading,
        presenceColor = colors.presenceColor(uiModel.presence),
        actionLabel = uiModel.action?.label,
        onActionClick = onAction,
        progress = uiModel.progress,
        countdown = countdown,
        progressTrackColor = colors.border.subtle,
    )
}
