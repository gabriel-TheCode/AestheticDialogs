package com.thecode.aestheticdialogs.components.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.android.tools.screenshot.PreviewTest
import com.thecode.aestheticdialogs.components.notification.models.NotificationAlignment
import com.thecode.aestheticdialogs.components.notification.models.NotificationQueuePolicy
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.components.notification.variants.NotificationAmbient
import com.thecode.aestheticdialogs.components.notification.variants.NotificationDefault
import com.thecode.aestheticdialogs.components.notification.variants.NotificationFilled
import com.thecode.aestheticdialogs.components.notification.variants.NotificationGradient
import com.thecode.aestheticdialogs.components.notification.variants.NotificationStrip
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews
import com.thecode.aestheticdialogs.tokens.AestheticDimens
import com.thecode.aestheticdialogs.tokens.AestheticNotificationAnimation
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

/**
 * A notification banner, rendered where you put it.
 *
 * Use this when you already have a place for the banner — a column above your
 * content, a slot in your own scaffold. Use [AestheticNotificationHost] when you
 * want it to float over the screen, animate itself in and out, be swiped away, or
 * show how long it has left.
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onDismiss the close affordance was pressed.
 * @param modifier applied to the banner surface.
 * @param onClick the banner body was tapped. A banner that leads nowhere leaves
 *   it out.
 * @param onAction the trailing action was pressed. Only reachable when the model
 *   carries one.
 * @param leading drawn in the leading slot in place of the tone mark. An avatar,
 *   usually: a banner that comes from a person should show the person.
 */
@Composable
public fun AestheticNotification(
    uiModel: NotificationUiModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onAction: () -> Unit = {},
    leading: @Composable (() -> Unit)? = null,
) {
    NotificationVariant(uiModel, onClick, onAction, onDismiss, modifier, leading, countdown = null)
}

@Composable
private fun NotificationVariant(
    uiModel: NotificationUiModel,
    onClick: () -> Unit,
    onAction: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier,
    leading: @Composable (() -> Unit)?,
    countdown: Float?,
    stripInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
) {
    when (uiModel) {
        is NotificationUiModel.Default ->
            NotificationDefault(uiModel, onClick, onAction, onDismiss, modifier, leading, countdown)

        is NotificationUiModel.Filled ->
            NotificationFilled(uiModel, onClick, onAction, onDismiss, modifier, leading, countdown)

        is NotificationUiModel.Gradient ->
            NotificationGradient(uiModel, onClick, onAction, onDismiss, modifier, leading, countdown)

        is NotificationUiModel.Ambient ->
            NotificationAmbient(uiModel, onClick, onAction, onDismiss, modifier, countdown)

        is NotificationUiModel.Strip ->
            NotificationStrip(uiModel, onClick, onAction, onDismiss, modifier, stripInsets)
    }
}

/**
 * Floats a banner over your content and animates it in and out.
 *
 * ```
 * Box(Modifier.fillMaxSize()) {
 *     HomeContent()
 *     AestheticNotificationHost(
 *         notification = uiState.banner,
 *         onDismiss = { viewModel.dismissBanner() },
 *         onAction = { viewModel.undo() },
 *         autoDismissMillis = 4_000,
 *     )
 * }
 * ```
 *
 * The host is stateless about *what* is shown: `notification` lives in your state
 * and setting it to `null` is what dismisses the banner. What it owns is
 * presentation timing — the retained copy it draws while the banner slides away,
 * the auto-dismiss delay, and, under [NotificationQueuePolicy.Enqueue], the
 * banners waiting their turn.
 *
 * A [NotificationUiModel.Strip] is docked rather than floated: it sits flush
 * against its edge with no margin, and its auto-dismiss delay is ignored, because
 * a condition is still true four seconds later.
 *
 * @param notification the banner to show, or `null` for none.
 * @param onDismiss the close affordance was pressed, the banner was swiped away,
 *   or the auto-dismiss delay elapsed.
 * @param modifier applied to the overlay box.
 * @param onClick the banner body was tapped.
 * @param onAction the trailing action was pressed.
 * @param leading drawn in the leading slot of the banner being shown.
 * @param alignment which edge the banner sits against, and therefore which way it
 *   slides.
 * @param animation how it enters and leaves. Collapses to an instant cut when the
 *   user has asked the system for reduced motion.
 * @param autoDismissMillis calls [onDismiss] after this delay. `null` keeps the
 *   banner until you remove it. The timer restarts whenever a different banner is
 *   shown.
 * @param queuePolicy what happens when a banner arrives while another is showing.
 * @param swipeToDismiss whether a sideways drag past a threshold calls
 *   [onDismiss]. Users swipe at banners whether or not the gesture exists; when
 *   it does nothing, that reads as a bug.
 * @param showCountdown draws a hairline that drains with [autoDismissMillis]. An
 *   invisible timer is what makes "Undo" a gamble.
 */
@Composable
public fun AestheticNotificationHost(
    notification: NotificationUiModel?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onAction: () -> Unit = {},
    leading: @Composable (() -> Unit)? = null,
    alignment: NotificationAlignment = NotificationAlignment.Top,
    animation: AestheticNotificationAnimation = AestheticNotificationAnimation.Slide,
    autoDismissMillis: Long? = null,
    queuePolicy: NotificationQueuePolicy = NotificationQueuePolicy.Replace,
    swipeToDismiss: Boolean = true,
    showCountdown: Boolean = true,
) {
    val currentOnDismiss by rememberUpdatedState(onDismiss)
    val currentOnClick by rememberUpdatedState(onClick)
    val currentOnAction by rememberUpdatedState(onAction)

    val pending = remember { mutableStateListOf<NotificationUiModel>() }
    var shown by remember { mutableStateOf(notification) }

    // Retained so the banner still has something to draw while it animates out.
    var lastShown by remember { mutableStateOf(notification) }

    LaunchedEffect(notification, queuePolicy) {
        when {
            notification == null -> shown = null

            shown == null || shown == notification -> shown = notification

            else -> when (queuePolicy) {
                NotificationQueuePolicy.Replace -> {
                    pending.clear()
                    shown = notification
                }

                // Capped: a host that queues without a limit turns an emission
                // loop into a banner that never stops arriving.
                NotificationQueuePolicy.Enqueue ->
                    if (pending.size < MAX_PENDING && notification !in pending) {
                        pending.add(notification)
                    }

                NotificationQueuePolicy.Drop -> Unit
            }
        }
    }

    // The queue drains when the caller takes the current banner away, which is
    // the same moment the exit transition starts.
    LaunchedEffect(shown) {
        if (shown == null && pending.isNotEmpty()) {
            shown = pending.removeAt(0)
        }
    }

    shown?.let { lastShown = it }

    val docked = shown is NotificationUiModel.Strip || lastShown is NotificationUiModel.Strip
    val timed = autoDismissMillis != null && shown !is NotificationUiModel.Strip
    val motionEnabled = AestheticDialogsTheme.motion.enabled
    val countdown = remember { Animatable(1f) }

    if (timed) {
        LaunchedEffect(shown, autoDismissMillis) {
            if (shown == null) return@LaunchedEffect
            countdown.snapTo(1f)
            delay(autoDismissMillis.milliseconds)
            currentOnDismiss()
        }
    }

    // The hairline is a second animation rather than a read of the timer above:
    // the delay is the contract, the drain is only its picture, and a dropped
    // frame must not shorten the delay.
    if (timed && showCountdown && motionEnabled) {
        LaunchedEffect(shown, autoDismissMillis) {
            if (shown == null) return@LaunchedEffect
            countdown.animateTo(0f, tween(autoDismissMillis!!.toInt(), easing = LinearEasing))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (docked) {
                    Modifier
                } else {
                    Modifier
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .padding(AestheticSpacing.lg)
                },
            ),
        contentAlignment = when (alignment) {
            NotificationAlignment.Top -> Alignment.TopCenter
            NotificationAlignment.Bottom -> Alignment.BottomCenter
        },
    ) {
        val effectiveAnimation = if (motionEnabled) {
            animation
        } else {
            AestheticNotificationAnimation.None
        }

        AnimatedVisibility(
            visible = shown != null,
            enter = enterTransitionFor(effectiveAnimation, alignment),
            exit = exitTransitionFor(effectiveAnimation, alignment),
        ) {
            lastShown?.let { retained ->
                NotificationVariant(
                    uiModel = retained,
                    onClick = currentOnClick,
                    onAction = currentOnAction,
                    onDismiss = currentOnDismiss,
                    modifier = if (swipeToDismiss && retained !is NotificationUiModel.Strip) {
                        Modifier.swipeToDismiss { currentOnDismiss() }
                    } else {
                        Modifier
                    },
                    leading = leading,
                    countdown = countdown.value.takeIf { timed && showCountdown && motionEnabled },
                    // A docked strip is the one banner drawn behind the system
                    // bars, so it is the one that has to inset its own copy.
                    stripInsets = WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal + when (alignment) {
                            NotificationAlignment.Top -> WindowInsetsSides.Top
                            NotificationAlignment.Bottom -> WindowInsetsSides.Bottom
                        },
                    ),
                )
            }
        }
    }
}

/**
 * Sideways drag that dismisses past a threshold, and springs back before it.
 *
 * The offset is presentation state with no meaning outside the gesture: whether
 * the banner is gone is still the caller's to decide, and this only asks.
 */
@Composable
private fun Modifier.swipeToDismiss(onDismiss: () -> Unit): Modifier {
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val threshold = with(LocalDensity.current) {
        AestheticDimens.bannerSwipeDismissDistance.toPx()
    }
    val exitSpec = AestheticDialogsTheme.motion.exitSpec<Float>()

    return this
        .offset { IntOffset(offset.value.roundToInt(), 0) }
        .alpha(1f - (abs(offset.value) / (threshold * FADE_OUT_RATIO)).coerceIn(0f, 1f))
        .draggable(
            state = rememberDraggableState { delta ->
                scope.launch { offset.snapTo(offset.value + delta) }
            },
            orientation = Orientation.Horizontal,
            onDragStopped = {
                if (abs(offset.value) >= threshold) {
                    onDismiss()
                } else {
                    offset.animateTo(0f, exitSpec)
                }
            },
        )
}

@Composable
private fun enterTransitionFor(
    animation: AestheticNotificationAnimation,
    alignment: NotificationAlignment,
): EnterTransition {
    val spec = AestheticDialogsTheme.motion.enterSpec<Float>()
    val offsetSpec = AestheticDialogsTheme.motion.enterSpec<IntOffset>()
    return when (animation) {
        AestheticNotificationAnimation.Slide -> slideInVertically(offsetSpec) { height ->
            if (alignment == NotificationAlignment.Top) -height else height
        } + fadeIn(spec)

        AestheticNotificationAnimation.SlideHorizontal ->
            slideInHorizontally(offsetSpec) { width -> -width } + fadeIn(spec)

        AestheticNotificationAnimation.Fade -> fadeIn(spec)

        AestheticNotificationAnimation.Scale ->
            scaleIn(initialScale = SCALE_FROM, animationSpec = spec) + fadeIn(spec)

        AestheticNotificationAnimation.None -> EnterTransition.None
    }
}

@Composable
private fun exitTransitionFor(
    animation: AestheticNotificationAnimation,
    alignment: NotificationAlignment,
): ExitTransition {
    val spec = AestheticDialogsTheme.motion.exitSpec<Float>()
    val offsetSpec = AestheticDialogsTheme.motion.exitSpec<IntOffset>()
    return when (animation) {
        AestheticNotificationAnimation.Slide -> slideOutVertically(offsetSpec) { height ->
            if (alignment == NotificationAlignment.Top) -height else height
        } + fadeOut(spec)

        AestheticNotificationAnimation.SlideHorizontal ->
            slideOutHorizontally(offsetSpec) { width -> -width } + fadeOut(spec)

        AestheticNotificationAnimation.Fade -> fadeOut(spec)

        AestheticNotificationAnimation.Scale ->
            scaleOut(targetScale = SCALE_FROM, animationSpec = spec) + fadeOut(spec)

        AestheticNotificationAnimation.None -> ExitTransition.None
    }
}

private const val SCALE_FROM = 0.92f

/** The banner is fully transparent a little past the dismiss threshold, never before it. */
private const val FADE_OUT_RATIO = 1.6f

private const val MAX_PENDING = 4

@PreviewTest
@ThemePreviews
@Composable
private fun NotificationSubtlePreview() {
    AestheticPreviewSurface {
        AestheticNotification(
            uiModel = NotificationUiModel.Default(
                title = "Saved",
                message = "Your changes are on every device.",
                tone = DialogTone.Success,
            ),
            onDismiss = {},
        )
    }
}

@PreviewTest
@ThemePreviews
@Composable
private fun NotificationFilledPreview() {
    AestheticPreviewSurface {
        AestheticNotification(
            uiModel = NotificationUiModel.Filled(
                title = "Heads up",
                message = "Two invoices are due this week.",
                tone = DialogTone.Warning,
            ),
            onDismiss = {},
        )
    }
}

@PreviewTest
@ThemePreviews
@Composable
private fun NotificationEmojiPreview() {
    AestheticPreviewSurface {
        AestheticNotification(
            uiModel = NotificationUiModel.Default(
                title = "Nice one",
                message = "That was your tenth album this month.",
                tone = DialogTone.Success,
                emoji = "\uD83D\uDC4D",
            ),
            onDismiss = {},
        )
    }
}

@PreviewTest
@ThemePreviews
@Composable
private fun NotificationGradientPreview() {
    AestheticPreviewSurface {
        AestheticNotification(
            uiModel = NotificationUiModel.Gradient(
                title = "Amara sent a photo",
                message = "Tap to open the album",
                tone = DialogTone.Info,
                timestamp = "13:56",
            ),
            onDismiss = {},
        )
    }
}

@PreviewTest
@ThemePreviews
@Composable
private fun NotificationAmbientPreview() {
    AestheticPreviewSurface {
        AestheticNotification(
            uiModel = NotificationUiModel.Ambient(
                title = "Back online",
                message = "Syncing what you missed.",
                tone = DialogTone.Success,
            ),
            onDismiss = {},
        )
    }
}
