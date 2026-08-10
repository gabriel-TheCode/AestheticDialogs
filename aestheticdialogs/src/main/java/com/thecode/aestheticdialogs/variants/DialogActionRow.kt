package com.thecode.aestheticdialogs.variants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.primitives.DialogActionsPrimitive
import com.thecode.aestheticdialogs.primitives.DialogButtonPrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens

/**
 * The action row shared by every dialog in the library.
 *
 * This is a Variant, not a Primitive: it takes semantic input (a
 * [DialogActionEmphasis], a [DialogTone]) and resolves it into the raw colours
 * the button primitive paints. Keeping the resolution here — in one place — is
 * what makes "confirm" look identical in a confirmation, an alert and a form.
 *
 * The row holds at most two actions on purpose. Three-button dialogs force a
 * stacking decision at every screen width and, more importantly, a dialog with
 * three choices is usually a menu wearing a dialog's clothes.
 */
@Composable
internal fun DialogActionRow(
    primary: DialogAction?,
    secondary: DialogAction?,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: DialogTone = DialogTone.Neutral,
) {
    if (primary == null && secondary == null) return

    // While one action runs the other must not be pressable: cancelling a
    // half-finished delete leaves the caller with no defined outcome.
    val busy = primary?.loading == true || secondary?.loading == true

    DialogActionsPrimitive(modifier = modifier) {
        secondary?.let { action ->
            ActionButton(
                action = action,
                tone = tone,
                busy = busy,
                onClick = onSecondaryClick,
            )
        }
        primary?.let { action ->
            ActionButton(
                action = action,
                tone = tone,
                busy = busy,
                onClick = onPrimaryClick,
            )
        }
    }
}

@Composable
private fun RowScope.ActionButton(
    action: DialogAction,
    tone: DialogTone,
    busy: Boolean,
    onClick: () -> Unit,
) {
    val enabled = action.enabled && (!busy || action.loading)
    val style = actionStyle(emphasis = action.emphasis, tone = tone, enabled = enabled)

    DialogButtonPrimitive(
        label = action.label,
        onClick = onClick,
        containerColor = style.container,
        contentColor = style.content,
        shape = AestheticDialogsTheme.shapes.button,
        modifier = Modifier.weight(1f),
        enabled = enabled,
        loading = action.loading,
        border = style.border,
    )
}

private class ActionStyle(
    val container: Color,
    val content: Color,
    val border: BorderStroke?,
)

/**
 * Resolves emphasis and tone into colours.
 *
 * The tone reaches the primary button on purpose: a success dialog's confirm
 * button is green and an error dialog's is red, which is the visual signature
 * AestheticDialogs has always had. Secondary and text actions stay neutral so
 * the row still has one obvious focal point.
 */
@Composable
private fun actionStyle(
    emphasis: DialogActionEmphasis,
    tone: DialogTone,
    enabled: Boolean,
): ActionStyle {
    val colors = AestheticDialogsTheme.colors

    if (!enabled) {
        return ActionStyle(
            container = when (emphasis) {
                DialogActionEmphasis.Primary, DialogActionEmphasis.Destructive ->
                    colors.action.disabledContainer

                DialogActionEmphasis.Secondary, DialogActionEmphasis.Text -> Color.Transparent
            },
            content = colors.action.disabledContent,
            border = when (emphasis) {
                DialogActionEmphasis.Secondary -> BorderStroke(
                    AestheticDimens.borderWidth,
                    colors.action.disabledContainer,
                )

                else -> null
            },
        )
    }

    return when (emphasis) {
        DialogActionEmphasis.Primary -> {
            val toneColors = colors.status.forTone(tone)
            if (tone == DialogTone.Neutral) {
                ActionStyle(colors.action.primary, colors.action.onPrimary, null)
            } else {
                ActionStyle(toneColors.accent, toneColors.onAccent, null)
            }
        }

        DialogActionEmphasis.Destructive -> ActionStyle(
            container = colors.status.error.accent,
            content = colors.status.error.onAccent,
            border = null,
        )

        DialogActionEmphasis.Secondary -> ActionStyle(
            container = Color.Transparent,
            content = colors.action.secondaryContent,
            border = BorderStroke(AestheticDimens.borderWidth, colors.action.secondaryBorder),
        )

        DialogActionEmphasis.Text -> ActionStyle(
            container = Color.Transparent,
            content = colors.action.secondaryContent,
            border = null,
        )
    }
}
