package com.thecode.aestheticdialogs.components.sheet.primitives

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.thecode.aestheticdialogs.R
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.primitives.DialogActionsRowPrimitive
import com.thecode.aestheticdialogs.primitives.DialogHeaderPrimitive
import com.thecode.aestheticdialogs.primitives.DialogMessagePrimitive
import com.thecode.aestheticdialogs.primitives.DialogScrimEffect
import com.thecode.aestheticdialogs.primitives.dialogWidthFor
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticElevation
import com.thecode.aestheticdialogs.tokens.AestheticSpacing

/**
 * A surface docked to the bottom of the window on compact widths, and an ordinary
 * centred dialog once there is room for one.
 *
 * It holds no state. [visibleState] drives the transition and belongs to the
 * component — which is what lets a sheet animate *out*: the host keeps the window
 * composed until the exit has finished. [surfaceDragModifier] and
 * [handleDragModifier] carry the drag gesture from the same place, and are
 * applied only when the sheet is docked: a centred sheet has no edge to be
 * dragged towards.
 */
@Composable
internal fun SheetPrimitive(
    title: String,
    message: String?,
    visibleState: MutableTransitionState<Boolean>,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    modifier: Modifier = Modifier,
    surfaceDragModifier: Modifier = Modifier,
    handleDragModifier: Modifier = Modifier,
    showDragHandle: Boolean = true,
    showCloseButton: Boolean = false,
    primaryLabel: String? = null,
    primaryContainerColor: Color = Color.Transparent,
    primaryContentColor: Color = Color.Transparent,
    onPrimaryClick: () -> Unit = {},
    secondaryLabel: String? = null,
    secondaryContainerColor: Color = Color.Transparent,
    secondaryContentColor: Color = primaryContentColor,
    secondaryBorder: BorderStroke? = null,
    onSecondaryClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val colors = AestheticDialogsTheme.colors
    val motion = AestheticDialogsTheme.motion
    val handleLabel = stringResource(R.string.aesthetic_dialogs_drag_handle)
    val dismissLabel = stringResource(R.string.aesthetic_dialogs_dismiss)

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = false,
        ),
    ) {
        DialogScrimEffect(colors.surface.scrim.alpha)

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            // Below the medium breakpoint the sheet is a sheet; above it there is
            // room for a dialog, and a surface pinned to the bottom edge of a
            // tablet is a long way from anything the user is looking at.
            val availableWidth = maxWidth
            val docked = availableWidth < AestheticDimens.breakpointMedium
            val maxSheetHeight = maxHeight * MAX_HEIGHT_FRACTION

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = if (docked) Alignment.BottomCenter else Alignment.Center,
            ) {
                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = if (!motion.enabled) {
                        EnterTransition.None
                    } else if (docked) {
                        slideInVertically(motion.enterSpec()) { it } + fadeIn(motion.enterSpec())
                    } else {
                        scaleIn(initialScale = ENTER_SCALE, animationSpec = motion.enterSpec()) +
                            fadeIn(motion.enterSpec())
                    },
                    exit = if (!motion.enabled) {
                        ExitTransition.None
                    } else if (docked) {
                        slideOutVertically(motion.exitSpec()) { it } + fadeOut(motion.exitSpec())
                    } else {
                        scaleOut(targetScale = ENTER_SCALE, animationSpec = motion.exitSpec()) +
                            fadeOut(motion.exitSpec())
                    },
                ) {
                    Surface(
                        modifier = modifier
                            .width(if (docked) availableWidth else dialogWidthFor(availableWidth))
                            .heightIn(max = maxSheetHeight)
                            .then(if (docked) surfaceDragModifier else Modifier)
                            .imePadding()
                            .semantics {
                                paneTitle = title
                                isTraversalGroup = true
                            },
                        shape = if (docked) {
                            AestheticDialogsTheme.shapes.sheet
                        } else {
                            AestheticDialogsTheme.shapes.dialog
                        },
                        color = colors.surface.container,
                        contentColor = colors.content.primary,
                        tonalElevation = AestheticElevation.none,
                        shadowElevation = AestheticElevation.dialog,
                    ) {
                        Column {
                            if (showDragHandle) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = AestheticDimens.minTouchTarget)
                                        .then(if (docked) handleDragModifier else Modifier)
                                        // Pointer dragging is not available to
                                        // every user, so the same outcome is
                                        // offered as an accessibility action.
                                        .semantics {
                                            contentDescription = handleLabel
                                            customActions = listOf(
                                                CustomAccessibilityAction(dismissLabel) {
                                                    onDismissRequest()
                                                    true
                                                },
                                            )
                                        },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(
                                                width = AestheticDimens.sheetHandleWidth,
                                                height = AestheticDimens.sheetHandleHeight,
                                            )
                                            .background(
                                                color = colors.border.default,
                                                shape = AestheticDialogsTheme.shapes.button,
                                            ),
                                    )
                                }
                            }

                            DialogHeaderPrimitive(
                                title = title,
                                onCloseClick = if (showCloseButton) onDismissRequest else null,
                            )

                            Column(
                                modifier = Modifier
                                    .weight(weight = 1f, fill = false)
                                    .verticalScroll(rememberScrollState()),
                            ) {
                                message?.let { DialogMessagePrimitive(message = it) }
                                content()
                                Spacer(Modifier.height(AestheticSpacing.sm))
                            }

                            if (primaryLabel != null || secondaryLabel != null) {
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
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val ENTER_SCALE = 0.92f

/** A sheet stops short of the top of the window, so the scrim stays visible above it. */
private const val MAX_HEIGHT_FRACTION = 0.9f
