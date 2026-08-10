package com.thecode.aestheticdialogs.components.confirmation.variants

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.confirmation.models.ConfirmationDialogSignal
import com.thecode.aestheticdialogs.components.confirmation.models.ConfirmationDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.DialogMessagePrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.StatusBadgePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import com.thecode.aestheticdialogs.variants.DialogActionRow

/**
 * The neutral confirmation.
 *
 * A mark is drawn only when the question carries a tone; a plain "Discard
 * changes?" gets a title and nothing else, because an icon that means nothing is
 * noise a screen reader has to skip.
 */
@Composable
internal fun ConfirmationDialogDefault(
    uiModel: ConfirmationDialogUiModel.Default,
    onSignal: (ConfirmationDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConfirmationDialogFrame(
        uiModel = uiModel,
        tone = uiModel.tone,
        confirmEmphasis = DialogActionEmphasis.Primary,
        showBadge = uiModel.tone != DialogTone.Neutral,
        onSignal = onSignal,
        modifier = modifier,
    )
}

/**
 * The destructive confirmation: error mark, error confirm button, cancel
 * presented as the outlined way back.
 */
@Composable
internal fun ConfirmationDialogDestructive(
    uiModel: ConfirmationDialogUiModel.Destructive,
    onSignal: (ConfirmationDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConfirmationDialogFrame(
        uiModel = uiModel,
        tone = DialogTone.Error,
        confirmEmphasis = DialogActionEmphasis.Destructive,
        showBadge = true,
        onSignal = onSignal,
        modifier = modifier,
    )
}

/**
 * The layout both variants share.
 *
 * Private to the variant file rather than promoted to a primitive: it is a
 * composition of primitives specific to this component, and moving it down a
 * layer would let other components depend on a confirmation's shape.
 */
@Composable
private fun ConfirmationDialogFrame(
    uiModel: ConfirmationDialogUiModel,
    tone: DialogTone,
    confirmEmphasis: DialogActionEmphasis,
    showBadge: Boolean,
    onSignal: (ConfirmationDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val toneColors = colors.status.forTone(tone)

    DialogFramePrimitive(
        onDismissRequest = { onSignal(ConfirmationDialogSignal.Dismissed) },
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        accessibilityPaneTitle = uiModel.title,
        modifier = modifier,
        header = {
            DialogHeaderPrimitive(
                title = uiModel.title,
                badge = if (showBadge) {
                    {
                        StatusBadgePrimitive(
                            mark = GlyphMark.forTone(tone),
                            accentColor = toneColors.accent,
                            onAccentColor = toneColors.onAccent,
                            containerColor = toneColors.container,
                            size = AestheticDimens.iconLg + AestheticSpacing.md,
                            filled = false,
                        )
                    }
                } else {
                    null
                },
            )
        },
        actions = {
            DialogActionRow(
                primary = DialogAction(
                    label = uiModel.confirmLabel,
                    emphasis = confirmEmphasis,
                    enabled = uiModel.isConfirmEnabled,
                    loading = uiModel.isConfirming,
                ),
                secondary = DialogAction(
                    label = uiModel.cancelLabel,
                    emphasis = DialogActionEmphasis.Secondary,
                ),
                onPrimaryClick = { onSignal(ConfirmationDialogSignal.Confirmed) },
                onSecondaryClick = { onSignal(ConfirmationDialogSignal.Cancelled) },
                tone = tone,
            )
        },
    ) {
        uiModel.message?.let { message ->
            DialogMessagePrimitive(message = message)
        }
        Spacer(Modifier.height(AestheticSpacing.sm))
    }
}
