package com.thecode.aestheticdialogs.components.feedback.variants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogSignal
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.primitives.DialogActionsPrimitive
import com.thecode.aestheticdialogs.primitives.DialogButtonPrimitive
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.StatusBadgePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import com.thecode.aestheticdialogs.variants.DialogActionRow
import com.thecode.aestheticdialogs.variants.toneGradient

/**
 * The quiet feedback dialog: dialog surface, tone mark, centred copy, one
 * tone-coloured action.
 */
@Composable
internal fun FeedbackDialogFlat(
    uiModel: FeedbackDialogUiModel.Flat,
    onSignal: (FeedbackDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val toneColors = colors.status.forTone(uiModel.tone)
    val actionLabel = uiModel.actionLabel

    DialogFramePrimitive(
        onDismissRequest = { onSignal(FeedbackDialogSignal.Dismissed) },
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        accessibilityPaneTitle = uiModel.title,
        modifier = modifier,
        actions = if (actionLabel != null) {
            {
                DialogActionRow(
                    primary = DialogAction(
                        label = actionLabel,
                        emphasis = DialogActionEmphasis.Primary,
                    ),
                    secondary = null,
                    onPrimaryClick = { onSignal(FeedbackDialogSignal.ActionClicked) },
                    onSecondaryClick = {},
                    tone = uiModel.tone,
                )
            }
        } else {
            null
        },
    ) {
        FeedbackBody(
            title = uiModel.title,
            message = uiModel.message,
            titleColor = colors.content.primary,
            messageColor = colors.content.secondary,
            badge = if (uiModel.tone != DialogTone.Neutral) {
                {
                    StatusBadgePrimitive(
                        mark = GlyphMark.forTone(uiModel.tone),
                        accentColor = toneColors.accent,
                        onAccentColor = toneColors.onAccent,
                        containerColor = toneColors.container,
                        size = AestheticDimens.statusGlyph,
                        filled = true,
                    )
                }
            } else {
                null
            },
            hasAction = actionLabel != null,
        )
    }
}

/**
 * The loud feedback dialog: a gradient panel with inverted copy.
 *
 * The action button is assembled here from the button primitive rather than
 * through [DialogActionRow], because the row resolves colours from the theme and
 * this variant is deliberately painting outside it. That is the variant layer
 * doing its job — a styling decision that belongs to one visual form, kept out of
 * the shared resolver where it would become a special case for everyone.
 */
@Composable
internal fun FeedbackDialogFlash(
    uiModel: FeedbackDialogUiModel.Flash,
    onSignal: (FeedbackDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val toneColors = colors.status.forTone(uiModel.tone)
    val onGradient = toneColors.onAccent
    val actionLabel = uiModel.actionLabel

    DialogFramePrimitive(
        onDismissRequest = { onSignal(FeedbackDialogSignal.Dismissed) },
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        accessibilityPaneTitle = uiModel.title,
        modifier = modifier,
        containerColor = Color.Transparent,
        containerBrush = toneGradient(uiModel.tone),
        actions = if (actionLabel != null) {
            {
                DialogActionsPrimitive {
                    DialogButtonPrimitive(
                        label = actionLabel,
                        onClick = { onSignal(FeedbackDialogSignal.ActionClicked) },
                        containerColor = Color.Transparent,
                        contentColor = onGradient,
                        shape = AestheticDialogsTheme.shapes.button,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(AestheticDimens.borderWidth, onGradient),
                    )
                }
            }
        } else {
            null
        },
    ) {
        FeedbackBody(
            title = uiModel.title,
            message = uiModel.message,
            titleColor = onGradient,
            messageColor = onGradient,
            badge = if (uiModel.tone != DialogTone.Neutral) {
                {
                    StatusBadgePrimitive(
                        mark = GlyphMark.forTone(uiModel.tone),
                        accentColor = onGradient,
                        onAccentColor = toneColors.accent,
                        containerColor = onGradient,
                        size = AestheticDimens.statusGlyph,
                        filled = true,
                    )
                }
            } else {
                null
            },
            hasAction = actionLabel != null,
        )
    }
}

/** The centred badge / title / message stack both feedback variants share. */
@Composable
private fun FeedbackBody(
    title: String,
    message: String?,
    titleColor: Color,
    messageColor: Color,
    badge: (@Composable () -> Unit)?,
    hasAction: Boolean,
) {
    val typography = AestheticDialogsTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = AestheticSpacing.xxl,
                end = AestheticSpacing.xxl,
                top = AestheticSpacing.xxxl,
                bottom = if (hasAction) AestheticSpacing.lg else AestheticSpacing.xxxl,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // A neutral dialog carries no status, so it gets no status mark: the
        // glyph for `Neutral` is the info mark, and drawing it would say the one
        // thing the tone exists to not say.
        badge?.let {
            it()
            Spacer(Modifier.height(AestheticSpacing.xl))
        }

        Text(
            text = title,
            style = typography.title,
            color = titleColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        message?.let {
            Spacer(Modifier.height(AestheticSpacing.sm))
            Text(
                text = it,
                style = typography.message,
                color = messageColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
