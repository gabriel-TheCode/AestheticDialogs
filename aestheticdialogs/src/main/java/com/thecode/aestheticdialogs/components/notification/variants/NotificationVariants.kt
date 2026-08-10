package com.thecode.aestheticdialogs.components.notification.variants

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import com.thecode.aestheticdialogs.components.notification.models.NotificationSignal
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.primitives.BannerPrimitive
import com.thecode.aestheticdialogs.primitives.CloseButtonPrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.StatusBadgePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import com.thecode.aestheticdialogs.variants.toneGradient
import com.thecode.aestheticdialogs.variants.toneRimGradient

/** Card with a tone bar down the leading edge. */
@Composable
internal fun NotificationToaster(
    uiModel: NotificationUiModel.Toaster,
    onSignal: (NotificationSignal) -> Unit,
    modifier: Modifier = Modifier,
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
        modifier = modifier,
        leadingAccentColor = tone.accent,
        liveRegionMode = uiModel.tone.liveRegionMode(),
        leading = {
            StatusBadgePrimitive(
                mark = GlyphMark.forTone(uiModel.tone),
                accentColor = tone.accent,
                onAccentColor = tone.onAccent,
                containerColor = tone.container,
                size = AestheticDimens.statusGlyphCompact,
                filled = false,
            )
        },
        trailing = closeAffordance(uiModel.showCloseButton, colors.content.muted, onSignal),
        onClick = { onSignal(NotificationSignal.Clicked) },
    )
}

/** Tone-filled card with inverted copy. */
@Composable
internal fun NotificationRainbow(
    uiModel: NotificationUiModel.Rainbow,
    onSignal: (NotificationSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tone = AestheticDialogsTheme.colors.status.forTone(uiModel.tone)

    BannerPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        titleColor = tone.onAccent,
        messageColor = tone.onAccent,
        containerColor = tone.accent,
        shape = AestheticDialogsTheme.shapes.banner,
        modifier = modifier,
        liveRegionMode = uiModel.tone.liveRegionMode(),
        leading = {
            // The card already carries the tone, so the badge inverts.
            StatusBadgePrimitive(
                mark = GlyphMark.forTone(uiModel.tone),
                accentColor = tone.accent,
                onAccentColor = tone.onAccent,
                containerColor = tone.onAccent,
                size = AestheticDimens.statusGlyphCompact,
                filled = false,
            )
        },
        trailing = closeAffordance(uiModel.showCloseButton, tone.onAccent, onSignal),
        onClick = { onSignal(NotificationSignal.Clicked) },
    )
}

/** Centred card under a gradient strip. */
@Composable
internal fun NotificationConnectify(
    uiModel: NotificationUiModel.Connectify,
    onSignal: (NotificationSignal) -> Unit,
    modifier: Modifier = Modifier,
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
        modifier = modifier,
        topAccentBrush = toneRimGradient(uiModel.tone),
        liveRegionMode = uiModel.tone.liveRegionMode(),
        centered = true,
        trailing = closeAffordance(uiModel.showCloseButton, colors.content.muted, onSignal),
        onClick = { onSignal(NotificationSignal.Clicked) },
    )
}

/** Card with a large emoji instead of a drawn mark. */
@Composable
internal fun NotificationEmoji(
    uiModel: NotificationUiModel.Emoji,
    onSignal: (NotificationSignal) -> Unit,
    modifier: Modifier = Modifier,
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
        modifier = modifier,
        liveRegionMode = uiModel.tone.liveRegionMode(),
        leading = {
            Text(
                // The emoji is decoration next to text that already says the
                // same thing, so it stays out of the accessibility tree.
                text = uiModel.emoji ?: uiModel.tone.defaultEmoji(),
                style = AestheticDialogsTheme.typography.title,
            )
        },
        trailing = closeAffordance(uiModel.showCloseButton, colors.content.muted, onSignal),
        onClick = { onSignal(NotificationSignal.Clicked) },
    )
}

/** Gradient card with a trailing timestamp. */
@Composable
internal fun NotificationEmotion(
    uiModel: NotificationUiModel.Emotion,
    onSignal: (NotificationSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tone = AestheticDialogsTheme.colors.status.forTone(uiModel.tone)
    val timestamp = uiModel.timestamp

    BannerPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        titleColor = tone.onAccent,
        messageColor = tone.onAccent,
        containerColor = Color.Transparent,
        containerBrush = toneGradient(uiModel.tone),
        shape = AestheticDialogsTheme.shapes.banner,
        modifier = modifier,
        liveRegionMode = uiModel.tone.liveRegionMode(),
        leading = {
            StatusBadgePrimitive(
                mark = GlyphMark.forTone(uiModel.tone),
                accentColor = tone.onAccent,
                onAccentColor = tone.accent,
                containerColor = tone.onAccent,
                size = AestheticDimens.iconLg,
                filled = true,
            )
        },
        trailing = if (timestamp != null) {
            {
                Text(
                    text = timestamp,
                    style = AestheticDialogsTheme.typography.caption,
                    color = tone.onAccent,
                    modifier = Modifier.padding(end = AestheticSpacing.lg),
                )
            }
        } else {
            closeAffordance(uiModel.showCloseButton, tone.onAccent, onSignal)
        },
        onClick = { onSignal(NotificationSignal.Clicked) },
    )
}

private fun closeAffordance(
    show: Boolean,
    tint: Color,
    onSignal: (NotificationSignal) -> Unit,
): (@Composable () -> Unit)? = if (show) {
    {
        CloseButtonPrimitive(
            onClick = { onSignal(NotificationSignal.Dismissed) },
            tint = tint,
            modifier = Modifier.padding(end = AestheticSpacing.sm),
        )
    }
} else {
    null
}

/**
 * Errors interrupt; everything else waits for a pause in speech.
 *
 * This is the one accessibility decision the library makes on the caller's
 * behalf, because getting it wrong is invisible to a sighted developer.
 */
private fun DialogTone.liveRegionMode(): LiveRegionMode = when (this) {
    DialogTone.Error -> LiveRegionMode.Assertive
    else -> LiveRegionMode.Polite
}

private fun DialogTone.defaultEmoji(): String = when (this) {
    DialogTone.Success -> "👍"
    DialogTone.Error -> "🤷"
    DialogTone.Warning -> "⚠️"
    DialogTone.Info -> "💡"
    DialogTone.Neutral -> "💬"
}
