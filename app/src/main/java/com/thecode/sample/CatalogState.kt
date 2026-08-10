package com.thecode.sample

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
    FeedbackFlat(
        label = "Feedback · flat",
        description = "Quiet card, tone mark, one action.",
    ),
    FeedbackFlash(
        label = "Feedback · flash",
        description = "Gradient panel, inverted copy.",
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

/** The banner variants the catalog can raise, in the order they are listed. */
fun catalogNotifications(tone: DialogTone): List<Pair<String, NotificationUiModel>> = listOf(
    "Toaster" to NotificationUiModel.Toaster(
        title = "Saved",
        message = "Your changes are on every device.",
        tone = tone,
    ),
    "Rainbow" to NotificationUiModel.Rainbow(
        title = "Heads up",
        message = "Two invoices are due this week.",
        tone = tone,
    ),
    "Connectify" to NotificationUiModel.Connectify(
        title = "Back online",
        message = "Syncing what you missed.",
        tone = tone,
    ),
    "Emoji" to NotificationUiModel.Emoji(
        title = "Nice one",
        message = "That was your tenth album this month.",
        tone = tone,
    ),
    "Emotion" to NotificationUiModel.Emotion(
        title = "Amara sent a photo",
        message = "Tap to open the album",
        timestamp = "13:56",
        tone = tone,
    ),
)
