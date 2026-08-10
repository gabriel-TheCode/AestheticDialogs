package com.thecode.aestheticdialogs.components.alert.variants

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.alert.models.AlertDialogSignal
import com.thecode.aestheticdialogs.components.alert.models.AlertDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.DialogMessagePrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.StatusBadgePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import com.thecode.aestheticdialogs.variants.DialogActionRow

/**
 * The alert layout: tone mark, title, message, one or two actions.
 *
 * The mark is drawn as a tinted disc rather than a solid one. An alert is read,
 * not celebrated, so the colour states the tone without taking over the surface.
 */
@Composable
internal fun AlertDialogDefault(
    uiModel: AlertDialogUiModel.Default,
    onSignal: (AlertDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val toneColors = colors.status.forTone(uiModel.tone)
    val showBadge = uiModel.icon == null && uiModel.tone != DialogTone.Neutral

    DialogFramePrimitive(
        onDismissRequest = { onSignal(AlertDialogSignal.Dismissed) },
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
                            mark = GlyphMark.forTone(uiModel.tone),
                            accentColor = toneColors.accent,
                            onAccentColor = toneColors.onAccent,
                            containerColor = toneColors.container,
                            size = AestheticDimens.iconLg + AestheticSpacing.lg,
                            filled = false,
                        )
                    }
                } else {
                    null
                },
                customIcon = uiModel.icon,
                iconTint = toneColors.accent,
                onCloseClick = if (uiModel.showCloseButton) {
                    { onSignal(AlertDialogSignal.Dismissed) }
                } else {
                    null
                },
            )
        },
        actions = {
            DialogActionRow(
                primary = uiModel.primaryAction,
                secondary = uiModel.secondaryAction,
                onPrimaryClick = { onSignal(AlertDialogSignal.PrimaryActionClicked) },
                onSecondaryClick = { onSignal(AlertDialogSignal.SecondaryActionClicked) },
                tone = uiModel.tone,
            )
        },
    ) {
        uiModel.message?.let { message ->
            DialogMessagePrimitive(message = message)
        }
        Spacer(Modifier.height(AestheticSpacing.sm))
    }
}
