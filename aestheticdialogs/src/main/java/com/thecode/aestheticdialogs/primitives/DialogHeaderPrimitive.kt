package com.thecode.aestheticdialogs.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.thecode.aestheticdialogs.R
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * A circular badge carrying a status mark.
 *
 * [filled] picks between the two treatments the design system uses: a solid
 * accent disc for surfaces that need a focal point (the modal feedback dialogs),
 * and a tinted disc with an accent mark for surfaces that already carry colour
 * (banners, alert headers).
 */
@Composable
internal fun StatusBadgePrimitive(
    mark: GlyphMark,
    accentColor: Color,
    onAccentColor: Color,
    containerColor: Color,
    size: Dp,
    modifier: Modifier = Modifier,
    filled: Boolean = true,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = if (filled) accentColor else containerColor,
                shape = AestheticDialogsTheme.shapes.circle,
            ),
        contentAlignment = Alignment.Center,
    ) {
        AestheticGlyph(
            mark = mark,
            color = if (filled) onAccentColor else accentColor,
            size = size * GLYPH_SIZE_RATIO,
        )
    }
}

private const val GLYPH_SIZE_RATIO = 0.55f

/**
 * A borderless close affordance sized to the minimum touch target.
 *
 * The glyph stays small and the touch area grows around it, so the control
 * meets the 48dp target without looking like it.
 */
@Composable
internal fun CloseButtonPrimitive(
    onClick: () -> Unit,
    tint: Color,
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.aesthetic_dialogs_close),
) {
    val label = contentDescription
    Box(
        modifier = modifier
            .size(AestheticDimens.minTouchTarget)
            .clickable(onClick = onClick, role = Role.Button)
            // The glyph is drawn, not written, so the node has no text to
            // borrow a name from: it needs one of its own.
            .semantics { this.contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        AestheticGlyph(
            mark = GlyphMark.Close,
            color = tint,
            size = AestheticDimens.iconMd,
        )
    }
}

/**
 * The header of a modal dialog: an optional mark, a title, and an optional close
 * affordance.
 *
 * The title is marked as a heading so screen reader users can jump to it, and
 * is drawn in the primary content colour rather than the status accent — no
 * usable yellow reaches 4.5:1 on white.
 */
@Composable
internal fun DialogHeaderPrimitive(
    title: String,
    modifier: Modifier = Modifier,
    badge: @Composable (() -> Unit)? = null,
    customIcon: ImageVector? = null,
    iconTint: Color = AestheticDialogsTheme.colors.content.secondary,
    onCloseClick: (() -> Unit)? = null,
    closeContentDescription: String? = null,
    centered: Boolean = false,
) {
    val colors = AestheticDialogsTheme.colors
    val typography = AestheticDialogsTheme.typography
    val defaultCloseDescription = stringResource(R.string.aesthetic_dialogs_close)

    Column(
        modifier = modifier.padding(
            start = AestheticSpacing.xxl,
            end = if (onCloseClick != null) AestheticSpacing.sm else AestheticSpacing.xxl,
            top = if (onCloseClick != null) AestheticSpacing.sm else AestheticSpacing.xxl,
        ),
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start,
    ) {
        if (onCloseClick != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                CloseButtonPrimitive(
                    onClick = onCloseClick,
                    tint = colors.content.muted,
                    contentDescription = closeContentDescription ?: defaultCloseDescription,
                )
            }
        }

        when {
            badge != null -> {
                badge()
                Spacer(Modifier.height(AestheticSpacing.lg))
            }

            customIcon != null -> {
                Icon(
                    imageVector = customIcon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(AestheticDimens.iconLg),
                )
                Spacer(Modifier.height(AestheticSpacing.md))
            }
        }

        Text(
            text = title,
            style = typography.title,
            color = colors.content.primary,
            textAlign = if (centered) TextAlign.Center else TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() },
        )
    }
}

/**
 * The body copy of a dialog. Lives next to the header so the two always share
 * the same horizontal rhythm.
 */
@Composable
internal fun DialogMessagePrimitive(
    message: String,
    modifier: Modifier = Modifier,
    color: Color = AestheticDialogsTheme.colors.content.secondary,
    centered: Boolean = false,
) {
    Text(
        text = message,
        style = AestheticDialogsTheme.typography.message,
        color = color,
        textAlign = if (centered) TextAlign.Center else TextAlign.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = AestheticSpacing.xxl,
                end = AestheticSpacing.xxl,
                top = AestheticSpacing.sm,
            ),
    )
}
