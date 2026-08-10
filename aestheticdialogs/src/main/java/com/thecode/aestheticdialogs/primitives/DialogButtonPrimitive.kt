package com.thecode.aestheticdialogs.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import com.thecode.aestheticdialogs.R
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * One button in a dialog action row.
 *
 * Raw parameters only: the primitive is told which colours to paint and never
 * asks what the button means. Emphasis, tone and destructiveness are resolved by
 * the variant layer in `DialogActionStyling`.
 */
@Composable
internal fun DialogButtonPrimitive(
    label: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    border: BorderStroke? = null,
) {
    val interactive = enabled && !loading
    val loadingDescription = stringResource(R.string.aesthetic_dialogs_loading)

    Surface(
        onClick = onClick,
        modifier = modifier
            .semantics {
                if (loading) stateDescription = loadingDescription
            }
            .defaultMinSize(
                minWidth = AestheticDimens.buttonMinWidth,
                minHeight = AestheticDimens.buttonHeight,
            ),
        enabled = interactive,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = AestheticSpacing.xl,
                vertical = AestheticSpacing.md,
            ),
            contentAlignment = Alignment.Center,
        ) {
            if (loading) {
                // The label stays in the tree, transparent, so the button keeps
                // its width and the row does not move under the user's finger.
                Text(
                    text = label,
                    style = AestheticDialogsTheme.typography.action,
                    color = Color.Transparent,
                    textAlign = TextAlign.Center,
                )
                CircularProgressIndicator(
                    modifier = Modifier.size(AestheticDimens.progressIndicatorSize),
                    color = contentColor,
                    strokeWidth = AestheticDimens.progressIndicatorStroke,
                )
            } else {
                Text(
                    text = label,
                    style = AestheticDialogsTheme.typography.action,
                    color = contentColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

/**
 * The action row.
 *
 * At most two actions, laid out with equal weight. Equal weight is what removes
 * the "do the labels fit side by side, or must they stack" problem entirely:
 * each button owns half the row and its label wraps rather than overflowing,
 * which is what large font sizes and long translations need.
 */
@Composable
internal fun DialogActionsPrimitive(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.padding(
            PaddingValues(
                start = AestheticSpacing.xxl,
                end = AestheticSpacing.xxl,
                top = AestheticSpacing.sm,
                bottom = AestheticSpacing.xxl,
            ),
        ),
        horizontalArrangement = Arrangement.spacedBy(AestheticSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}
