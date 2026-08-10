package com.thecode.aestheticdialogs.components.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import com.android.tools.screenshot.PreviewTest
import com.thecode.aestheticdialogs.components.notification.models.NotificationAlignment
import com.thecode.aestheticdialogs.components.notification.models.NotificationSignal
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.components.notification.variants.NotificationConnectify
import com.thecode.aestheticdialogs.components.notification.variants.NotificationEmoji
import com.thecode.aestheticdialogs.components.notification.variants.NotificationEmotion
import com.thecode.aestheticdialogs.components.notification.variants.NotificationRainbow
import com.thecode.aestheticdialogs.components.notification.variants.NotificationToaster
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.preview.AestheticPreviewSurface
import com.thecode.aestheticdialogs.preview.ThemePreviews
import com.thecode.aestheticdialogs.tokens.AestheticNotificationAnimation
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * A notification banner, rendered where you put it.
 *
 * Use this when you already have a place for the banner — a column above your
 * content, a slot in your own scaffold. Use [AestheticNotificationHost] when you
 * want it to float over the screen and animate itself in and out.
 *
 * @param uiModel the visual state; the subclass selects the variant.
 * @param onSignal receives taps on the banner and on its close affordance.
 * @param modifier applied to the banner surface.
 */
@Composable
public fun AestheticNotification(
    uiModel: NotificationUiModel,
    onSignal: (NotificationSignal) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiModel) {
        is NotificationUiModel.Toaster -> NotificationToaster(uiModel, onSignal, modifier)
        is NotificationUiModel.Rainbow -> NotificationRainbow(uiModel, onSignal, modifier)
        is NotificationUiModel.Connectify -> NotificationConnectify(uiModel, onSignal, modifier)
        is NotificationUiModel.Emoji -> NotificationEmoji(uiModel, onSignal, modifier)
        is NotificationUiModel.Emotion -> NotificationEmotion(uiModel, onSignal, modifier)
    }
}

/**
 * Floats a banner over your content and animates it in and out.
 *
 * Place it as the last child of the `Box` that holds your screen:
 *
 * ```
 * Box(Modifier.fillMaxSize()) {
 *     HomeContent()
 *     AestheticNotificationHost(
 *         notification = uiState.banner,
 *         onSignal = { viewModel.dismissBanner() },
 *         autoDismissMillis = 4_000,
 *     )
 * }
 * ```
 *
 * The host is still stateless: `notification` lives in your state and setting it
 * to `null` is what dismisses the banner. The host retains the last non-null
 * value for the length of the exit transition, which is presentation state and
 * nothing more — it is why banners get a real exit animation while modal dialogs
 * do not.
 *
 * @param notification the banner to show, or `null` for none.
 * @param onSignal receives taps, close presses and the auto-dismiss timeout.
 * @param modifier applied to the overlay box.
 * @param alignment which edge the banner sits against, and therefore which way
 *   it slides.
 * @param animation how it enters and leaves. Collapses to an instant cut when
 *   the user has asked the system for reduced motion.
 * @param autoDismissMillis emits [NotificationSignal.Dismissed] after this delay.
 *   `null` keeps the banner until you remove it. The timer restarts whenever a
 *   different banner is shown.
 */
@Composable
public fun AestheticNotificationHost(
    notification: NotificationUiModel?,
    onSignal: (NotificationSignal) -> Unit,
    modifier: Modifier = Modifier,
    alignment: NotificationAlignment = NotificationAlignment.Top,
    animation: AestheticNotificationAnimation = AestheticNotificationAnimation.Slide,
    autoDismissMillis: Long? = null,
) {
    val currentOnSignal by rememberUpdatedState(onSignal)

    // Retained so the banner still has something to draw while it animates out.
    var lastNotification by remember { mutableStateOf(notification) }
    if (notification != null) {
        lastNotification = notification
    }

    if (notification != null && autoDismissMillis != null) {
        LaunchedEffect(notification, autoDismissMillis) {
            delay(autoDismissMillis.milliseconds)
            currentOnSignal(NotificationSignal.Dismissed)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(AestheticSpacing.lg),
        contentAlignment = when (alignment) {
            NotificationAlignment.Top -> Alignment.TopCenter
            NotificationAlignment.Bottom -> Alignment.BottomCenter
        },
    ) {
        val motionEnabled = AestheticDialogsTheme.motion.enabled
        val effectiveAnimation = if (motionEnabled) {
            animation
        } else {
            AestheticNotificationAnimation.None
        }

        AnimatedVisibility(
            visible = notification != null,
            enter = enterTransitionFor(effectiveAnimation, alignment),
            exit = exitTransitionFor(effectiveAnimation, alignment),
        ) {
            lastNotification?.let { retained ->
                AestheticNotification(uiModel = retained, onSignal = currentOnSignal)
            }
        }
    }
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

@PreviewTest
@ThemePreviews
@Composable
private fun NotificationToasterPreview() {
    AestheticPreviewSurface {
        AestheticNotification(
            uiModel = NotificationUiModel.Toaster(
                title = "Saved",
                message = "Your changes are on every device.",
                tone = DialogTone.Success,
            ),
            onSignal = {},
        )
    }
}

@PreviewTest
@ThemePreviews
@Composable
private fun NotificationRainbowPreview() {
    AestheticPreviewSurface {
        AestheticNotification(
            uiModel = NotificationUiModel.Rainbow(
                title = "Heads up",
                message = "Two invoices are due this week.",
                tone = DialogTone.Warning,
            ),
            onSignal = {},
        )
    }
}

@PreviewTest
@ThemePreviews
@Composable
private fun NotificationConnectifyPreview() {
    AestheticPreviewSurface {
        AestheticNotification(
            uiModel = NotificationUiModel.Connectify(
                title = "Back online",
                message = "Syncing what you missed.",
                tone = DialogTone.Success,
            ),
            onSignal = {},
        )
    }
}

@PreviewTest
@ThemePreviews
@Composable
private fun NotificationEmojiPreview() {
    AestheticPreviewSurface {
        AestheticNotification(
            uiModel = NotificationUiModel.Emoji(
                title = "Nice one",
                message = "That was your tenth album this month.",
                tone = DialogTone.Success,
            ),
            onSignal = {},
        )
    }
}

@PreviewTest
@ThemePreviews
@Composable
private fun NotificationEmotionPreview() {
    AestheticPreviewSurface {
        AestheticNotification(
            uiModel = NotificationUiModel.Emotion(
                title = "Amara sent a photo",
                message = "Tap to open the album",
                timestamp = "13:56",
                tone = DialogTone.Info,
            ),
            onSignal = {},
        )
    }
}
