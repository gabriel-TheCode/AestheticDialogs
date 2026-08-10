package com.thecode.aestheticdialogs.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticElevation
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * The banner every notification variant is built from.
 *
 * A banner is not a dialog: it does not open a window, does not take focus and
 * does not block input. AestheticDialogs 1.x rendered these as `AlertDialog`s
 * pinned to the top of the screen, which meant an informational toast stole
 * focus, swallowed the back button and dimmed the app behind it. Here they are
 * ordinary composables the host places in the layout.
 *
 * The whole banner is one live region so a screen reader announces the message
 * when it appears, without the user having to find it.
 */
@Composable
internal fun BannerPrimitive(
    title: String,
    message: String?,
    titleColor: Color,
    messageColor: Color,
    containerColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    containerBrush: Brush? = null,
    leadingAccentColor: Color? = null,
    topAccentBrush: Brush? = null,
    liveRegionMode: LiveRegionMode = LiveRegionMode.Polite,
    centered: Boolean = false,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = AestheticDimens.notificationMaxWidth)
            .semantics { liveRegion = liveRegionMode },
        shape = shape,
        color = containerColor,
        shadowElevation = AestheticElevation.banner,
    ) {
        Column(
            modifier = Modifier
                .clip(shape)
                .then(
                    if (containerBrush != null) Modifier.background(containerBrush) else Modifier,
                )
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        ) {
            topAccentBrush?.let { brush ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(AestheticDimens.accentBarHeight)
                        .background(brush),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = AestheticDimens.bannerMinHeight),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingAccentColor?.let { color ->
                    Box(
                        Modifier
                            .width(AestheticDimens.accentBarWidth)
                            .height(AestheticDimens.minTouchTarget)
                            .background(color),
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = AestheticSpacing.lg,
                            vertical = AestheticSpacing.md,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (centered) {
                        Arrangement.Center
                    } else {
                        Arrangement.Start
                    },
                ) {
                    leading?.let {
                        it()
                        Spacer(Modifier.width(AestheticSpacing.lg))
                    }

                    Column(
                        modifier = if (centered) Modifier else Modifier.weight(1f),
                        horizontalAlignment = if (centered) {
                            Alignment.CenterHorizontally
                        } else {
                            Alignment.Start
                        },
                    ) {
                        Text(
                            text = title,
                            style = AestheticDialogsTheme.typography.itemLabel,
                            color = titleColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = if (centered) TextAlign.Center else TextAlign.Start,
                        )
                        message?.let {
                            Text(
                                text = it,
                                style = AestheticDialogsTheme.typography.supporting,
                                color = messageColor,
                                maxLines = BANNER_MESSAGE_MAX_LINES,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = if (centered) TextAlign.Center else TextAlign.Start,
                            )
                        }
                    }
                }

                trailing?.invoke()
            }
        }
    }
}

private const val BANNER_MESSAGE_MAX_LINES = 2
