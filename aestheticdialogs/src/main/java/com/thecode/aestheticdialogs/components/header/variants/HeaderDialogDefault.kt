package com.thecode.aestheticdialogs.components.header.variants

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.header.models.HeaderDialogUiModel
import com.thecode.aestheticdialogs.components.header.primitives.HeaderPrimitive
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.tokens.AestheticPalette
import com.thecode.aestheticdialogs.variants.actionColors
import com.thecode.aestheticdialogs.variants.toneGradient

/** The header layout: band, title, message, up to two actions. */
@Composable
internal fun HeaderDialogDefault(
    uiModel: HeaderDialogUiModel.Default,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    band: @Composable (BoxScope.() -> Unit)? = null,
) {
    val colors = AestheticDialogsTheme.colors
    val tone = uiModel.tone
    val primary = actionColors(uiModel.primaryAction.emphasis, tone, uiModel.primaryAction.enabled)
    val secondary = uiModel.secondaryAction?.let { actionColors(it.emphasis, tone, it.enabled) }

    HeaderPrimitive(
        title = uiModel.title,
        message = uiModel.message,
        primaryLabel = uiModel.primaryAction.label,
        primaryContainerColor = primary.container,
        primaryContentColor = primary.content,
        onPrimaryClick = onPrimaryAction,
        onDismissRequest = onDismiss,
        dismissOnBackPress = uiModel.dismissBehavior.dismissOnBackPress,
        dismissOnClickOutside = uiModel.dismissBehavior.dismissOnClickOutside,
        modifier = modifier,
        bandBrush = if (band == null) toneGradient(tone) else null,
        // The disc under the glyph is the scrim colour, dark in both shipped
        // schemes, so the glyph is light in both — `content.inverse` would flip
        // with the theme and disappear into it.
        bandContentColor = AestheticPalette.White,
        bandScrimColor = colors.surface.scrim,
        showCloseButton = uiModel.showCloseButton,
        secondaryLabel = uiModel.secondaryAction?.label,
        secondaryContainerColor = secondary?.container ?: primary.container,
        secondaryContentColor = secondary?.content ?: primary.content,
        secondaryBorder = secondary?.border,
        onSecondaryClick = onSecondaryAction,
        band = band,
    )
}
