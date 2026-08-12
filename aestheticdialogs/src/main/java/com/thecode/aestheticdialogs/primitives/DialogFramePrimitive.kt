package com.thecode.aestheticdialogs.primitives

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticElevation

/**
 * The single modal frame every AestheticDialogs dialog is built on.
 *
 * It owns the parts that must not be reimplemented per dialog:
 *
 * - the platform window and its dismissal contract;
 * - the scrim opacity, taken from the theme;
 * - the adaptive width, chosen from the space the window actually offers;
 * - the layout contract "header and actions stay, content scrolls";
 * - the enter transition and its reduced-motion behaviour;
 * - the accessibility pane, so screen readers announce the dialog and trap
 *   traversal inside it.
 *
 * Variants supply the three slots and nothing else, which is what stops seven
 * dialogs from growing seven subtly different dismiss behaviours.
 */
@Composable
internal fun DialogFramePrimitive(
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    accessibilityPaneTitle: String,
    modifier: Modifier = Modifier,
    containerColor: Color = AestheticDialogsTheme.colors.surface.container,
    /**
     * Painted over [containerColor] when set. The gradient-backed feedback
     * variants use it; everything else leaves it null.
     */
    containerBrush: Brush? = null,
    shape: Shape = AestheticDialogsTheme.shapes.dialog,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    /**
     * Whether the frame scrolls the content slot itself. Components that render
     * a lazy list must set this to `false` and scroll their own content: nesting
     * a `LazyColumn` inside a scrolling `Column` is a measurement error, not a
     * style preference.
     */
    scrollableContent: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val motion = AestheticDialogsTheme.motion
    val scrimAlpha = AestheticDialogsTheme.colors.surface.scrim.alpha

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            // The library sizes its own surface; the platform default would clamp
            // every dialog to the Material minimum.
            usePlatformDefaultWidth = false,
        ),
    ) {
        DialogScrimEffect(scrimAlpha)

        // The constraints report the window the dialog was actually given, which
        // is the question that has a correct answer on foldables and in freeform
        // windows.
        BoxWithConstraints {
            val availableWidth = maxWidth
            val availableHeight = maxHeight

            val visibleState = remember { MutableTransitionState(false) }
            visibleState.targetState = true

            AnimatedVisibility(
                visibleState = visibleState,
                enter = if (motion.enabled) {
                    fadeIn(animationSpec = motion.enterSpec()) +
                        scaleIn(initialScale = ENTER_SCALE, animationSpec = motion.enterSpec())
                } else {
                    EnterTransition.None
                },
                // The caller owns whether the dialog is composed, so by the time
                // an exit could run the node is already gone.
                exit = ExitTransition.None,
            ) {
                Surface(
                    modifier = modifier
                        .width(dialogWidthFor(availableWidth))
                        .heightIn(max = availableHeight)
                        .imePadding()
                        .semantics {
                            paneTitle = accessibilityPaneTitle
                            isTraversalGroup = true
                        },
                    shape = shape,
                    color = containerColor,
                    contentColor = AestheticDialogsTheme.colors.content.primary,
                    tonalElevation = AestheticElevation.none,
                    shadowElevation = AestheticElevation.dialog,
                ) {
                    Column(
                        modifier = if (containerBrush != null) {
                            Modifier.background(brush = containerBrush, shape = shape)
                        } else {
                            Modifier
                        },
                    ) {
                        header?.invoke(this)

                        // Short content stays short; long content is capped and
                        // scrolls, so the action row stays reachable at any font
                        // size.
                        Column(
                            modifier = Modifier
                                .weight(weight = 1f, fill = false)
                                .then(
                                    if (scrollableContent) {
                                        Modifier.verticalScroll(rememberScrollState())
                                    } else {
                                        Modifier
                                    },
                                ),
                            content = content,
                        )

                        actions?.invoke()
                    }
                }
            }
        }
    }
}

private const val ENTER_SCALE = 0.92f

/**
 * Chooses the dialog width from the space available.
 *
 * Compact windows use the full width minus a margin so the dialog does not look
 * lost; medium and expanded windows pin a comfortable measure instead of
 * stretching a paragraph across a tablet.
 */
internal fun dialogWidthFor(availableWidth: Dp): Dp = when {
    availableWidth < AestheticDimens.breakpointMedium ->
        availableWidth - AestheticDimens.compactWindowMargin * 2

    availableWidth < AestheticDimens.breakpointExpanded -> AestheticDimens.dialogWidthMedium

    else -> AestheticDimens.dialogWidthExpanded
}

/**
 * Applies the theme's scrim opacity to the dialog window.
 *
 * Android dims dialog windows with a fixed black overlay whose *amount* is the
 * only settable part, so the scrim token's alpha channel is what carries over.
 * Doing it here means the scrim is a token like everything else, instead of
 * whatever the host application's theme happens to specify.
 */
@Composable
internal fun DialogScrimEffect(alpha: Float) {
    val window = (LocalView.current.parent as? DialogWindowProvider)?.window
    if (window != null) {
        LaunchedEffect(window, alpha) {
            window.setDimAmount(alpha)
        }
    }
}
