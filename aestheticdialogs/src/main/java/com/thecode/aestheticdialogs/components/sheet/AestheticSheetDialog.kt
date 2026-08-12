package com.thecode.aestheticdialogs.components.sheet

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.thecode.aestheticdialogs.components.sheet.models.SheetDialogUiModel
import com.thecode.aestheticdialogs.components.sheet.variants.SheetDialogDefault
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * A dialog docked to the bottom of the window, where the thumb is.
 *
 * ```
 * AestheticSheetDialog(
 *     uiModel = SheetDialogUiModel.Default(
 *         title = "Share this album",
 *         message = "Anyone with the link can see the 24 photos inside it.",
 *         primaryAction = DialogAction("Copy link"),
 *         secondaryAction = DialogAction("Cancel", DialogActionEmphasis.Text),
 *     ),
 *     onDismiss = { viewModel.dismissSheet() },
 *     onPrimaryAction = { viewModel.copyLink() },
 * ) {
 *     ShareTargets(uiState.targets)
 * }
 * ```
 *
 * This composable animates in and is cut away when you stop composing it, like
 * every other modal in the library. [AestheticSheetHost] is the version that
 * slides back out, and for a sheet it is worth using: a surface that arrives from
 * the bottom edge and then vanishes reads as a glitch.
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onDismiss the scrim was tapped, back was pressed, the close affordance
 *   was used, or the sheet was dragged away.
 * @param modifier applied to the sheet surface.
 * @param onPrimaryAction the primary action was pressed.
 * @param onSecondaryAction the secondary action was pressed.
 * @param content your content, laid out in a column between the message and the
 *   action row. Pad it yourself: the sheet does not guess at your rhythm.
 */
@Composable
public fun AestheticSheetDialog(
    uiModel: SheetDialogUiModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onPrimaryAction: () -> Unit = {},
    onSecondaryAction: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = true

    SheetVariant(
        uiModel = uiModel,
        visibleState = visibleState,
        onPrimaryAction = onPrimaryAction,
        onSecondaryAction = onSecondaryAction,
        onDismiss = onDismiss,
        modifier = modifier,
        content = content,
    )
}

/**
 * Shows a sheet and animates it out when you take it away.
 *
 * ```
 * AestheticSheetHost(
 *     sheet = uiState.sheet,
 *     onDismiss = { viewModel.dismissSheet() },
 * ) {
 *     ShareTargets(uiState.targets)
 * }
 * ```
 *
 * The host is still stateless about *what* is shown: `sheet` lives in your state
 * and setting it to `null` is what dismisses it. What the host owns is the
 * retained copy it draws while the surface slides away — the same bargain
 * [com.thecode.aestheticdialogs.components.notification.AestheticNotificationHost]
 * makes, and the reason a sheet is the one modal in the library with a real exit
 * transition.
 *
 * @param sheet the sheet to show, or `null` for none.
 * @param onDismiss the sheet asked to go away.
 * @param modifier applied to the sheet surface.
 * @param onPrimaryAction the primary action was pressed.
 * @param onSecondaryAction the secondary action was pressed.
 * @param content your content. It is passed the sheet being drawn, which during
 *   the exit transition is the one you have already removed from your state.
 */
@Composable
public fun AestheticSheetHost(
    sheet: SheetDialogUiModel?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onPrimaryAction: () -> Unit = {},
    onSecondaryAction: () -> Unit = {},
    content: @Composable ColumnScope.(SheetDialogUiModel) -> Unit = {},
) {
    val currentOnDismiss by rememberUpdatedState(onDismiss)

    // Retained so the sheet still has something to draw while it animates out.
    var lastSheet by remember { mutableStateOf(sheet) }
    if (sheet != null) {
        lastSheet = sheet
    }

    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = sheet != null

    // The platform window has to outlive the caller's state by the length of the
    // exit transition, and not one frame longer.
    if (visibleState.targetState || visibleState.currentState) {
        lastSheet?.let { retained ->
            SheetVariant(
                uiModel = retained,
                visibleState = visibleState,
                onPrimaryAction = onPrimaryAction,
                onSecondaryAction = onSecondaryAction,
                onDismiss = currentOnDismiss,
                modifier = modifier,
                content = { content(retained) },
            )
        }
    }
}

/**
 * Where the drag lives.
 *
 * The offset and its animation are presentation state with no meaning outside the
 * gesture, so they belong to the component: the primitive receives two modifiers
 * and stays stateless.
 */
@Composable
private fun SheetVariant(
    uiModel: SheetDialogUiModel,
    visibleState: MutableTransitionState<Boolean>,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val exitSpec = AestheticDialogsTheme.motion.exitSpec<Float>()
    val threshold = with(LocalDensity.current) {
        AestheticDimens.sheetDragDismissDistance.toPx()
    }

    val handleDragModifier = Modifier.draggable(
        state = rememberDraggableState { delta ->
            scope.launch { offset.snapTo((offset.value + delta).coerceAtLeast(0f)) }
        },
        orientation = Orientation.Vertical,
        onDragStopped = {
            if (offset.value >= threshold) onDismiss() else offset.animateTo(0f, exitSpec)
        },
    )

    when (uiModel) {
        is SheetDialogUiModel.Default -> SheetDialogDefault(
            uiModel = uiModel,
            visibleState = visibleState,
            onPrimaryAction = onPrimaryAction,
            onSecondaryAction = onSecondaryAction,
            onDismiss = onDismiss,
            modifier = modifier,
            surfaceDragModifier = Modifier.offset { IntOffset(0, offset.value.roundToInt()) },
            handleDragModifier = handleDragModifier,
            content = content,
        )
    }
}

@ThemePreviews
@Composable
private fun SheetDialogPreview() {
    AestheticPreviewSurface {
        AestheticSheetDialog(
            uiModel = SheetDialogUiModel.Default(
                title = "Share this album",
                message = "Anyone with the link can see the 24 photos inside it.",
                primaryAction = DialogAction("Copy link"),
                secondaryAction = DialogAction("Cancel", DialogActionEmphasis.Text),
            ),
            onDismiss = {},
        ) {
            Column(
                modifier = Modifier.padding(horizontal = AestheticSpacing.xxl),
                verticalArrangement = Arrangement.spacedBy(AestheticSpacing.md),
            ) {
                listOf("Messages", "Mail", "Nearby share").forEach { target ->
                    Text(
                        text = target,
                        style = AestheticDialogsTheme.typography.itemLabel,
                        color = AestheticDialogsTheme.colors.content.primary,
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun SheetDialogNoActionsPreview() {
    AestheticPreviewSurface {
        AestheticSheetDialog(
            uiModel = SheetDialogUiModel.Default(
                title = "Sort by",
                message = "Applies to this album only.",
            ),
            onDismiss = {},
        )
    }
}
