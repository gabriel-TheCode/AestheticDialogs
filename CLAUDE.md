# AestheticDialogs — working notes

A published Jetpack Compose dialog design system. Treat every change as one an
external team will depend on for years.

## Architecture — non-negotiable

Three layers, from *Building a Scalable UI System in Jetpack Compose*
(Gabriel TEKOMBO). Full detail in [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

```
Component  (public)    behaviour: wires the UI model, emits signals
    ↓
Variant    (internal)  one stable visual configuration
    ↓
Primitive  (internal)  how it is drawn; raw parameters only
    ↓
Tokens     (public)    colour, type, spacing, shape, motion
```

| Layer | Owns | Must not |
|---|---|---|
| Component | the behavioural contract, callbacks, dispatch on the UI model | interpret what a signal *means*, navigate, hold feature state |
| Variant | one visual configuration, minor conditional rendering | own state, know the screen, be public |
| Primitive | rendering, layout, tokens | take a UI model, hold state, know where it is used, grow feature logic |

Rules that get code rejected:

- **UI models never contain lambdas.** Callbacks are component parameters.
- **Signals travel up, never get interpreted inside the library.** The design
  system decides how things look; the caller decides what they mean.
- **Variants and primitives stay `internal`.** `explicitApi()` enforces it.
- Abstract only when duplication is *observed*, not anticipated.
- A new visual form is a new Variant. A new *setting* is a parameter. Do not
  collapse variants into a style-descriptor table — that deletes the layer.

## Comments — the standard

Comments are part of the public quality of this library. Write them the way a
staff engineer reviews them.

**Keep**

- KDoc on every public declaration, with `@param` / `@return`:
  ```kotlin
  /**
   * ...
   *
   * @param uiModel the visual state; the subclass selects the variant.
   * @param onSignal receives what the user did.
   * @param modifier applied to the dialog surface.
   */
  ```
- Comments that explain **why** something is the way it is, when the reason is
  not visible in the code: a non-obvious constraint, a platform quirk, a
  deliberate trade-off, an accessibility decision, a rejected alternative.
- Short warnings that stop the next person breaking something.

**Delete**

- Comments that restate the code (`// set the title`, `// loop over items`).
- Development narration — anything that reads like a changelog, a task, or a
  memory of the session that produced it: *"this used to be X"*, *"passing Y
  twice is what caused the bug"*, *"an earlier revision did Z"*. Historical
  rationale belongs in `docs/`, in the git history, or in a migration guide.
- Version archaeology in code. "1.x did it this way" is documentation, not a
  comment — put it in `docs/ARCHITECTURE.md` or `docs/MIGRATION.md`.
- Section banners and decorative separators.
- Commented-out code.

**Shape**

- One or two lines. If a rationale needs a paragraph, it is documentation.
- Present tense, describing the code as it is — never how it got there.
- Above the statement it explains, never trailing at end of line.

## Build

```bash
./gradlew build                      # compile, lint, unit + interaction tests
./gradlew spotlessApply              # format (config lives in build.gradle.kts, not .editorconfig)
./gradlew apiDump                    # after any public API change — the diff is reviewed
scripts/generate-docs-images.sh      # redraw docs/images; --baselines also records screenshot baselines
```

Documentation images are rendered from the real components by Robolectric
(`Docs*.kt` in the library test source set). Add a dialog, add a case there —
the README then updates itself.

## Public API

- `explicitApi()` is on. Everything public is a commitment.
- Material 3 is `implementation` and must never appear in a public signature.
- The library installs no `MaterialTheme` and reads none — see `ThemeIsolationTest`.
- Public sealed hierarchies: adding a case breaks consumers' exhaustive `when`.
  Batch such additions into one release.
