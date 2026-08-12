package com.thecode.sample

import com.thecode.aestheticdialogs.components.notification.models.NotificationAction
import com.thecode.aestheticdialogs.components.notification.models.NotificationPresence
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.components.selection.models.SelectionItem
import com.thecode.aestheticdialogs.foundation.DialogTone

/**
 * Every dialog the catalog can open.
 *
 * The catalog holds one of these plus a little accompanying state, which is the
 * whole point of the demonstration: what is on screen is a value in the caller's
 * state, and the dialogs are pure functions of it.
 */
enum class CatalogDemo(val label: String, val description: String) {
    ConfirmationDefault(
        label = "Confirmation",
        description = "A question with two answers.",
    ),
    ConfirmationDestructive(
        label = "Confirmation · destructive",
        description = "Error tone, guaranteed treatment, loading confirm.",
    ),
    AlertInfo(
        label = "Alert · info",
        description = "Two actions, informational tone.",
    ),
    AlertError(
        label = "Alert · error",
        description = "Retry and cancel.",
    ),
    AlertLongContent(
        label = "Alert · long content",
        description = "Content scrolls, actions stay pinned.",
    ),
    SelectionSingle(
        label = "Selection · single",
        description = "Radio rows, commit on tap.",
    ),
    SelectionMultiple(
        label = "Selection · multiple",
        description = "Checkboxes, search, disabled rows, confirm.",
    ),
    SelectionEmpty(
        label = "Selection · empty",
        description = "Nothing matched the query.",
    ),
    InputText(
        label = "Input · text",
        description = "Focus on open, validation, done action.",
    ),
    InputPassword(
        label = "Input · password",
        description = "Masked entry with a reveal toggle.",
    ),
    ContentRich(
        label = "Rich content",
        description = "Your content, our frame.",
    ),
    FeedbackDefault(
        label = "Feedback · default",
        description = "Quiet card, tone mark, one action.",
    ),
    FeedbackGradient(
        label = "Feedback · gradient",
        description = "Gradient panel, inverted copy.",
    ),
    FeedbackCompact(
        label = "Feedback · compact",
        description = "One line, one action, 72dp.",
    ),
    Sheet(
        label = "Sheet",
        description = "Docked to the bottom edge, drag it away.",
    ),
    Header(
        label = "Header",
        description = "A band above the title.",
    ),
    ProgressDefault(
        label = "Progress · indeterminate",
        description = "No way out but the cancel action.",
    ),
    ProgressDeterminate(
        label = "Progress · determinate",
        description = "A count the caller owns.",
    ),
}

/** The tones the catalog cycles through, so every dialog can be seen in all five. */
val catalogTones: List<DialogTone> = DialogTone.entries.toList()

/**
 * Demo data for the selection dialogs.
 *
 * Not annotated `@Immutable`: it holds a `List`, and the library's own rule is
 * that the annotation is a promise, not a decoration.
 */
data class CatalogData(
    val languages: List<SelectionItem> = listOf(
        SelectionItem("en", "English", "United Kingdom"),
        SelectionItem("fr", "Français", "France"),
        SelectionItem("pt", "Português", "Brasil"),
        SelectionItem("de", "Deutsch", "Deutschland"),
        SelectionItem("sw", "Kiswahili", "Coming soon", enabled = false),
        SelectionItem("ja", "日本語", "日本"),
        SelectionItem("ar", "العربية", "مصر"),
    ),
)

/** The banners the catalog can raise, in the order they are listed. */
fun catalogNotifications(tone: DialogTone): List<Pair<String, NotificationUiModel>> = listOf(
    "Default" to NotificationUiModel.Default(
        title = "Saved",
        message = "Your changes are on every device.",
        tone = tone,
    ),
    "Filled" to NotificationUiModel.Filled(
        title = "Heads up",
        message = "Two invoices are due this week.",
        tone = tone,
    ),
    "With an action" to NotificationUiModel.Default(
        title = "Album archived",
        message = "24 photos moved out of your library.",
        tone = tone,
        action = NotificationAction("Undo"),
    ),
    "Progress" to NotificationUiModel.Default(
        title = "Uploading 12 of 24",
        message = "You can keep using the app.",
        tone = tone,
        progress = 0.5f,
    ),
    "From a person" to NotificationUiModel.Gradient(
        title = "Amara sent a photo",
        message = "Tap to open the album",
        tone = tone,
        timestamp = "13:56",
        presence = NotificationPresence.Online,
    ),
    "Status strip" to NotificationUiModel.Strip(
        title = "You are offline",
        message = "Showing the last version we downloaded.",
        tone = tone,
        // A strip never auto-dismisses, and the catalog has no connection to come
        // back: without the cross it would stay up for the rest of the session.
        showCloseButton = true,
    ),
    "Ambient" to NotificationUiModel.Ambient(
        title = "Back online",
        message = "Syncing what you missed.",
        tone = tone,
    ),
    "Emoji" to NotificationUiModel.Default(
        title = "Nice one",
        message = "That was your tenth album this month.",
        tone = tone,
        emoji = "\uD83D\uDC4D",
    ),
    "Gradient" to NotificationUiModel.Gradient(
        title = "Amara sent a photo",
        message = "Tap to open the album",
        timestamp = "13:56",
        tone = tone,
    ),
)
