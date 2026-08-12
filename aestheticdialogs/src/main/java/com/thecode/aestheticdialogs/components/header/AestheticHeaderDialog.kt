package com.thecode.aestheticdialogs.components.header

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.header.models.HeaderDialogUiModel
import com.thecode.aestheticdialogs.components.header.variants.HeaderDialogDefault
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews

/**
 * A dialog with a picture on top: onboarding, a paywall, what changed in this
 * release.
 *
 * ```
 * AestheticHeaderDialog(
 *     uiModel = HeaderDialogUiModel.Default(
 *         title = "Albums, offline",
 *         message = "Everything you starred is now on the device.",
 *         primaryAction = DialogAction("Take the tour"),
 *         secondaryAction = DialogAction("Later", DialogActionEmphasis.Text),
 *     ),
 *     onPrimaryAction = { viewModel.startTour() },
 *     onDismiss = { viewModel.dismissWhatsNew() },
 *     band = { Image(painterResource(R.drawable.offline_albums), null, Modifier.fillMaxSize()) },
 * )
 * ```
 *
 * The band is a slot rather than a field on the UI model because an image is not
 * data: a `Painter` in a model would drag a rendering type into a class that is
 * meant to survive being built in a mapper and dropped into a preview. Leave it
 * out and the band is painted with the tone.
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onPrimaryAction the primary action was pressed.
 * @param onDismiss the close affordance was used, the scrim was tapped, or back
 *   was pressed.
 * @param modifier applied to the dialog surface.
 * @param onSecondaryAction the secondary action was pressed. Only reachable when
 *   the model carries one.
 * @param band drawn in the band across the top, clipped to the dialog's corners.
 *   Fill it: the box is as wide as the dialog and as tall as the band.
 */
@Composable
public fun AestheticHeaderDialog(
    uiModel: HeaderDialogUiModel,
    onPrimaryAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onSecondaryAction: () -> Unit = {},
    band: @Composable (BoxScope.() -> Unit)? = null,
) {
    when (uiModel) {
        is HeaderDialogUiModel.Default ->
            HeaderDialogDefault(uiModel, onPrimaryAction, onSecondaryAction, onDismiss, modifier, band)
    }
}

@ThemePreviews
@Composable
private fun HeaderDialogPreview() {
    AestheticPreviewSurface {
        AestheticHeaderDialog(
            uiModel = HeaderDialogUiModel.Default(
                title = "Albums, offline",
                message = "Everything you starred is on the device now, and stays there " +
                    "until you say otherwise.",
                primaryAction = DialogAction("Take the tour"),
                secondaryAction = DialogAction("Later", DialogActionEmphasis.Text),
            ),
            onPrimaryAction = {},
            onDismiss = {},
        )
    }
}

@ThemePreviews
@Composable
private fun HeaderDialogSuccessPreview() {
    AestheticPreviewSurface {
        AestheticHeaderDialog(
            uiModel = HeaderDialogUiModel.Default(
                title = "You are on Plus",
                message = "Unlimited albums, shared libraries and no adverts.",
                tone = DialogTone.Success,
                primaryAction = DialogAction("Start using it"),
                showCloseButton = false,
            ),
            onPrimaryAction = {},
            onDismiss = {},
        )
    }
}
