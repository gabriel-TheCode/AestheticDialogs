package com.thecode.aestheticdialogs.components.alert.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.thecode.aestheticdialogs.primitives.DialogActionsRowPrimitive
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.DialogMessagePrimitive
import com.thecode.aestheticdialogs.primitives.GlyphMark
import com.thecode.aestheticdialogs.primitives.StatusBadgePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * The alert surface: a mark or an icon, a title, a message, one or two actions.
 *
 * The mark is drawn as a tinted disc rather than a solid one. An alert is read,
 * not celebrated, so the colour states the tone without taking over the surface.
 */
@Composable
internal fun AlertPrimitive(
    title: String,
    message: String?,
    primaryLabel: String,
    primaryContainerColor: Color,
    primaryContentColor: Color,
    onPrimaryClick: () -> Unit,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    modifier: Modifier = Modifier,
    mark: GlyphMark? = null,
    markColor: Color = primaryContainerColor,
    markContainerColor: Color = Color.Transparent,
    icon: ImageVector? = null,
    iconTint: Color = markColor,
    showCloseButton: Boolean = false,
    secondaryLabel: String? = null,
    secondaryContainerColor: Color = Color.Transparent,
    secondaryContentColor: Color = primaryContentColor,
    secondaryBorder: BorderStroke? = null,
    onSecondaryClick: () -> Unit = {},
) {
    DialogFramePrimitive(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        accessibilityPaneTitle = title,
        modifier = modifier,
        header = {
            DialogHeaderPrimitive(
                title = title,
                badge = mark?.let {
                    {
                        StatusBadgePrimitive(
                            mark = it,
                            accentColor = markColor,
                            onAccentColor = markContainerColor,
                            containerColor = markContainerColor,
                            size = AestheticDimens.iconLg + AestheticSpacing.lg,
                            filled = false,
                        )
                    }
                },
                customIcon = icon,
                iconTint = iconTint,
                onCloseClick = if (showCloseButton) onDismissRequest else null,
            )
        },
        actions = {
            DialogActionsRowPrimitive(
                primaryLabel = primaryLabel,
                primaryContainerColor = primaryContainerColor,
                primaryContentColor = primaryContentColor,
                onPrimaryClick = onPrimaryClick,
                secondaryLabel = secondaryLabel,
                secondaryContainerColor = secondaryContainerColor,
                secondaryContentColor = secondaryContentColor,
                secondaryBorder = secondaryBorder,
                onSecondaryClick = onSecondaryClick,
            )
        },
    ) {
        message?.let { DialogMessagePrimitive(message = it) }
        Spacer(Modifier.height(AestheticSpacing.sm))
    }
}
