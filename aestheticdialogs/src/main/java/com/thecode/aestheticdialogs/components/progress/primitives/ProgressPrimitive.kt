package com.thecode.aestheticdialogs.components.progress.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.thecode.aestheticdialogs.R
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.primitives.DialogActionsRowPrimitive
import com.thecode.aestheticdialogs.primitives.DialogFramePrimitive
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * The surface that says "wait": a ring where the other dialogs draw a badge, the
 * copy, and at most a way to give up.
 *
 * Both dismiss gestures are off and are not a parameter: the silhouette exists to
 * say wait, and a surface that disappears when the scrim is tapped says the
 * opposite. Cancelling is an action with a label, or it is not offered.
 *
 * A `null` [progress] spins; a value fills. The determinate form carries its own
 * range semantics from the platform, so screen readers announce the percentage
 * without the dialog repeating it; the indeterminate form has nothing to announce
 * and gets a name instead.
 */
@Composable
internal fun ProgressPrimitive(
    title: String,
    message: String?,
    progress: Float?,
    indicatorColor: Color,
    trackColor: Color,
    titleColor: Color,
    messageColor: Color,
    modifier: Modifier = Modifier,
    progressLabel: String? = null,
    progressLabelColor: Color = messageColor,
    cancelLabel: String? = null,
    cancelContentColor: Color = titleColor,
    cancelBorder: BorderStroke? = null,
    onCancel: () -> Unit = {},
) {
    val runningDescription = stringResource(R.string.aesthetic_dialogs_in_progress)

    DialogFramePrimitive(
        onDismissRequest = {},
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        accessibilityPaneTitle = title,
        modifier = modifier,
        actions = if (cancelLabel != null) {
            {
                DialogActionsRowPrimitive(
                    primaryLabel = null,
                    primaryContainerColor = Color.Transparent,
                    primaryContentColor = cancelContentColor,
                    onPrimaryClick = {},
                    secondaryLabel = cancelLabel,
                    secondaryContentColor = cancelContentColor,
                    secondaryBorder = cancelBorder,
                    onSecondaryClick = onCancel,
                )
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
                    bottom = if (cancelLabel != null) AestheticSpacing.lg else AestheticSpacing.xxxl,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (progress == null) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(AestheticDimens.progressIndicatorLarge)
                        .semantics { contentDescription = runningDescription },
                    color = indicatorColor,
                    strokeWidth = AestheticDimens.progressIndicatorLargeStroke,
                    trackColor = trackColor,
                )
            } else {
                CircularProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.size(AestheticDimens.progressIndicatorLarge),
                    color = indicatorColor,
                    strokeWidth = AestheticDimens.progressIndicatorLargeStroke,
                    trackColor = trackColor,
                )
            }

            Spacer(Modifier.height(AestheticSpacing.xl))

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

            progressLabel?.let {
                Spacer(Modifier.height(AestheticSpacing.sm))
                Text(
                    text = it,
                    style = AestheticDialogsTheme.typography.caption,
                    color = progressLabelColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
