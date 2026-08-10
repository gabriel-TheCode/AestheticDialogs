package com.thecode.aestheticdialogs.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thecode.aestheticdialogs.components.content.models.ContentDialogSignal
import com.thecode.aestheticdialogs.components.content.models.ContentDialogUiModel
import com.thecode.aestheticdialogs.components.content.variants.ContentDialogDefault
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * A dialog whose middle is yours.
 *
 * Use it when the content is genuinely specific — a consent summary, a chart, a
 * short form — and reach for [com.thecode.aestheticdialogs.components.alert.AestheticAlertDialog]
 * or [com.thecode.aestheticdialogs.components.confirmation.AestheticConfirmationDialog]
 * when it is not. The header, the actions and every behaviour around them stay
 * with the design system.
 *
 * ```
 * AestheticContentDialog(
 *     uiModel = ContentDialogUiModel.Default(
 *         title = "Before you continue",
 *         subtitle = "Three things we will do with your data.",
 *         primaryAction = DialogAction("I agree"),
 *         secondaryAction = DialogAction("Not now", DialogActionEmphasis.Text),
 *     ),
 *     onSignal = { ... },
 * ) {
 *     ConsentSummary(uiState.consent)
 * }
 * ```
 *
 * @param uiModel the visual state.
 * @param onSignal receives what the user did.
 * @param modifier applied to the dialog surface.
 * @param content your content, laid out in a column between the header and the
 *   action row. Pad it yourself: the frame does not guess at your rhythm.
 */
@Composable
public fun AestheticContentDialog(
    uiModel: ContentDialogUiModel,
    onSignal: (ContentDialogSignal) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    when (uiModel) {
        is ContentDialogUiModel.Default ->
            ContentDialogDefault(uiModel, onSignal, modifier, content)
    }
}

@ThemePreviews
@Composable
private fun ContentDialogPreview() {
    AestheticPreviewSurface {
        AestheticContentDialog(
            uiModel = ContentDialogUiModel.Default(
                title = "Before you continue",
                subtitle = "Three things this app does with your photos.",
                primaryAction = DialogAction("I agree"),
                secondaryAction = DialogAction("Not now", DialogActionEmphasis.Text),
            ),
            onSignal = {},
        ) {
            Column(
                modifier = Modifier.padding(horizontal = AestheticSpacing.xxl),
                verticalArrangement = Arrangement.spacedBy(AestheticSpacing.md),
            ) {
                listOf(
                    "Albums are stored on your device only.",
                    "Backups are encrypted before they leave the phone.",
                    "You can delete everything from Settings at any time.",
                ).forEach { line ->
                    Text(
                        text = line,
                        style = AestheticDialogsTheme.typography.message,
                        color = AestheticDialogsTheme.colors.content.secondary,
                    )
                }
            }
        }
    }
}
