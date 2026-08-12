package com.thecode.aestheticdialogs.components.header.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.primitives.CloseButtonPrimitive
import com.thecode.aestheticdialogs.primitives.DialogActionsRowPrimitive
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.DialogMessagePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * The header surface: a full-bleed band, then the title, the message and the
 * actions.
 *
 * The band is not clipped here — the dialog surface already clips to the theme's
 * shape, so it inherits the top corner radius without knowing what it is. The
 * close affordance sits on a scrim disc rather than being tinted to match: the
 * band may be a photograph the library has never seen, and a plain glyph is
 * legible on exactly half of those.
 */
@Composable
internal fun HeaderPrimitive(
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
    bandBrush: Brush? = null,
    bandContentColor: Color = Color.White,
    bandScrimColor: Color = Color.Transparent,
    showCloseButton: Boolean = true,
    secondaryLabel: String? = null,
    secondaryContainerColor: Color = Color.Transparent,
    secondaryContentColor: Color = primaryContentColor,
    secondaryBorder: BorderStroke? = null,
    onSecondaryClick: () -> Unit = {},
    band: @Composable (BoxScope.() -> Unit)? = null,
) {
    DialogFramePrimitive(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        accessibilityPaneTitle = title,
        modifier = modifier,
        header = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AestheticDimens.headerBandHeight)
                    .then(
                        if (bandBrush != null) Modifier.background(bandBrush) else Modifier,
                    ),
            ) {
                band?.invoke(this)

                if (showCloseButton) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(AestheticSpacing.sm)
                            .background(bandScrimColor, AestheticDialogsTheme.shapes.circle),
                    ) {
                        CloseButtonPrimitive(onClick = onDismissRequest, tint = bandContentColor)
                    }
                }
            }

            DialogHeaderPrimitive(title = title)
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
