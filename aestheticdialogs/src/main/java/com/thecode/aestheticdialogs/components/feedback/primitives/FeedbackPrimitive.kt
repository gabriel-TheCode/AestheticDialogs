package com.thecode.aestheticdialogs.components.feedback.primitives

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.primitives.DialogActionsPrimitive
import com.thecode.aestheticdialogs.primitives.DialogButtonPrimitive
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.StatusBadgePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * The modal "here is what happened" surface: a status mark, centred copy and one
 * action.
 *
 * It nests the frame, the badge and the button primitives itself and takes raw
 * parameters for all three, so a variant only has to initialise it. A primitive
 * containing other primitives is the point of the layer: this one can be used on
 * its own, and the assembly lives in one place instead of once per variant.
 */
@Composable
internal fun FeedbackPrimitive(
    title: String,
    message: String?,
    titleColor: Color,
    messageColor: Color,
    containerColor: Color,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    modifier: Modifier = Modifier,
    containerBrush: Brush? = null,
    /** Drawn above the title. `null` leaves the dialog without a mark. */
    mark: GlyphMark? = null,
    markColor: Color = titleColor,
    markContainerColor: Color = containerColor,
    actionLabel: String? = null,
    actionContainerColor: Color = Color.Transparent,
    actionContentColor: Color = titleColor,
    actionBorder: BorderStroke? = null,
    onActionClick: () -> Unit = {},
) {
    DialogFramePrimitive(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        accessibilityPaneTitle = title,
        modifier = modifier,
        containerColor = containerColor,
        containerBrush = containerBrush,
        actions = if (actionLabel != null) {
            {
                DialogActionsPrimitive {
                    DialogButtonPrimitive(
                        label = actionLabel,
                        onClick = onActionClick,
                        containerColor = actionContainerColor,
                        contentColor = actionContentColor,
                        shape = AestheticDialogsTheme.shapes.button,
                        modifier = Modifier.weight(1f),
                        border = actionBorder,
                    )
                }
            }
        } else {
            null
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AestheticSpacing.xxl,
                    end = AestheticSpacing.xxl,
                    top = AestheticSpacing.xxxl,
                    bottom = if (actionLabel != null) AestheticSpacing.lg else AestheticSpacing.xxxl,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            mark?.let {
                StatusBadgePrimitive(
                    mark = it,
                    accentColor = markColor,
                    onAccentColor = markContainerColor,
                    containerColor = markColor,
                    size = AestheticDimens.statusGlyph,
                    filled = true,
                )
                Spacer(Modifier.height(AestheticSpacing.xl))
            }

            Text(
                text = title,
                style = AestheticDialogsTheme.typography.title,
                color = titleColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            message?.let {
                Spacer(Modifier.height(AestheticSpacing.sm))
                Text(
                    text = it,
                    style = AestheticDialogsTheme.typography.message,
                    color = messageColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
