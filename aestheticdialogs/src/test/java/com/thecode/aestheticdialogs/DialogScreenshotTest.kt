package com.thecode.aestheticdialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureScreenRoboImage
import com.thecode.aestheticdialogs.components.alert.AestheticAlertDialog
import com.thecode.aestheticdialogs.components.alert.models.AlertDialogUiModel
import com.thecode.aestheticdialogs.components.confirmation.AestheticConfirmationDialog
import com.thecode.aestheticdialogs.components.confirmation.models.ConfirmationDialogUiModel
import com.thecode.aestheticdialogs.components.feedback.AestheticFeedbackDialog
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogUiModel
import com.thecode.aestheticdialogs.components.header.AestheticHeaderDialog
import com.thecode.aestheticdialogs.components.header.models.HeaderDialogUiModel
import com.thecode.aestheticdialogs.components.input.AestheticInputDialog
import com.thecode.aestheticdialogs.components.input.models.InputDialogUiModel
import com.thecode.aestheticdialogs.components.notification.AestheticNotificationHost
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.components.progress.AestheticProgressDialog
import com.thecode.aestheticdialogs.components.progress.models.ProgressDialogUiModel
import com.thecode.aestheticdialogs.components.selection.AestheticSelectionDialog
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogUiModel
import com.thecode.aestheticdialogs.components.selection.models.SelectionItem
import com.thecode.aestheticdialogs.components.sheet.AestheticSheetDialog
import com.thecode.aestheticdialogs.components.sheet.models.SheetDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Visual regression coverage.
 *
 * The strategy is deliberately narrow: **one representative state per
 * component, in both themes, plus the two states that break layouts** (a long
 * message and a doubled font scale). That is fourteen images.
 *
 * The alternative — every variant times every tone times every theme times every
 * font scale — is over four hundred images that nobody reviews, and a diff that
 * everybody approves without looking. Behaviour is covered by the interaction
 * tests next to this file; these images exist to catch the things a semantics
 * assertion cannot see: a colour that stopped resolving, a padding that
 * collapsed, a surface that lost its corner radius.
 *
 * Record the baselines with `./gradlew recordRoborazziDebug`, and check them with
 * `./gradlew verifyRoborazziDebug`.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel5)
@OptIn(ExperimentalRoborazziApi::class)
class DialogScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun confirmationDestructiveLight() = capture("confirmation_destructive_light") {
        DestructiveConfirmation()
    }

    @Test
    fun confirmationDestructiveDark() = capture("confirmation_destructive_dark", dark = true) {
        DestructiveConfirmation()
    }

    @Test
    fun alertWithTwoActionsLight() = capture("alert_two_actions_light") {
        TwoActionAlert()
    }

    @Test
    fun alertWithTwoActionsDark() = capture("alert_two_actions_dark", dark = true) {
        TwoActionAlert()
    }

    @Test
    fun alertWithLongMessage() = capture("alert_long_message_light") {
        AestheticAlertDialog(
            uiModel = AlertDialogUiModel.Default(
                title = "How your albums are stored",
                message = LONG_MESSAGE,
                tone = DialogTone.Info,
                primaryAction = DialogAction("Got it"),
                showCloseButton = true,
            ),
            onPrimaryAction = {},
            onDismiss = {},
        )
    }

    @Test
    fun selectionMultipleLight() = capture("selection_multiple_light") {
        MultiSelection()
    }

    @Test
    fun selectionMultipleDark() = capture("selection_multiple_dark", dark = true) {
        MultiSelection()
    }

    @Test
    fun inputWithErrorLight() = capture("input_error_light") {
        ErroredInput()
    }

    @Test
    fun inputWithErrorDark() = capture("input_error_dark", dark = true) {
        ErroredInput()
    }

    @Test
    fun feedbackGradientLight() = capture("feedback_gradient_light") {
        GradientFeedback()
    }

    @Test
    fun feedbackGradientDark() = capture("feedback_gradient_dark", dark = true) {
        GradientFeedback()
    }

    @Test
    fun notificationDefaultLight() = capture("notification_default_light") {
        DefaultBanner()
    }

    @Test
    fun notificationDefaultDark() = capture("notification_default_dark", dark = true) {
        DefaultBanner()
    }

    @Test
    fun sheetLight() = capture("sheet_light") { ShareSheet() }

    @Test
    fun sheetDark() = capture("sheet_dark", dark = true) { ShareSheet() }

    @Test
    fun headerLight() = capture("header_light") { WhatsNewHeader() }

    @Test
    fun headerDark() = capture("header_dark", dark = true) { WhatsNewHeader() }

    @Test
    fun progressLight() = capture("progress_light") { DeterminateProgress() }

    @Test
    fun progressDark() = capture("progress_dark", dark = true) { DeterminateProgress() }

    @Test
    fun confirmationAtDoubleFontScale() =
        capture("confirmation_font_scale_200", fontScale = 2f) {
            DestructiveConfirmation()
        }

    private fun capture(
        name: String,
        dark: Boolean = false,
        fontScale: Float = 1f,
        content: @Composable () -> Unit,
    ) {
        composeRule.setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(density.density, fontScale),
            ) {
                AestheticDialogsTheme(darkTheme = dark) {
                    content()
                }
            }
        }
        // Dialogs live in their own window, so the whole screen is captured
        // rather than the root of the main composition.
        captureScreenRoboImage("$SCREENSHOT_DIR/$name.png")
    }

    @Composable
    private fun DestructiveConfirmation() {
        AestheticConfirmationDialog(
            uiModel = ConfirmationDialogUiModel.Destructive(
                title = "Delete this album?",
                message = "The 24 photos inside it will be deleted too. This cannot be undone.",
                confirmLabel = "Delete album",
                cancelLabel = "Keep it",
            ),
            onConfirm = {},
            onCancel = {},
            onDismiss = {},
        )
    }

    @Composable
    private fun TwoActionAlert() {
        AestheticAlertDialog(
            uiModel = AlertDialogUiModel.Default(
                title = "Upload failed",
                message = "The connection dropped after 12 of 24 photos.",
                tone = DialogTone.Error,
                primaryAction = DialogAction("Retry"),
                secondaryAction = DialogAction("Cancel", DialogActionEmphasis.Secondary),
            ),
            onPrimaryAction = {},
            onDismiss = {},
        )
    }

    @Composable
    private fun MultiSelection() {
        AestheticSelectionDialog(
            uiModel = SelectionDialogUiModel.Multiple(
                title = "Notify me about",
                items = listOf(
                    SelectionItem("en", "English", "United Kingdom"),
                    SelectionItem("fr", "Français", "France"),
                    SelectionItem("sw", "Kiswahili", "Coming soon", enabled = false),
                ),
                selectedIds = setOf("en"),
                confirmLabel = "Save",
                cancelLabel = "Cancel",
                searchQuery = "",
            ),
            onItemClick = {},
            onCancel = {},
        )
    }

    @Composable
    private fun ErroredInput() {
        AestheticInputDialog(
            uiModel = InputDialogUiModel.Text(
                title = "Add a recipient",
                value = "not-an-address",
                label = "Email",
                errorText = "That does not look like an email address.",
                confirmLabel = "Add",
                cancelLabel = "Cancel",
                isConfirmEnabled = false,
            ),
            onValueChange = {},
            onConfirm = {},
            onCancel = {},
        )
    }

    @Composable
    private fun GradientFeedback() {
        AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Gradient(
                title = "Message sent",
                message = "It will arrive even if you close the app.",
                tone = DialogTone.Success,
                actionLabel = "Nice",
            ),
            onDismiss = {},
        )
    }

    @Composable
    private fun ShareSheet() {
        AestheticSheetDialog(
            uiModel = SheetDialogUiModel.Default(
                title = "Share this album",
                message = "Anyone with the link can see the 24 photos inside it.",
                primaryAction = DialogAction("Copy link"),
                secondaryAction = DialogAction("Cancel", DialogActionEmphasis.Text),
            ),
            onDismiss = {},
        )
    }

    @Composable
    private fun WhatsNewHeader() {
        AestheticHeaderDialog(
            uiModel = HeaderDialogUiModel.Default(
                title = "Albums, offline",
                message = "Everything you starred is on the device now.",
                primaryAction = DialogAction("Take the tour"),
                secondaryAction = DialogAction("Later", DialogActionEmphasis.Text),
            ),
            onPrimaryAction = {},
            onDismiss = {},
        )
    }

    @Composable
    private fun DeterminateProgress() {
        AestheticProgressDialog(
            uiModel = ProgressDialogUiModel.Determinate(
                title = "Uploading",
                message = "Keep the app open until this finishes.",
                progress = 0.5f,
                progressLabel = "12 of 24",
                tone = DialogTone.Info,
                cancelLabel = "Cancel upload",
            ),
            onCancel = {},
        )
    }

    @Composable
    private fun DefaultBanner() {
        AestheticNotificationHost(
            notification = NotificationUiModel.Default(
                title = "Saved",
                message = "Your changes are on every device.",
                tone = DialogTone.Success,
            ),
            onDismiss = {},
        )
    }

    private companion object {
        // Baselines are committed, so they live in the source tree rather than
        // in `build/`, where `verifyRoborazziDebug` would have nothing to compare
        // against on a clean checkout.
        const val SCREENSHOT_DIR = "src/test/screenshots"
        const val LONG_MESSAGE =
            "Albums are stored on your device and encrypted before they leave it. " +
                "You can revoke access at any time from Settings, and revoking access " +
                "deletes every copy we hold within thirty days. Shared albums stay " +
                "visible to the people you shared them with until you remove them " +
                "individually. Nothing is used to train anything."
    }
}
