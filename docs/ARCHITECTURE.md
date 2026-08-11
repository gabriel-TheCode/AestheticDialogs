# AestheticDialogs 2.0 — architecture

AestheticDialogs is a dialog design system built on the three-layer UI
architecture: **Component → Variant → Primitive → Tokens**. This document
explains what each layer owns, why the boundaries sit where they do, and which
decisions were made deliberately against the obvious alternative.

## 1. The layers

```
              ┌─────────────────────────────────────────┐
  your app    │  state  →  UI model  →  Component       │   public
              └───────────────────────────┬─────────────┘
                                          │  signals ↑
              ┌───────────────────────────▼─────────────┐
   library    │  Variant   (internal)                   │
              │  Primitive (internal)                   │
              │  Tokens / Theme                         │   public
              └─────────────────────────────────────────┘
```

### Component — the public API

One per dialog family, named `Aesthetic` + what it is:
`AestheticConfirmationDialog`, `AestheticAlertDialog`, `AestheticSelectionDialog`,
`AestheticContentDialog`, `AestheticInputDialog`, `AestheticFeedbackDialog`,
`AestheticNotification` / `AestheticNotificationHost`.

A component does three things and nothing else:

1. accepts a UI model,
2. dispatches exhaustively on it to pick a variant,
3. passes callbacks through.

Every component body in this library is a single `when`. If one grows a
condition, that condition belongs in a variant.

> **Naming note.** The article's convention is `DesignSystemName` +
> `ComponentName`, which would give `AestheticDialogsConfirmationDialog`. The
> prefix is shortened to `Aesthetic` because the full name stutters against the
> word "Dialog" that every component already ends with. The rule is kept, the
> stutter is not.

### Variant — the visual forms

Internal. A variant receives one subclass of a UI model, resolves its semantics
(a `DialogTone`, a `DialogActionEmphasis`) into raw values, and composes
primitives. Examples: `ConfirmationDialogDestructive`, `FeedbackDialogFlash`,
`SelectionDialogMultiple`.

Colour resolution lives beside the variants — `DialogActionRow`, `ToneStyling` —
never in the models and never in the primitives. That is what makes "the confirm
button is green in a success dialog" one rule instead of six.

### Primitive — the visual building blocks

Internal, stateless, raw parameters only: `Color`, `Dp`, `Shape`, `String`,
lambdas. A primitive never sees a UI model and never knows what it is rendering.

The shared ones are the heart of the library:

| Primitive | Owns |
|---|---|
| `DialogFramePrimitive` | The window, the scrim, the adaptive width, the enter transition, the accessibility pane, and the "header and actions stay, content scrolls" layout |
| `BannerPrimitive` | The non-modal banner surface and its live region |
| `DialogButtonPrimitive` / `DialogActionsPrimitive` | Buttons and the action row |
| `DialogHeaderPrimitive` / `DialogMessagePrimitive` | Title, mark, close affordance, body copy |
| `StatusBadgePrimitive` / `AestheticGlyph` | The drawn marks |
| `SelectionRowPrimitive` / `TextFieldPrimitive` | List rows and text entry |

**Where this deviates from the reference architecture:** the proof of concept
puts primitives under each component's own folder. Dialogs are different — the
whole point of a dialog library is that eight dialogs behave *identically* when
you press back. So the shared primitives live in one `primitives` package, and a
primitive only lives under a component when it is genuinely specific to it.
Duplicating `DialogFramePrimitive` per component would reintroduce exactly the
bug 1.x had: eight slightly different dismiss behaviours.

### Tokens and theme — public, no exceptions

`AestheticPalette` (raw hues) is internal. Everything a component reads is
semantic: `AestheticDialogsTheme.colors.status.error.accent`, never `Red500`.
Spacing, radius, dimensions, elevation, typography, shapes and motion are all
tokens; a raw `dp` or `Color` literal inside `primitives/` or `variants/` is a
review failure.

## 2. Enforcement

The boundary is a compiler rule, not a convention:

1. `explicitApi()` on the library module — every public declaration is deliberate
   and every visibility is written down.
2. `internal` on every variant, every primitive and every styling resolver, so a
   consumer *cannot* import one.
3. The folder structure mirrors the layers 1:1, so a review can spot a violation
   by reading an import.
4. The binary-compatibility validator tracks the public API in `api/*.api`; a
   change to it shows up in the diff.

## 3. UI models

Sealed hierarchies of `@Immutable` data classes, one subclass per variant, no
lambdas, no `Dp`, no `Color`, no `FontWeight`. One rendering type is admitted:
`AlertDialogUiModel.Default.icon` takes an `ImageVector`, because an icon the
caller owns cannot be expressed any other way.

```kotlin
ConfirmationDialogUiModel.Destructive(
    title = "Delete this album?",
    message = "The 24 photos inside it will be deleted too.",
    confirmLabel = "Delete album",
    cancelLabel = "Keep it",
    isConfirming = uiState.isDeleting,
)
```

**Why no lambdas.** A model with a lambda in it is a new object on every
recomposition, cannot be compared, cannot be built in a mapper you unit-test, and
cannot be dropped into a preview. Callbacks are parameters.

**Where models were *not* used.** `AestheticContentDialog` takes a content slot as
a parameter, because a composable slot is not data. `AestheticInputDialog` takes
`onValueChange` separately from `onSignal`, because a text field's edit callback
is idiomatic Compose and folding it into a signal would make the common case
worse.

**Two shapes of action API, on purpose.** A confirmation encodes its two buttons
as `confirmLabel` and `cancelLabel`, because their roles are fixed and a caller
should not be able to make "cancel" the primary action. An alert and a content
dialog take `DialogAction` objects, because their actions genuinely vary
("Retry"/"Cancel", "Update now"/"Later", a single "Got it"). That is the design
system being opinionated where the answer is known and flexible where it is not —
not an inconsistency.

**One deliberate absence of `@Immutable`.** `SelectionDialogUiModel` holds a
`List`, which Compose cannot prove is immutable. It is not annotated, because
annotating it would be a promise the type system does not back. The cost is one
recomposition of the dialog when its parent recomposes; the rows are keyed by id
and skip individually. The library does not lie to the compiler to win a
benchmark.

## 4. Signals

Components with more than one meaningful interaction expose one typed callback:

```kotlin
public sealed interface ConfirmationDialogSignal {
    public data object Confirmed : ConfirmationDialogSignal
    public data object Cancelled : ConfirmationDialogSignal
    public data object Dismissed : ConfirmationDialogSignal
}
```

`Cancelled` (a decision) and `Dismissed` (a retreat) are separate because
analytics and "are you sure you want to leave" flows care about the difference.
Callers that do not can handle both in one branch — the `when` makes that
explicit rather than accidental.

Signals are not applied everywhere by reflex. `onValueChange: (String) -> Unit`
stays a plain lambda, because a text field has one obvious interaction and
wrapping it would only add ceremony.

## 5. State ownership

| The library owns | You own |
|---|---|
| Rendering, layout, colour, motion | Whether the dialog is on screen |
| Accessibility semantics | What it says |
| The password reveal toggle (`rememberSaveable`) | The selected values, the text being typed |
| Retaining a banner for the length of its exit transition | Whether a tap changes the selection |

Every dialog is stateless. `AestheticConfirmationDialog` never dismisses itself;
it emits `Dismissed` and you remove it. The only state the library holds is
presentation state with no meaning outside the component, and each instance is
named in the table above.

**The consequence: modal dialogs animate in but not out.** An exit transition
requires the library to keep the dialog composed after you have decided it is
gone, which means owning its visibility — the thing this architecture is built to
avoid. Banners are different: `AestheticNotificationHost` retains the last banner
purely to draw it while it slides away, which is presentation state and nothing
more. That is why the loud motion lives on banners, where it is honest.

## 6. Theming

```
library defaults  →  AestheticDialogsTheme(...)  →  the component's UI model
```

Three levels, each owning something different: the library owns defaults, the
theme owns the brand, the UI model owns the semantics of one dialog. There is no
per-instance colour or typography parameter — a dialog that needs its own palette
is a design decision, and design decisions belong in the theme.

Branding is done by copying a scheme, not implementing one:

```kotlin
AestheticDialogsTheme(
    colors = aestheticLightColors().copy(
        action = aestheticLightColors().action.copy(primary = BrandBlue),
    ),
    shapes = AestheticShapes(dialog = RoundedCornerShape(4.dp)),
) { … }
```

**Colour, type, shape and motion are themeable; spacing and dimensions are not.**
The first four express a brand. The last two express the structure of the
components themselves, and letting callers rewrite them would turn every dialog
into an untestable layout.

**No dynamic colour.** A design system exists so that a warning looks like a
warning; a wallpaper-derived palette cannot make that promise.

**The theme is optional.** Components resolve the light or dark scheme from the
system setting when no theme is present. Nothing crashes and nothing renders
light-on-light — but wrapping is still the only place branding can be applied.

## 7. Motion

Five duration and easing tokens on `AestheticMotion`, one enter transition for
modal dialogs, five `AestheticNotificationAnimation` values for banners. 1.x had
sixteen window animations including spin, windmill and split.

`AestheticMotion.enabled` is resolved from the platform animator duration scale,
so a user who turned animations off in accessibility settings gets instant
dialogs in every app that uses the library, without any of those apps plumbing
the setting through. When it is false, transitions collapse to a **cut**, not to
a shorter animation — that is what the setting actually asks for.

## 8. Adaptive layout

`DialogFramePrimitive` measures the window it was given and picks a width bucket:

| Available width | Dialog width |
|---|---|
| < 600dp | fill, minus a 24dp margin each side |
| 600–840dp | 480dp |
| ≥ 840dp | 560dp |

The question asked is "how much space is there", never "is this a tablet". A
phone in landscape, a half-open foldable and a freeform desktop window all answer
the first question correctly and the second one wrong. The rule is a pure
function and is unit-tested in `AdaptiveWidthTest`.

Height is bounded by the window; the header and action row stay pinned while the
content region scrolls, so a 200% font scale never pushes the confirm button off
screen.

## 9. Accessibility

Built in, not bolted on:

- every modal dialog sets `paneTitle` and `isTraversalGroup`, so screen readers
  announce it and keep traversal inside it;
- titles are marked as headings;
- banners are live regions — `Assertive` for errors, `Polite` for everything
  else, which is the one accessibility decision the library makes for you,
  because getting it wrong is invisible to a sighted developer;
- selection rows carry the selection semantics for the whole row
  (`selectable`/`toggleable` with `Role`), so TalkBack announces
  "Français, radio button, selected" once instead of an unlabelled control next
  to unrelated text;
- every interactive target is at least 48dp;
- all type is in `sp` and all layouts survive a 200% font scale;
- the input dialog moves focus to its field on open, so the keyboard and the
  accessibility focus both land where the user needs them;
- status colour is never the only carrier of meaning: every tone has a distinct
  drawn mark as well as a hue.

**Contrast.** In both shipped schemes every `Tone.accent` clears 4.5:1 against its
surface, so accent-coloured banner titles are readable. Modal dialog titles still
use `content.primary`: a caller who overrides an accent should not be able to
make a title unreadable, and the modal already carries its tone in a large badge.

## 10. Testing

Three layers, each answering a different question:

| Kind | Question | Where |
|---|---|---|
| Unit | Is the rule right? | `AdaptiveWidthTest`, `TokenSemanticsTest` |
| Interaction | Does the gesture produce the right signal, and is the semantics tree correct? | `ConfirmationDialogTest`, `SelectionDialogTest`, `InputDialogTest`, `NotificationHostTest` |
| Screenshot | Did it stop looking right? | `DialogScreenshotTest` |

The screenshot suite is deliberately narrow: one representative state per
component, in both themes, plus the states that break layouts. Every variant
times every tone times every theme times every font scale is four hundred images
nobody reviews and a diff everybody approves without looking.

## 11. Dependencies

`api` is reserved for types that appear in a public signature: the Compose
runtime, `ui`, `ui-graphics`, `ui-text` and `foundation`. **Material 3 is
`implementation`** — it powers the internal primitives but never appears in an
AestheticDialogs signature, so nothing forces you to write Material code to use
this library. It remains a transitive runtime dependency: it takes part in
version resolution and it ships in your APK, like any other `implementation`
dependency. `AestheticColors.withBrand` takes plain `Color`s for
exactly that reason: taking a Material `ColorScheme` would have been one line
shorter at the call site and would have dragged Material 3 into the public API.

### The library does not install a `MaterialTheme`

`AestheticDialogsTheme` provides four CompositionLocals and nothing else.

An earlier revision bridged the Aesthetic palette into a `MaterialTheme` there,
so the internal Material primitives would inherit the right colours. It worked,
and it was wrong: the composable is documented as something you wrap around an
application, so it was replacing the host's colour scheme and type scale for that
entire subtree — including the host's own components, and including whatever a
caller passed into `AestheticContentDialog`. A library may style what it draws;
it may not restyle the application that embeds it.

The three Material components that actually need colours — `OutlinedTextField`,
`Checkbox`, `RadioButton` — are passed them explicitly, and both `Surface`s state
their `contentColor` rather than deriving it from a scheme. The guarantee is
covered by `ThemeIsolationTest`.

The library ships **no icon dependency and no drawable resources**. Every mark is
geometry on a unit square (`AestheticGlyph`), which is why a consumer does not
inherit a few thousand vector assets for the four marks a dialog needs, and why
the marks scale to any size without a density bucket.

## 12. Adding something

A new variant of an existing component:

1. add a subclass to the sealed UI model,
2. write the internal variant that resolves it and composes primitives,
3. add the branch to the component's `when` — the compiler will insist,
4. add previews and a catalog entry,
5. run `./gradlew apiDump` and commit the API change.

A new component: copy the shape of `components/alert` (the smallest complete
example), keep the visibility rules, and reuse `DialogFramePrimitive`. If you
find yourself needing a second dialog frame, that is a signal the first one needs
a slot, not a sibling.

## 13. Trade-offs, taken knowingly

**Boilerplate.** Four artifacts per component — model, variant, component,
previews — for what a single composable could express. Paid because the four are
what let a visual change stop at the variant and a behavioural change stop at the
screen.

**No exit animation on modal dialogs.** Explained in §5. The alternative is the
library owning visibility, which costs more than the animation is worth.

**At most two actions per dialog.** Three-button dialogs force a stacking decision
at every width, and a dialog with three choices is usually a menu wearing a
dialog's clothes.

**`AestheticContentDialog` is an escape hatch.** Someone will use it to bypass the
design system. It is still the right call: without it they would reach for a raw
`Dialog {}` and lose the frame, the insets, the adaptive width and the
accessibility pane as well.

**No Material You.** Explained in §6. Some teams will want it; they can pass their
own scheme to `AestheticDialogsTheme`.
