package com.thecode.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.thecode.aestheticdialogs.components.alert.AestheticAlertDialog
import com.thecode.aestheticdialogs.components.alert.models.AlertDialogSignal
import com.thecode.aestheticdialogs.components.alert.models.AlertDialogUiModel
import com.thecode.aestheticdialogs.components.confirmation.AestheticConfirmationDialog
import com.thecode.aestheticdialogs.components.confirmation.models.ConfirmationDialogSignal
import com.thecode.aestheticdialogs.components.confirmation.models.ConfirmationDialogUiModel
import com.thecode.aestheticdialogs.components.content.AestheticContentDialog
import com.thecode.aestheticdialogs.components.content.models.ContentDialogSignal
import com.thecode.aestheticdialogs.components.content.models.ContentDialogUiModel
import com.thecode.aestheticdialogs.components.feedback.AestheticFeedbackDialog
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogSignal
import com.thecode.aestheticdialogs.components.feedback.models.FeedbackDialogUiModel
import com.thecode.aestheticdialogs.components.input.AestheticInputDialog
import com.thecode.aestheticdialogs.components.input.models.InputDialogSignal
import com.thecode.aestheticdialogs.components.input.models.InputDialogUiModel
import com.thecode.aestheticdialogs.components.notification.AestheticNotificationHost
import com.thecode.aestheticdialogs.components.notification.models.NotificationSignal
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.components.selection.AestheticSelectionDialog
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogSignal
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogActionEmphasis
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private const val LOREM = "Albums are stored on your device and encrypted before they leave it. " +
    "You can revoke access at any time from Settings, and revoking access deletes every copy " +
    "we hold within thirty days. Shared albums stay visible to the people you shared them with " +
    "until you remove them individually. Nothing is used to train anything."

/**
 * The catalog screen.
 *
 * Every dialog below follows the same loop: a value in this composable's state
 * selects a UI model, the component renders it, and the signal comes back here
 * to change the state. No dialog dismisses itself, and none of them owns a
 * decision.
 */
@Composable
fun CatalogScreen(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val data = remember { CatalogData() }

    var activeDemo by remember { mutableStateOf<CatalogDemo?>(null) }
    var tone by remember { mutableStateOf(DialogTone.Success) }
    var banner by remember { mutableStateOf<NotificationUiModel?>(null) }

    // Demo-only state that stands in for what a real screen would keep in its
    // ViewModel: the current selection, the text being typed, whether a request
    // is in flight.
    var selectedLanguage by remember { mutableStateOf("fr") }
    var selectedTopics by remember { mutableStateOf(setOf("en", "pt")) }
    var searchQuery by remember { mutableStateOf("") }
    var albumName by remember { mutableStateOf("Lisbon, spring") }
    var password by remember { mutableStateOf("") }
    var isConfirming by remember { mutableStateOf(false) }

    // A fake request, so the loading state can be seen doing something.
    LaunchedEffect(isConfirming) {
        if (isConfirming) {
            delay(1_500.milliseconds)
            isConfirming = false
            activeDemo = null
            banner = NotificationUiModel.Toaster(
                title = "Album deleted",
                message = "24 photos went with it.",
                tone = DialogTone.Success,
            )
        }
    }

    Box(modifier = modifier.fillMaxSize().background(colors.surface.sunken)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                // The catalog makes the same adaptive promise the library does: a
                // column of rows stretched across a tablet is not a layout.
                .widthIn(max = CONTENT_MAX_WIDTH)
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            contentPadding = PaddingValues(AestheticSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AestheticSpacing.sm),
        ) {
            item {
                CatalogHeader(darkTheme = darkTheme, onDarkThemeChange = onDarkThemeChange)
            }

            item {
                SectionTitle("Tone")
                TonePicker(selected = tone, onSelected = { tone = it })
            }

            item { SectionTitle("Dialogs") }

            items(CatalogDemo.entries.toList()) { demo ->
                CatalogRow(
                    title = demo.label,
                    subtitle = demo.description,
                    onClick = { activeDemo = demo },
                )
            }

            item { SectionTitle("Notifications") }

            items(catalogNotifications(tone)) { (label, model) ->
                CatalogRow(
                    title = label,
                    subtitle = "Auto-dismisses after four seconds.",
                    onClick = { banner = model },
                )
            }
        }

        AestheticNotificationHost(
            notification = banner,
            onSignal = { signal ->
                when (signal) {
                    NotificationSignal.Clicked, NotificationSignal.Dismissed -> banner = null
                }
            },
            autoDismissMillis = 4_000,
        )
    }

    val close = { activeDemo = null }

    when (activeDemo) {
        null -> Unit

        CatalogDemo.ConfirmationDefault -> AestheticConfirmationDialog(
            uiModel = ConfirmationDialogUiModel.Default(
                title = "Leave without saving?",
                message = "Your draft has unsaved changes. They will be lost.",
                confirmLabel = "Leave",
                cancelLabel = "Keep editing",
                tone = tone,
            ),
            onSignal = { close() },
        )

        CatalogDemo.ConfirmationDestructive -> AestheticConfirmationDialog(
            uiModel = ConfirmationDialogUiModel.Destructive(
                title = "Delete this album?",
                message = "The 24 photos inside it will be deleted too. This cannot be undone.",
                confirmLabel = "Delete album",
                cancelLabel = "Keep it",
                isConfirming = isConfirming,
            ),
            onSignal = { signal ->
                when (signal) {
                    ConfirmationDialogSignal.Confirmed -> isConfirming = true
                    ConfirmationDialogSignal.Cancelled,
                    ConfirmationDialogSignal.Dismissed,
                    -> close()
                }
            },
        )

        CatalogDemo.AlertInfo -> AestheticAlertDialog(
            uiModel = AlertDialogUiModel.Default(
                title = "Update available",
                message = "Version 3.4 adds offline albums and fixes the sync stall.",
                tone = tone,
                primaryAction = DialogAction("Update now"),
                secondaryAction = DialogAction("Later", DialogActionEmphasis.Text),
            ),
            onSignal = { close() },
        )

        CatalogDemo.AlertError -> AestheticAlertDialog(
            uiModel = AlertDialogUiModel.Default(
                title = "Upload failed",
                message = "The connection dropped after 12 of 24 photos.",
                tone = DialogTone.Error,
                primaryAction = DialogAction("Retry"),
                secondaryAction = DialogAction("Cancel", DialogActionEmphasis.Secondary),
            ),
            onSignal = { close() },
        )

        CatalogDemo.AlertLongContent -> AestheticAlertDialog(
            uiModel = AlertDialogUiModel.Default(
                title = "How your albums are stored",
                message = LOREM + "\n\n" + LOREM,
                tone = tone,
                primaryAction = DialogAction("Got it"),
                showCloseButton = true,
            ),
            onSignal = { close() },
        )

        CatalogDemo.SelectionSingle -> AestheticSelectionDialog(
            uiModel = SelectionDialogUiModel.Single(
                title = "App language",
                items = data.languages,
                selectedId = selectedLanguage,
                cancelLabel = "Cancel",
            ),
            onSignal = { signal ->
                when (signal) {
                    is SelectionDialogSignal.ItemClicked -> {
                        selectedLanguage = signal.id
                        close()
                    }

                    else -> close()
                }
            },
        )

        CatalogDemo.SelectionMultiple -> AestheticSelectionDialog(
            uiModel = SelectionDialogUiModel.Multiple(
                title = "Notify me about",
                // Filtering is the caller's job, which is why it happens here.
                items = data.languages.filter {
                    it.label.contains(searchQuery, ignoreCase = true)
                },
                selectedIds = selectedTopics,
                confirmLabel = "Save",
                cancelLabel = "Cancel",
                searchQuery = searchQuery,
                emptyText = "Nothing matches “$searchQuery”.",
            ),
            onSignal = { signal ->
                when (signal) {
                    is SelectionDialogSignal.ItemClicked -> {
                        selectedTopics = if (signal.id in selectedTopics) {
                            selectedTopics - signal.id
                        } else {
                            selectedTopics + signal.id
                        }
                    }

                    is SelectionDialogSignal.SearchQueryChanged -> searchQuery = signal.query

                    else -> close()
                }
            },
        )

        CatalogDemo.SelectionEmpty -> AestheticSelectionDialog(
            uiModel = SelectionDialogUiModel.Single(
                title = "App language",
                items = emptyList(),
                selectedId = null,
                cancelLabel = "Cancel",
                searchQuery = "klingon",
                emptyText = "No language matches “klingon”.",
            ),
            onSignal = { close() },
        )

        CatalogDemo.InputText -> AestheticInputDialog(
            uiModel = InputDialogUiModel.Text(
                title = "Rename album",
                value = albumName,
                label = "Album name",
                supportingText = "Visible to anyone you share the album with.",
                errorText = "An album needs a name.".takeIf { albumName.isBlank() },
                confirmLabel = "Rename",
                cancelLabel = "Cancel",
                isConfirmEnabled = albumName.isNotBlank(),
            ),
            onValueChange = { albumName = it },
            onSignal = { close() },
        )

        CatalogDemo.InputPassword -> AestheticInputDialog(
            uiModel = InputDialogUiModel.Password(
                title = "Confirm your password",
                message = "This deletes every backup on this device.",
                value = password,
                label = "Password",
                confirmLabel = "Confirm",
                cancelLabel = "Cancel",
                isConfirmEnabled = password.length >= 8,
            ),
            onValueChange = { password = it },
            onSignal = { signal ->
                when (signal) {
                    InputDialogSignal.Confirmed,
                    InputDialogSignal.Cancelled,
                    InputDialogSignal.Dismissed,
                    -> {
                        password = ""
                        close()
                    }
                }
            },
        )

        CatalogDemo.ContentRich -> AestheticContentDialog(
            uiModel = ContentDialogUiModel.Default(
                title = "Before you continue",
                subtitle = "Three things this app does with your photos.",
                primaryAction = DialogAction("I agree"),
                secondaryAction = DialogAction("Not now", DialogActionEmphasis.Text),
            ),
            onSignal = { close() },
        ) {
            Column(
                modifier = Modifier.padding(horizontal = AestheticSpacing.xxl),
                verticalArrangement = Arrangement.spacedBy(AestheticSpacing.md),
            ) {
                listOf(
                    "Albums are stored on your device only.",
                    "Backups are encrypted before they leave the phone.",
                    "You can delete everything from Settings at any time.",
                ).forEach { line ->
                    Text(
                        text = line,
                        style = AestheticDialogsTheme.typography.message,
                        color = AestheticDialogsTheme.colors.content.secondary,
                    )
                }
            }
        }

        CatalogDemo.FeedbackFlat -> AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Flat(
                title = "Something went wrong",
                message = "We could not reach the server. Try again in a moment.",
                tone = tone,
                actionLabel = "OK",
            ),
            onSignal = { signal ->
                when (signal) {
                    FeedbackDialogSignal.ActionClicked, FeedbackDialogSignal.Dismissed -> close()
                }
            },
        )

        CatalogDemo.FeedbackFlash -> AestheticFeedbackDialog(
            uiModel = FeedbackDialogUiModel.Flash(
                title = "Message sent",
                message = "It will arrive even if you close the app.",
                tone = tone,
                actionLabel = "Nice",
            ),
            onSignal = { close() },
        )
    }
}

@Composable
private fun CatalogHeader(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
) {
    val colors = AestheticDialogsTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AestheticSpacing.lg),
        horizontalArrangement = Arrangement.spacedBy(AestheticSpacing.lg),
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "AestheticDialogs 2.0",
                style = AestheticDialogsTheme.typography.title,
                color = colors.content.primary,
            )
            Text(
                text = "A Compose dialog design system.",
                style = AestheticDialogsTheme.typography.message,
                color = colors.content.secondary,
            )
        }
        DarkThemeSwitch(checked = darkTheme, onCheckedChange = onDarkThemeChange)
    }
}

@Composable
private fun DarkThemeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AestheticDialogsTheme.colors
    val label = "Dark theme"

    // No visible label, so the control needs a name of its own: what a sighted
    // user reads from the position has to reach TalkBack some other way.
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.semantics { contentDescription = label },
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.action.onPrimary,
            checkedTrackColor = colors.action.primary,
            checkedBorderColor = colors.action.primary,
            uncheckedThumbColor = colors.content.muted,
            uncheckedTrackColor = colors.surface.container,
            uncheckedBorderColor = colors.border.default,
        ),
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = AestheticDialogsTheme.typography.caption,
        color = AestheticDialogsTheme.colors.content.muted,
        modifier = Modifier.padding(top = AestheticSpacing.lg, bottom = AestheticSpacing.sm),
    )
}

@Composable
private fun TonePicker(
    selected: DialogTone,
    onSelected: (DialogTone) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(AestheticSpacing.sm)) {
        catalogTones.forEach { tone ->
            Pill(
                label = tone.name,
                selected = tone == selected,
                onClick = { onSelected(tone) },
            )
        }
    }
}

@Composable
private fun Pill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = AestheticDialogsTheme.colors
    Surface(
        onClick = onClick,
        shape = AestheticDialogsTheme.shapes.button,
        color = if (selected) colors.action.primary else colors.surface.container,
        contentColor = if (selected) colors.action.onPrimary else colors.content.secondary,
    ) {
        Text(
            text = label,
            style = AestheticDialogsTheme.typography.caption,
            modifier = Modifier.padding(
                horizontal = AestheticSpacing.md,
                vertical = AestheticSpacing.sm,
            ),
        )
    }
}

@Composable
private fun CatalogRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    val colors = AestheticDialogsTheme.colors
    Surface(
        shape = AestheticDialogsTheme.shapes.control,
        color = colors.surface.container,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(AestheticSpacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.padding(end = 8.dp)) {
                Text(
                    text = title,
                    style = AestheticDialogsTheme.typography.itemLabel,
                    color = colors.content.primary,
                )
                Text(
                    text = subtitle,
                    style = AestheticDialogsTheme.typography.supporting,
                    color = colors.content.muted,
                )
            }
        }
    }
}

private val CONTENT_MAX_WIDTH = 640.dp
