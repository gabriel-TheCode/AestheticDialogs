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
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.components.notification.AestheticNotificationHost
import com.thecode.aestheticdialogs.components.notification.models.NotificationAction
import com.thecode.aestheticdialogs.components.notification.models.NotificationQueuePolicy
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

    private val banner = NotificationUiModel.Default(
        title = "Saved",
        message = "Your changes are on every device.",
    )

    private val second = NotificationUiModel.Default(
        title = "Also saved",
        message = "The second one.",
    )

    @Test
    fun `shows the banner it is given`() {
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(notification = banner, onDismiss = {})
            }
        }

        composeRule.onNodeWithText("Saved").assertIsDisplayed()
    }

    @Test
    fun `the close affordance emits Dismissed rather than hiding the banner`() {
        val dismissals = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = banner,
                    onDismiss = { dismissals += "dismiss" },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Close").performClick()

        assertThat(dismissals).containsExactly("dismiss")
        // The caller has not removed it yet, so it is still on screen.
        composeRule.onNodeWithText("Saved").assertIsDisplayed()
    }

    @Test
    fun `auto-dismiss emits once the delay elapses`() {
        val dismissals = mutableListOf<String>()
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = banner,
                    onDismiss = { dismissals += "dismiss" },
                    autoDismissMillis = 3_000,
                )
            }
        }

        composeRule.mainClock.advanceTimeBy(2_000)
        assertThat(dismissals).isEmpty()

        composeRule.mainClock.advanceTimeBy(1_500)
        assertThat(dismissals).containsExactly("dismiss")
    }

    @Test
    fun `removing the notification takes the banner with it`() {
        composeRule.setContent {
            var current by remember { mutableStateOf<NotificationUiModel?>(banner) }
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = current,
                    onDismiss = { current = null },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Close").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Saved").assertDoesNotExist()
    }

    @Test
    fun `the trailing action is told apart from the body`() {
        val calls = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = banner.copy(action = NotificationAction("Undo")),
                    onDismiss = { calls += "dismiss" },
                    onClick = { calls += "click" },
                    onAction = { calls += "action" },
                )
            }
        }

        composeRule.onNodeWithText("Undo").performClick()

        assertThat(calls).containsExactly("action")
    }

    @Test
    fun `a status strip is not auto-dismissed, because the condition has not ended`() {
        val dismissals = mutableListOf<String>()
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = NotificationUiModel.Strip(title = "You are offline"),
                    onDismiss = { dismissals += "dismiss" },
                    autoDismissMillis = 1_000,
                )
            }
        }

        composeRule.mainClock.advanceTimeBy(5_000)

        assertThat(dismissals).isEmpty()
    }

    @Test
    fun `swiping the banner sideways asks for dismissal`() {
        val dismissals = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = banner,
                    onDismiss = { dismissals += "dismiss" },
                )
            }
        }

        composeRule.onNodeWithText("Saved").performTouchInput { swipeRight() }
        composeRule.waitForIdle()

        assertThat(dismissals).containsExactly("dismiss")
    }

    @Test
    fun `swipeToDismiss off leaves the gesture alone`() {
        val dismissals = mutableListOf<String>()
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = banner,
                    onDismiss = { dismissals += "dismiss" },
                    swipeToDismiss = false,
                )
            }
        }

        composeRule.onNodeWithText("Saved").performTouchInput { swipeRight() }
        composeRule.waitForIdle()

        assertThat(dismissals).isEmpty()
    }

    @Test
    fun `Replace hands the screen to the newcomer`() {
        val current = mutableStateOf<NotificationUiModel?>(banner)
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = current.value,
                    onDismiss = {},
                    queuePolicy = NotificationQueuePolicy.Replace,
                )
            }
        }

        composeRule.runOnUiThread { current.value = second }
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Also saved").assertIsDisplayed()
    }

    @Test
    fun `Drop keeps the banner already on screen`() {
        val current = mutableStateOf<NotificationUiModel?>(banner)
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = current.value,
                    onDismiss = {},
                    queuePolicy = NotificationQueuePolicy.Drop,
                )
            }
        }

        composeRule.runOnUiThread { current.value = second }
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Saved").assertIsDisplayed()
        composeRule.onNodeWithText("Also saved").assertDoesNotExist()
    }

    @Test
    fun `Enqueue shows the second banner once the first is taken away`() {
        val current = mutableStateOf<NotificationUiModel?>(banner)
        composeRule.setContent {
            AestheticDialogsTheme {
                AestheticNotificationHost(
                    notification = current.value,
                    onDismiss = {},
                    queuePolicy = NotificationQueuePolicy.Enqueue,
                )
            }
        }

        composeRule.runOnUiThread { current.value = second }
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Saved").assertIsDisplayed()

        // The caller dismisses what it thinks is showing; the queue drains.
        composeRule.runOnUiThread { current.value = null }
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Also saved").assertIsDisplayed()
    }
}
