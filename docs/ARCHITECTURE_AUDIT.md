# AestheticDialogs 1.x — architecture audit

The audit that preceded the 2.0 rebuild. It records what 1.3.8 actually did,
what was wrong with it, and what each piece became. Kept in the repository
because "why is it like this now" is a question that outlives everyone who was
in the room.

## 1. What 1.x was

One file, 717 lines: `AestheticDialog.kt`. Inside it, a `Builder` class with a
`show()` method containing a single `when (dialogStyle)` over eight branches.
Each branch inflated an XML layout, bound it with ViewBinding, applied colours
with `ContextCompat.getColor`, created an `AlertDialog`, and then reached into
`alertDialog.window` to set gravity, background and size.

Supporting cast:

| Kind | Count | Notes |
|---|---|---|
| Public Kotlin types | 6 | `AestheticDialog`, `Builder`, `DialogStyle`, `DialogType`, `DialogAnimation`, `OnDialogClickListener` |
| XML layouts | 10 | one per style, two for the styles that split success/error |
| Drawables | 44 | 34 XML shapes/selectors, 10 PNG bitmaps |
| Animation XML | 30 | 15 enter/exit pairs for 16 animation values |
| Styles | 16 | one window-animation style per animation |
| Colour resources | 13 | including four unused `*_darker` variants |
| Dimension resources | 16 | fixed dialog widths and heights in `dp` |
| Tests | 2 | the Android Studio templates, asserting `2 + 2 == 4` |

Dependencies: `appcompat`, `cardview`, `core-ktx`. `minSdk 19`, `compileSdk 34`,
Groovy Gradle, no version catalog, no publishing configuration in the repository
(distribution was JitPack), no CI, no lint configuration, no ProGuard rules
beyond the template comments.

## 2. Component-by-component

### 2.1 `DialogStyle.FLAT`

| | |
|---|---|
| **Responsibility** | Modal status card with an icon, title, message and one button. |
| **Public API** | `AestheticDialog.Builder(activity, FLAT, type)` + eight setters. |
| **State** | None. Everything is passed at construction and never changes. |
| **Visual implementation** | `dialog_flat.xml` (fixed 300×290dp) + four `rounded_rect_*` drawables + four button selectors, chosen by a `when (dialogType)`. |
| **Dependencies** | `Activity`, `AlertDialog`, ViewBinding, `ContextCompat`. |
| **Problems** | Fixed 300×290dp: content longer than three lines is silently ellipsized, and the dialog is the same size on a phone and a tablet. Dark mode is a boolean that repaints four views by hand — and repeats the `setText`/`setOnClickListener` calls already made ten lines above. Button colour is duplicated across four drawable files that differ only in a colour reference. Title colour comes from the status hue on white, which fails contrast for warning. |
| **2.0** | `FeedbackDialogUiModel.Flat` → `FeedbackDialogFlat` variant → `DialogFramePrimitive` + `StatusBadgePrimitive` + `DialogActionRow`. Size is adaptive, dark mode is a theme, the four drawables are one `Tone` token lookup. |

### 2.2 `DialogStyle.FLASH`

| | |
|---|---|
| **Responsibility** | Modal status card on a gradient, with an outlined button. |
| **Public API** | Same builder. |
| **Visual implementation** | `dialog_flash.xml` + two gradient drawables (success and error only). |
| **Problems** | Only two of the four `DialogType` values have a gradient; passing `WARNING` silently rendered the error gradient (the branch was `if (type == SUCCESS) … else …`). Gradients are hard-coded hexes unrelated to the colour resources, so rebranding means editing XML. |
| **2.0** | `FeedbackDialogUiModel.Flash`. The gradient is derived from the tone accent, so all five tones work and a rebranded theme stays consistent. |

### 2.3 `DialogStyle.TOASTER`

| | |
|---|---|
| **Responsibility** | Edge-anchored banner: accent bar, icon, title, message, close. |
| **Visual implementation** | `dialog_toaster.xml`, fixed 100dp height, shown as an `AlertDialog` with `Gravity.TOP`. |
| **Problems** | It is not a dialog. Rendering it as one means it dims the screen, takes focus, blocks touches on the content behind it and consumes the back gesture — for an informational toast. The title size is `18dp`, not `sp`, so it ignores the user's font size setting entirely. |
| **2.0** | `NotificationUiModel.Toaster`, rendered by `BannerPrimitive` inside `AestheticNotificationHost`. Not modal, announced as a live region, type in `sp`. |

### 2.4 `DialogStyle.CONNECTIFY`

| | |
|---|---|
| **Responsibility** | Connectivity banner with a gradient strip. |
| **Visual implementation** | Two near-identical layouts (`dialog_connectify_success.xml`, `..._error.xml`) differing only in gradient and text colour, plus a manual `AppCompatImageView`/`AppCompatTextView` variable dance in `show()` to unify them. |
| **Problems** | The clearest duplication in the library: two layouts, two gradient drawables and fifteen lines of glue to paper over a single colour difference. Only success and error exist. |
| **2.0** | `NotificationUiModel.Connectify`, one variant, gradient from the tone. |

### 2.5 `DialogStyle.RAINBOW`

| | |
|---|---|
| **Responsibility** | Solid tone-filled banner. |
| **Problems** | Same modality problem as Toaster. Fixed 100dp height with two-line messages. |
| **2.0** | `NotificationUiModel.Rainbow`. |

### 2.6 `DialogStyle.EMOJI`

| | |
|---|---|
| **Responsibility** | Banner with a large emoji. |
| **Visual implementation** | `thumbs_up_sign.png` and `man_shrugging.png`, shipped at a single density. |
| **Problems** | Bitmaps for glyphs the platform already renders as text: they blur when scaled, add weight to every consumer's APK, cannot follow the system emoji style and cannot be changed by the caller. Only two emoji exist, hard-wired to success and error. |
| **2.0** | `NotificationUiModel.Emoji` takes the character. The library supplies a tone default; the caller can pass any emoji. Zero assets. |

### 2.7 `DialogStyle.EMOTION`

| | |
|---|---|
| **Responsibility** | Wide card with an avatar, title, message and a timestamp. |
| **Visual implementation** | `dialog_emotion.xml` inside a `CardView`, with `background_emotion_success.png` / `background_emotion_error.png` as background bitmaps. |
| **Problems** | The dialog called `SimpleDateFormat("HH:mm")` on `Calendar.getInstance()` **inside the view code**. That ignores the user's 12/24-hour preference, ignores their locale, uses the default time zone, allocates a formatter on every show, and makes the component impossible to screenshot-test because its output changes every minute. Background bitmaps mean the card cannot resize cleanly. |
| **2.0** | `NotificationUiModel.Emotion` takes a preformatted `timestamp: String?`. Formatting a time is a product decision. Background is a derived gradient. |

### 2.8 `DialogStyle.DRAKE`

| | |
|---|---|
| **Responsibility** | Meme dialog: a two-panel Drake reaction image with success/error text. |
| **Visual implementation** | `drake_success.png`, `drake_error.png`. |
| **Problems** | The images are frames from a copyrighted music video redistributed inside an Apache-2.0 library. Beyond licensing: the text is baked into the bitmap, so it cannot be localised, cannot be read by a screen reader, and cannot honour the `setTitle`/`setMessage` the builder accepted (this style ignored both). |
| **2.0** | **Removed.** Documented in `MIGRATION.md`. `FeedbackDialogUiModel.Flat` is the suggested replacement. |

### 2.9 `DialogAnimation` (16 values)

| | |
|---|---|
| **Implementation** | 30 `res/anim` XML files and 16 `<style>` entries setting `windowEnterAnimation` / `windowExitAnimation`. |
| **Problems** | `SPIN`, `WINDMILL`, `SPLIT` and `DIAGONAL` are rotation-heavy transitions with no way to reduce them, which is exactly what the "remove animations" accessibility setting exists to prevent. Sixteen options is a menu, not a design system — nothing in the API suggested which one to use, and the sample application picked a different one per button. |
| **2.0** | Five `AestheticNotificationAnimation` values for banners, one enter transition for modal dialogs, all routed through `AestheticMotion` and all collapsing to an instant cut when the platform animation scale is zero. |

### 2.10 `Builder` itself

| Problem | Detail |
|---|---|
| **`setDuration` schedules against an uninitialised field** | `setDuration` posts `{ dismiss() }` to a `Handler` *before* `show()` has assigned `alertDialog`. Calling `.setDuration(n)` and then never calling `.show()`, or a duration shorter than the time to reach `show()`, throws `UninitializedPropertyAccessException` on the main thread. |
| **`dismiss()` returns a new, unrelated instance** | `fun dismiss(): AestheticDialog = AestheticDialog()` — it constructs a fresh empty object and discards the receiver's identity. So does `show()`. The returned handle cannot dismiss anything. |
| **Activity leak** | The `Builder` holds an `Activity` and is handed to the click listener, so any listener kept beyond the dialog keeps the activity alive. |
| **No lifecycle awareness** | Nothing cancels the `Handler` callback when the activity is destroyed. |
| **`@Keep` on the whole class** | Prevents R8 from removing any of it from a consumer's app, even the seven styles they do not use. |
| **Placeholder defaults** | `title = "Title"`, `message = "Message"` — a caller who forgets a field ships the placeholder. |
| **Type/style combinations that do nothing** | `DRAKE` ignores title and message. `FLASH`, `EMOTION` and `DRAKE` ignore `WARNING` and `INFO`. Nothing in the API says so. |
| **Dark mode as a boolean** | `setDarkMode(true)` repaints a hand-picked subset of views. Four of the eight styles ignore it. It does not follow the system setting. |
| **2.0** | Stateless composables. No handle, no lifecycle, nothing to leak. Duration belongs to the banner host. Dark mode is a theme. Every tone works with every variant. |

## 3. Cross-cutting findings

**Duplication.** The `when (dialogType)` block that maps a status to a colour and
an icon appears five times, with small differences each time. The
`alertDialog.apply { show(); window?.apply { … } }` block appears eight times. Two
layouts exist purely because a gradient differs. In 2.0 those are one `Tone`
lookup, one `DialogFramePrimitive` and one variant.

**Coupling.** Every dialog depended on `Activity` (for `layoutInflater` and
`resources`), on ViewBinding, and on the concrete `AlertDialog`. Nothing could be
rendered, previewed or tested without an activity.

**State ownership.** Ambiguous by construction: the builder holds mutable fields,
the dialog holds window state, the caller holds a handle that does not work. In
2.0 the caller owns whether a dialog exists and what it says; the library owns
how it looks.

**Accessibility.** No `paneTitle`, so screen readers did not announce the dialog.
No content descriptions on the close icons. Close targets were 30dp, below the
48dp minimum. Text sizes in `dp` in two layouts. Status colour used for title
text, failing 4.5:1 for warning on white. No live regions on the banner styles.
No keyboard or focus handling anywhere.

**Performance.** Not a hot path, so the real costs were allocation-per-show
(`SimpleDateFormat`, `Calendar`, inflation of a full view tree) and APK weight:
44 drawables (10 of them bitmaps) and 30 animation files shipped to every consumer whether
or not they used a single one of them, with `@Keep` preventing R8 from removing
the code.

**Legacy assumptions.** `minSdk 19`; `enableJetifier=true`; a fixed 300dp width
that assumes a phone; `Gravity.TOP` banners that assume no display cutout;
`setLayout(WRAP_CONTENT, fixedDp)` that assumes no font scaling.

## 4. Feature matrix

| Feature | 1.x | Rebuilt | New | Priority | Public API |
|---|:---:|:---:|:---:|---|:---:|
| Flat dialog | ✓ | ✓ | | High | ✓ |
| Flash dialog | ✓ | ✓ | | High | ✓ |
| Toaster banner | ✓ | ✓ | | High | ✓ |
| Connectify banner | ✓ | ✓ | | High | ✓ |
| Rainbow banner | ✓ | ✓ | | Medium | ✓ |
| Emoji banner | ✓ | ✓ | | Medium | ✓ |
| Emotion banner | ✓ | ✓ | | Medium | ✓ |
| Drake dialog | ✓ | — | | — | — |
| Confirmation dialog | | ✓ | ✓ | High | ✓ |
| Alert dialog | | ✓ | ✓ | High | ✓ |
| Selection dialog | | ✓ | ✓ | High | ✓ |
| Rich content dialog | | ✓ | ✓ | Medium | ✓ |
| Input dialog | | ✓ | ✓ | Medium | ✓ |
| Notification host | | ✓ | ✓ | High | ✓ |
| Token system | Partial | ✓ | ✓ | High | ✓ |
| Theme (light/dark/brand) | Partial | ✓ | ✓ | High | ✓ |
| Motion system | ✓ (16 window anims) | ✓ (5 + reduced motion) | | Medium | ✓ |
| Adaptive layout | — | ✓ | ✓ | High | — |
| Loading states | — | ✓ | ✓ | Medium | ✓ |
| Empty / error patterns | — | ✓ | ✓ | Medium | ✓ |
| Accessibility | Partial | ✓ | ✓ | Critical | — |
| Previews | — | ✓ | ✓ | Medium | — |
| Unit + interaction tests | — | ✓ | ✓ | High | — |
| Screenshot tests | — | ✓ | ✓ | High | — |
| API compatibility tracking | — | ✓ | ✓ | High | — |
| Maven Central publishing | — | ✓ | ✓ | High | — |
| CI | — | planned | ✓ | High | — |

"Rebuilt" means the capability exists in 2.0 with a new implementation and a new
API, not that the old code was ported. "Planned" means the decision is made and
the work is not: there is no `.github/workflows` yet, and the row stays in the
matrix so the gap is visible rather than forgotten.

## 5. What did not change

The visual identity. The status hues are the 1.x hues, the generous corner radius
is the 1.x radius, the Flat and Flash silhouettes are recognisably the same
dialogs, and the five banner shapes are the five banner shapes. A library called
AestheticDialogs earns its name from how it looks; the rebuild is about
everything underneath.
