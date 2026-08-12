package com.thecode.aestheticdialogs.variants

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.tokens.AestheticDimens

/** The three values one button is painted with. */
@Immutable
internal class ActionColors(
    val container: Color,
    val content: Color,
    val border: BorderStroke?,
)

/**
 * Resolves an action's emphasis and its dialog's tone into the colours a button
 * primitive paints.
 *
 * The tone reaches the primary button on purpose: a success dialog's confirm
 * button is green and an error dialog's is red, which is the visual signature
 * AestheticDialogs has always had. Secondary and text actions stay neutral so the
 * row still has one obvious focal point.
 *
 * @param emphasis how much weight the action carries.
 * @param tone the tone of the dialog it sits in.
 * @param enabled whether the button accepts input.
 * @return the fill, the label colour and the outline.
 */
@Composable
internal fun actionColors(
    emphasis: DialogActionEmphasis,
    tone: DialogTone = DialogTone.Neutral,
    enabled: Boolean = true,
): ActionColors {
    val colors = AestheticDialogsTheme.colors

    if (!enabled) {
        return ActionColors(
            container = when (emphasis) {
                DialogActionEmphasis.Primary, DialogActionEmphasis.Destructive ->
                    colors.action.disabledContainer

                DialogActionEmphasis.Secondary, DialogActionEmphasis.Text -> Color.Transparent
            },
            content = colors.action.disabledContent,
            border = if (emphasis == DialogActionEmphasis.Secondary) {
                BorderStroke(AestheticDimens.borderWidth, colors.action.disabledContainer)
            } else {
                null
            },
        )
    }

    return when (emphasis) {
        DialogActionEmphasis.Primary -> {
            val toneColors = colors.status.forTone(tone)
            if (tone == DialogTone.Neutral) {
                ActionColors(colors.action.primary, colors.action.onPrimary, null)
            } else {
                ActionColors(toneColors.accent, toneColors.onAccent, null)
            }
        }

        DialogActionEmphasis.Destructive -> ActionColors(
            container = colors.status.error.accent,
            content = colors.status.error.onAccent,
            border = null,
        )

        DialogActionEmphasis.Secondary -> ActionColors(
            container = Color.Transparent,
            content = colors.action.secondaryContent,
            border = BorderStroke(AestheticDimens.borderWidth, colors.action.secondaryBorder),
        )

        DialogActionEmphasis.Text -> ActionColors(
            container = Color.Transparent,
            content = colors.action.secondaryContent,
            border = null,
        )
    }
}
