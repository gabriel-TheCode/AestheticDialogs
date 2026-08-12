# Migrating from AestheticDialogs 1.x to 2.0

2.0 is a rewrite in Jetpack Compose. There is no compatibility layer and no
deprecation path: the 1.x API took an `Activity`, inflated XML and returned a
handle, none of which has a meaning in a composable. Version 2.0.0 is a major
version because every call site changes.

**1.x keeps working.** If you are not on Compose yet, stay on
`com.github.gabriel-TheCode:AestheticDialogs:1.3.8`. It is not being developed
further, but nothing about 2.0 breaks it.

## The shape of the change

```kotlin
// 1.x — imperative, activity-bound, self-dismissing
AestheticDialog.Builder(this, DialogStyle.FLAT, DialogType.SUCCESS)
    .setTitle("Success")
    .setMessage("The message was sent")
    .setCancelable(false)
    .setDarkMode(true)
    .setAnimation(DialogAnimation.SHRINK)
    .setOnClickListener(object : OnDialogClickListener {
        override fun onClick(dialog: AestheticDialog.Builder) { dialog.dismiss() }
    })
    .show()
```

```kotlin
// 2.0 — declarative, state-driven, dismissed by you
if (uiState.showSuccess) {
    AestheticFeedbackDialog(
        uiModel = FeedbackDialogUiModel.Default(
            title = "Success",
            message = "The message was sent",
            tone = DialogTone.Success,
            actionLabel = "OK",
            dismissBehavior = DialogDismissBehavior.Blocking,
        ),
        onDismiss = { viewModel.dismissSuccess() },
    )
}
```

Three things moved:

- **Visibility is yours.** There is no `show()` and no `dismiss()`. The dialog
  exists when it is in the composition. Every signal — including `Dismissed` — is
  a *request*, and nothing happens until you act on it.
- **Dark mode is a theme.** `setDarkMode(true)` is gone. Wrap your app in
  `AestheticDialogsTheme` once and the whole library follows the system setting,
  or force it with `AestheticDialogsTheme(darkTheme = true)`.
- **Banners are not dialogs.** The five edge-anchored styles no longer open a
  window. See [Notifications](#notifications) below.

## Dependency

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.gabrielthecode:aestheticdialogs:2.0.0")
}
```

Published to Maven Central rather than JitPack, so the `maven { url = "https://jitpack.io" }`
line can go. `minSdk` moves from 19 to 24.

## Style-by-style

| 1.x | 2.0 |
|---|---|
| `DialogStyle.FLAT` | `FeedbackDialogUiModel.Flat` → `AestheticFeedbackDialog` |
| `DialogStyle.FLASH` | `FeedbackDialogUiModel.Gradient` → `AestheticFeedbackDialog` |
| `DialogStyle.TOASTER` | `NotificationUiModel.Default` → `AestheticNotificationHost` |
| `DialogStyle.RAINBOW` | `NotificationUiModel.Filled` → `AestheticNotificationHost` |
| `DialogStyle.CONNECTIFY` | `NotificationUiModel.Ambient` → `AestheticNotificationHost` |
| `DialogStyle.EMOJI` | `NotificationUiModel.Default(emoji = …)` → `AestheticNotificationHost` |
| `DialogStyle.EMOTION` | `NotificationUiModel.Gradient(timestamp = …)` → `AestheticNotificationHost` |
| `DialogStyle.DRAKE` | **Removed.** See below. |

### Why `DRAKE` is gone

It shipped two bitmaps that are frames from a copyrighted music video,
redistributed inside an Apache-2.0 library. That alone settles it. On top of
that, the text was baked into the images, so the style ignored the `setTitle` and
`setMessage` it accepted, could not be localised and could not be read aloud.

If you were using it, `FeedbackDialogUiModel.Default` is the closest replacement —
same modal card, same status tone, with text you control.

## Property-by-property

| 1.x | 2.0 |
|---|---|
| `.setTitle(s)` | `title = s` on the UI model |
| `.setMessage(s)` | `message = s` |
| `.setDarkMode(b)` | `AestheticDialogsTheme(darkTheme = b)` around your app |
| `.setCancelable(false)` | `dismissBehavior = DialogDismissBehavior.Blocking` |
| `.setCancelable(true)` | the default |
| `.setGravity(g)` | removed for dialogs (they centre, adaptively); `alignment` on `AestheticNotificationHost` for banners |
| `.setDuration(ms)` | `autoDismissMillis` on `AestheticNotificationHost` |
| `.setAnimation(a)` | `animation` on `AestheticNotificationHost`; modal dialogs have one enter transition |
| `.setOnClickListener { }` | one callback per action: `onConfirm`, `onCancel`, `onDismiss` |
| `.show()` / `.dismiss()` | your own state |
| `DialogType.SUCCESS/ERROR/WARNING/INFO` | `DialogTone.Success/Error/Warning/Info` (plus `Neutral`) |

`setCancelable` split into two flags, because it conflated the back gesture with
tapping outside:

```kotlin
DialogDismissBehavior(dismissOnBackPress = true, dismissOnClickOutside = false)
```

If you block dismissal, give the dialog a visible way out. A blocking dialog with
no action is a trap.

### Animations

1.x had sixteen. 2.0 has five, on banners only:
`Slide`, `Fade`, `Scale`, `SlideHorizontal`, `None`.

`SPIN`, `WINDMILL`, `SPLIT` and `DIAGONAL` are gone. They were rotation-heavy
transitions with no way to reduce them, which is what the system "remove
animations" setting exists to prevent. Everything in 2.0 collapses to an instant
cut when that setting is on, automatically.

## Notifications

The five banner styles used to be modal windows. Now they are composables you
place over your content:

```kotlin
Box(Modifier.fillMaxSize()) {
    HomeContent()

    AestheticNotificationHost(
        notification = uiState.banner,          // NotificationUiModel?
        onDismiss = { viewModel.dismissBanner() },
        alignment = NotificationAlignment.Top,
        autoDismissMillis = 4_000,
    )
}
```

Setting `uiState.banner` to `null` dismisses it, with an exit animation. Your
screen keeps its focus, its back gesture and its touch input the whole time — all
three of which 1.x took away for an informational toast.

### `EMOTION` needs a timestamp now

1.x called `SimpleDateFormat("HH:mm")` inside the dialog, which ignored the user's
12/24-hour preference and their locale. Format it yourself:

```kotlin
NotificationUiModel.Gradient(
    title = "Amara sent a photo",
    timestamp = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(locale)
        .format(sentAt),
)
```

### `EMOJI` takes a character

```kotlin
NotificationUiModel.Emoji(title = "Nice one", emoji = "🎉")
```

Leave `emoji` out and the library picks one from the tone.

## What you gain by migrating

Five dialog patterns that did not exist in 1.x — confirmation, alert, selection,
rich content and input — all sharing the frame, the theme and the accessibility
work with the two you already used. Plus adaptive width, real dark mode, reduced
motion support, 48dp touch targets, screen-reader announcements, and no bitmap
assets in your APK.

## Things to check after migrating

- **Blocking dialogs.** Anywhere you had `.setCancelable(false)`, confirm there is
  a visible action.
- **Loading.** `isConfirming = true` on a confirmation or an input dialog shows a
  spinner on the confirm button and locks the rest of the dialog. If you were
  disabling buttons by hand, delete that.
- **Long content.** Fixed 300×290dp dialogs silently ellipsized; 2.0 scrolls
  instead, so text you never saw in 1.x will start appearing.
- **Large screens.** Dialogs are no longer 300dp wide everywhere.

---

# Migrating from 2.0 to 3.0

3.0 changes the shape of every component's API and nothing else. No colour, no
layout, no token moved — the screenshot baselines from 2.0 pass unchanged.

## One callback per interaction

2.0 gave every component a single `onSignal` taking a sealed type, handled with
an exhaustive `when`. 3.0 gives it one named callback per interaction.

```kotlin
// 2.0
AestheticConfirmationDialog(
    uiModel = ConfirmationDialogUiModel.Destructive(…),
    onSignal = { signal ->
        when (signal) {
            ConfirmationDialogSignal.Confirmed -> viewModel.deleteAlbum()
            ConfirmationDialogSignal.Cancelled,
            ConfirmationDialogSignal.Dismissed -> viewModel.dismissDialog()
        }
    },
)

// 3.0
AestheticConfirmationDialog(
    uiModel = ConfirmationDialogUiModel.Destructive(…),
    onConfirm = { viewModel.deleteAlbum() },
    onCancel = { viewModel.dismissDialog() },
)
```

**Why.** In a published library, adding an interaction to a sealed signal type
stops every consumer's `when` from compiling. The same addition as a parameter
with a default costs them nothing. Exhaustiveness was the argument for signals,
and a parameter without a default buys the same guarantee — which is why the
callbacks that represent a real decision have no default.

The `…Signal` types are **removed**, not deprecated. They have no meaning left
once the callbacks exist, and keeping a second way to say the same thing in a
design system is how two dialects start.

## The new signatures

| Component | 3.0 |
|---|---|
| `AestheticConfirmationDialog` | `uiModel, onConfirm, onCancel, modifier, onDismiss = onCancel` |
| `AestheticAlertDialog` | `uiModel, onPrimaryAction, onDismiss, modifier, onSecondaryAction = {}` |
| `AestheticContentDialog` | `uiModel, onDismiss, modifier, onPrimaryAction = {}, onSecondaryAction = {}, content` |
| `AestheticFeedbackDialog` | `uiModel, onDismiss, modifier, onAction = onDismiss` |
| `AestheticInputDialog` | `uiModel, onValueChange, onConfirm, onCancel, modifier, onDismiss = onCancel` |
| `AestheticSelectionDialog` | `uiModel, onItemClick, onCancel, modifier, onSearchQueryChange = {}, onConfirm = {}, onDismiss = onCancel` |
| `AestheticNotification` / `AestheticNotificationHost` | `uiModel, onDismiss, modifier, onClick = {}` |

## Which callbacks carry a default, and why

`onDismiss` falls through to the way back **when the model guarantees there is
one**. A confirmation, an input and a selection all take a mandatory
`cancelLabel`, so a scrim tap defaults to the same outcome as pressing it. An
alert and a content dialog have an *optional* second action, so their
`onDismiss` is required: a back gesture that reaches nobody is how a dialog
becomes impossible to close.

`onAction` on a feedback dialog defaults to `onDismiss`, because its single
button almost always means "I have read it".

## Signals that carried a payload

`SelectionDialogSignal.ItemClicked(id)` and `SearchQueryChanged(query)` are now
ordinary parameters — no wrapper to allocate, no `is` check at the call site:

```kotlin
onItemClick = { id -> viewModel.toggle(id) },
onSearchQueryChange = { viewModel.search(it) },
```

## Banner variants renamed, content became fields

Five banner variants become four, and two of the five were never visual
configurations at all — they were a card with different *content*.

| 2.0 | 3.0 |
|---|---|
| `NotificationUiModel.Toaster` | `Default` |
| `NotificationUiModel.Rainbow` | `Filled` |
| `NotificationUiModel.Connectify` | `Ambient` |
| `NotificationUiModel.Emotion` | `Gradient(timestamp = "13:56")` |
| `NotificationUiModel.Emoji` | `Default(emoji = "👍")` |
| `FeedbackDialogUiModel.Flat` | `FeedbackDialogUiModel.Default` |
| `FeedbackDialogUiModel.Flash` | `FeedbackDialogUiModel.Gradient` |

Every one of them renders exactly what it rendered in 2.0 — the screenshot
baselines pass unchanged. Only the names moved, and `Emoji` gained an explicit
character where 2.0 derived one from the tone.

## Banners gained the seven things the model could not say

```kotlin
AestheticNotificationHost(
    notification = uiState.banner,
    onDismiss = { viewModel.dismiss() },
    onAction = { viewModel.undo() },              // new: a trailing action
    queuePolicy = NotificationQueuePolicy.Enqueue, // new: Replace (default), Enqueue, Drop
    swipeToDismiss = true,                         // new, on by default
    showCountdown = true,                          // new, on by default
    leading = { Avatar(uiState.sender) },          // new: an avatar in the leading slot
)
```

- **`NotificationAction`** on the model, with `onAction` on the component and the
  host. `Undo` is the most common banner behaviour on Android and 2.0 could not
  express it.
- **`NotificationUiModel.Strip`** — docked, square, no shadow, ignores
  `autoDismissMillis`, and insets its own copy out of the system bars it paints
  under. For a *condition* rather than an event; it has no close affordance
  unless you ask for one with `showCloseButton = true`.
- **`progress`** draws a determinate bar bonded to the bottom edge, for work that
  does not deserve a modal.
- **`presence`** draws an availability dot over the leading slot.
- **`swipeToDismiss`** and **`showCountdown`** are on by default. Both change what
  the user sees; pass `false` if you have a reason.

## Things to check after migrating to 3.0

- Every `onSignal = { … }` and every `when` over a `…Signal` type: the compiler
  finds all of them for you.
- Places where you handled `Cancelled` and `Dismissed` in one branch: you can now
  drop `onDismiss` and let it default — but only where the difference genuinely
  does not matter to you.
- Nothing else. If a screenshot of your dialogs changes between 2.0 and 3.0,
  that is a bug in this library, not in your migration.

