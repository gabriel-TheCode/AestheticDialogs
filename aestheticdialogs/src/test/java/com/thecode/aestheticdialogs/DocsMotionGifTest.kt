package com.thecode.aestheticdialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoboVideoOptions
import com.github.takahirom.roborazzi.recordRoboVideo
import com.thecode.aestheticdialogs.components.notification.AestheticNotificationHost
import com.thecode.aestheticdialogs.components.notification.models.NotificationAlignment
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.tokens.AestheticMotion
import com.thecode.aestheticdialogs.tokens.AestheticNotificationAnimation
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Records the animated documentation images.
 *
 * A still picture of a banner says what it looks like; it says nothing about the
 * thing the notification host exists for, which is that a banner arrives and
 * leaves. These are recorded off the real component with the Compose clock driven
 * frame by frame, so a change to
 * [com.thecode.aestheticdialogs.tokens.AestheticMotion] shows up in the README.
 *
 * Only the banner host is filmed. Modal dialogs are shown and removed by the
 * caller, so the library has no exit transition to record for them — the same
 * reason `docs/ARCHITECTURE.md` gives for the asymmetry.
 *
 * Video capture is record-only in Roborazzi; `verifyRoborazziDebug` skips it.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w420dp-h180dp-hdpi")
@OptIn(ExperimentalRoborazziApi::class)
class DocsMotionGifTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun slideFromTop() = record("notification-slide", AestheticNotificationAnimation.Slide)

    @Test
    fun fadeInPlace() = record("notification-fade", AestheticNotificationAnimation.Fade)

    @Test
    fun scaleUp() = record("notification-scale", AestheticNotificationAnimation.Scale)

    private fun record(name: String, animation: AestheticNotificationAnimation) {
        // Owned by the test rather than by the composition: the recording drives
        // it from outside, which is exactly how a caller drives the host.
        val banner: MutableState<NotificationUiModel?> = mutableStateOf(null)

        composeRule.setContent {
            AestheticDialogsTheme(
                darkTheme = false,
                // The platform animation scale is meaningless under Robolectric,
                // so motion is stated explicitly. The durations are the shipped
                // ones — the recording is documentation, not decoration.
                motion = AestheticMotion(enabled = true),
            ) {
                Box(
                    modifier =
                    Modifier
                        .testTag(STAGE_TAG)
                        .width(STAGE_WIDTH)
                        .height(STAGE_HEIGHT)
                        .background(AestheticDialogsTheme.colors.surface.sunken),
                ) {
                    AestheticNotificationHost(
                        notification = banner.value,
                        onDismiss = {},
                        alignment = NotificationAlignment.Top,
                        animation = animation,
                    )
                }
            }
        }

        composeRule.onNodeWithTag(STAGE_TAG).recordRoboVideo(
            composeRule = composeRule,
            filePath = "$DOCS_IMAGES/$name.gif",
            videoOptions = RoboVideoOptions(fps = FPS, backgroundColor = STAGE_BACKGROUND),
        ) {
            delay(BEAT_MILLIS)
            banner.value = SAMPLE
            delay(BEAT_MILLIS * 3)
            banner.value = null
            delay(BEAT_MILLIS * 2)
        }
    }

    private companion object {
        const val DOCS_IMAGES = "../docs/images"
        const val STAGE_TAG = "motion-stage"

        val STAGE_WIDTH = 420.dp
        val STAGE_HEIGHT = 160.dp

        /** A step of 40ms, which GIF encodes exactly. */
        const val FPS = 25
        const val BEAT_MILLIS = 400L

        /** `surface.sunken` in the light scheme, so the letterbox matches the stage. */
        const val STAGE_BACKGROUND: Int = 0xFFF6F7F9.toInt()

        val SAMPLE =
            NotificationUiModel.Default(
                title = "Saved",
                message = "Your changes are on every device.",
                tone = DialogTone.Success,
            )
    }
}
