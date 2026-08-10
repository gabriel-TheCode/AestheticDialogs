package com.thecode.aestheticdialogs.components.content.variants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.content.models.ContentDialogSignal
import com.thecode.aestheticdialogs.components.content.models.ContentDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.DialogMessagePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import com.thecode.aestheticdialogs.variants.DialogActionRow

/** Header, caller-owned content, action row. */
@Composable
internal fun ContentDialogDefault(
    uiModel: ContentDialogUiModel.Default,
    onSignal: (ContentDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val hasActions = uiModel.primaryAction != null || uiModel.secondaryAction != null

    DialogFramePrimitive(
        onDismissRequest = { onSignal(ContentDialogSignal.Dismissed) },
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        accessibilityPaneTitle = uiModel.title,
        modifier = modifier,
        scrollableContent = uiModel.scrollContent,
        header = {
            DialogHeaderPrimitive(
                title = uiModel.title,
                onCloseClick = if (uiModel.showCloseButton) {
                    { onSignal(ContentDialogSignal.Dismissed) }
                } else {
                    null
                },
            )
            uiModel.subtitle?.let { subtitle ->
                DialogMessagePrimitive(
                    message = subtitle,
                    color = AestheticDialogsTheme.colors.content.muted,
                )
            }
        },
        actions = if (hasActions) {
            {
                DialogActionRow(
                    primary = uiModel.primaryAction,
                    secondary = uiModel.secondaryAction,
                    onPrimaryClick = { onSignal(ContentDialogSignal.PrimaryActionClicked) },
                    onSecondaryClick = { onSignal(ContentDialogSignal.SecondaryActionClicked) },
                )
            }
        } else {
            null
        },
    ) {
        Spacer(Modifier.height(AestheticSpacing.lg))

        content()
        // Without an action row the content would otherwise sit on the rounded
        // bottom edge of the surface.
        Spacer(Modifier.height(if (hasActions) AestheticSpacing.sm else AestheticSpacing.xxl))
    }
}
