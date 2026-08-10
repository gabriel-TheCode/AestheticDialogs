package com.thecode.aestheticdialogs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.components.notification.AestheticNotificationHost
import com.thecode.aestheticdialogs.components.notification.models.NotificationSignal
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The banner host: still stateless, but responsible for the timing behaviour a
 * caller should not have to write.
 */
@RunWith(AndroidJUnit4::class)
class NotificationHostTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val banner = NotificationUiModel.Toaster(
        title = "Saved",
        message = "Your changes are on every device.",
    )

    @Test
    fun `shows the banner it is given`() {
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(notification = banner, onSignal = {})
            }
        }

        composeRule.onNodeWithText("Saved").assertIsDisplayed()
    }

    @Test
    fun `the close affordance emits Dismissed rather than hiding the banner`() {
        val signals = mutableListOf<NotificationSignal>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(notification = banner, onSignal = signals::add)
            }
        }

        composeRule.onNodeWithContentDescription("Close").performClick()

        assertThat(signals).containsExactly(NotificationSignal.Dismissed)
        // The caller has not removed it yet, so it is still on screen.
        composeRule.onNodeWithText("Saved").assertIsDisplayed()
    }

    @Test
    fun `auto-dismiss emits once the delay elapses`() {
        val signals = mutableListOf<NotificationSignal>()
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = banner,
                    onSignal = signals::add,
                    autoDismissMillis = 3_000,
                )
            }
        }

        composeRule.mainClock.advanceTimeBy(2_000)
        assertThat(signals).isEmpty()

        composeRule.mainClock.advanceTimeBy(1_500)
        assertThat(signals).containsExactly(NotificationSignal.Dismissed)
    }

    @Test
    fun `removing the notification takes the banner with it`() {
        composeRule.setContent {
            var current by remember { mutableStateOf<NotificationUiModel?>(banner) }
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = current,
                    onSignal = { current = null },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Close").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Saved").assertDoesNotExist()
    }
}
