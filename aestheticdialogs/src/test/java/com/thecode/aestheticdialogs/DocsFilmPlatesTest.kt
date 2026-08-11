package com.thecode.aestheticdialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import com.thecode.aestheticdialogs.components.alert.AestheticAlertDialog
import com.thecode.aestheticdialogs.components.alert.models.AlertDialogUiModel
import com.thecode.aestheticdialogs.components.confirmation.AestheticConfirmationDialog
import com.thecode.aestheticdialogs.components.confirmation.models.ConfirmationDialogUiModel
import com.thecode.aestheticdialogs.components.content.AestheticContentDialog
import com.thecode.aestheticdialogs.components.content.models.ContentDialogUiModel
import com.thecode.aestheticdialogs.components.feedback.AestheticFeedbackDialog
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogUiModel
import com.thecode.aestheticdialogs.components.input.AestheticInputDialog
import com.thecode.aestheticdialogs.components.input.models.InputDialogUiModel
import com.thecode.aestheticdialogs.components.notification.AestheticNotification
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.components.selection.AestheticSelectionDialog
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogUiModel
import com.thecode.aestheticdialogs.components.selection.models.SelectionItem
import com.thecode.aestheticdialogs.foundation.AestheticColors
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.foundation.aestheticLightColors
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Renders each component alone on white, for the launch film to composite.
 *
 * Same components, same tokens, same text as the gallery — only the demo backdrop
 * and the scrim are dropped. Each plate is captured twice, on white and on black,
 * so the crop step can solve for the component's alpha and cut it out exactly.
 * The film may move these around; it may not redraw them.
 *
 * Output is an intermediate, so it lives in `build/` rather than `docs/`.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w411dp-h700dp-xxxhdpi")
@OptIn(ExperimentalRoborazziApi::class)
class DocsFilmPlatesTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun confirmation() = plate("confirmation") {
        AestheticConfirmationDialog(
            uiModel = ConfirmationDialogUiModel.Destructive(
                title = "Delete this album?",
                message = "The 24 photos inside it will be deleted too. This cannot be undone.",
                confirmLabel = "Delete album",
                cancelLabel = "Keep it",
            ),
            onSignal = {},
        )
    }

    @Test
    fun alert() = plate("alert") {
        AestheticAlertDialog(
            uiModel = AlertDialogUiModel.Default(
                title = "You are offline",
                message = "We will sync your changes as soon as you reconnect.",
                tone = DialogTone.Warning,
                primaryAction = DialogAction("Retry"),
                secondaryAction = DialogAction("Dismiss", DialogActionEmphasis.Text),
            ),
            onSignal = {},
        )
    }

    @Test
    fun selection() = plate("selection") {
        AestheticSelectionDialog(
            uiModel = SelectionDialogUiModel.Multiple(
                title = "Notify me about",
                items = listOf(
                    SelectionItem("mentions", "Mentions", "When someone tags you"),
                    SelectionItem("releases", "Releases", "New versions of apps you follow"),
                    SelectionItem("digest", "Weekly digest", "Every Monday morning"),
                ),
                selectedIds = setOf("mentions", "releases"),
                confirmLabel = "Save",
                cancelLabel = "Cancel",
            ),
            onSignal = {},
        )
    }

    @Test
    fun input() = plate("input") {
        AestheticInputDialog(
            uiModel = InputDialogUiModel.Text(
                title = "Rename album",
                value = "Kinshasa, 2024",
                label = "Album name",
                confirmLabel = "Rename",
                cancelLabel = "Cancel",
            ),
            onValueChange = {},
            onSignal = {},
        )
    }

    @Test
    fun content() = plate("content") {
        AestheticContentDialog(
            uiModel = ContentDialogUiModel.Default(
                title = "Before you continue",
                subtitle = "Two things worth knowing",
                primaryAction = DialogAction("I agree"),
                secondaryAction = DialogAction("Not now", DialogActionEmphasis.Text),
            ),
            onSignal = {},
        ) {
            AestheticContentDialogBody()
        }
    }

    @Test
    fun feedback() = plate("feedback") {
        AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Flash(
                title = "Message sent",
                message = "It will arrive even if you close the app.",
                tone = DialogTone.Success,
                actionLabel = "Nice",
            ),
            onSignal = {},
        )
    }

    @Test
    fun bannerToaster() = banner("banner-toaster") {
        NotificationUiModel.Toaster(
            title = "Saved",
            message = "Your changes are on every device.",
            tone = DialogTone.Success,
        )
    }

    @Test
    fun bannerRainbow() = banner("banner-rainbow") {
        NotificationUiModel.Rainbow(
            title = "Version 2.0 is out",
            message = "Seven families, one design system.",
            tone = DialogTone.Info,
        )
    }

    @Test
    fun bannerConnectify() = banner("banner-connectify") {
        NotificationUiModel.Connectify(
            title = "Back online",
            message = "Syncing what you missed.",
            tone = DialogTone.Success,
        )
    }

    @Test
    fun bannerEmotion() = banner("banner-emotion") {
        NotificationUiModel.Emotion(
            title = "Amara sent a photo",
            message = "Tap to open the album",
            timestamp = "13:56",
            tone = DialogTone.Info,
        )
    }

    @Composable
    private fun AestheticContentDialogBody() {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(horizontal = AestheticSpacing.xxl),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement
                .spacedBy(AestheticSpacing.md),
        ) {
            CONSENT_LINES.forEach { line ->
                androidx.compose.material3.Text(
                    text = line,
                    style = AestheticDialogsTheme.typography.message,
                    color = AestheticDialogsTheme.colors.content.secondary,
                )
            }
        }
    }

    private fun plate(name: String, content: @Composable () -> Unit) {
        // Twice, on white and on black. The window is always opaque, so the only
        // way to recover the component's real alpha — and therefore to drop its
        // shadow cleanly — is to solve for it from two known backgrounds.
        var backdrop by mutableStateOf(Color.White)
        composeRule.setContent {
            AestheticDialogsTheme(darkTheme = false, colors = scrimless()) {
                Box(Modifier.fillMaxSize().background(backdrop)) { content() }
            }
        }
        captureScreenRoboImage("$PLATES/$name.png")
        backdrop = Color.Black
        composeRule.waitForIdle()
        captureScreenRoboImage("$PLATES/$name@black.png")
    }

    private fun banner(name: String, uiModel: @Composable () -> NotificationUiModel) {
        var backdrop by mutableStateOf(Color.White)
        composeRule.setContent {
            AestheticDialogsTheme(darkTheme = false, colors = scrimless()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backdrop)
                        .padding(AestheticSpacing.xxl),
                    contentAlignment = Alignment.Center,
                ) {
                    AestheticNotification(uiModel = uiModel(), onSignal = {})
                }
            }
        }
        captureScreenRoboImage("$PLATES/$name.png")
        backdrop = Color.Black
        composeRule.waitForIdle()
        captureScreenRoboImage("$PLATES/$name@black.png")
    }

    /** The dialog window dims the screen behind it; a plate has nothing behind it. */
    private fun scrimless(): AestheticColors = aestheticLightColors().let {
        it.copy(surface = it.surface.copy(scrim = Color.Transparent))
    }

    private companion object {
        const val PLATES = "build/film/plates"

        val CONSENT_LINES = listOf(
            "Albums stay on your device and are encrypted before they leave it.",
            "You can revoke access at any time from Settings.",
            "Nothing here is used to train anything.",
        )
    }
}
