## Feeling Awesome! Thanks for thinking about this.

You can contribute us by filing issues, bugs and PRs.

### Contributing:
- Open issue regarding proposed change.
- Repo owner will contact you there.
- If your proposed change is approved, Fork this repo and do changes.
- Open PR against latest `dev` branch. Add nice description in PR.
- You're done!

---

## Working on AestheticDialogs 2.0

### Requirements

JDK 17 and an Android SDK with API 36. Everything else comes from the Gradle
wrapper and the version catalog in `gradle/libs.versions.toml` — no version is
declared anywhere else.

### Commands

```bash
./gradlew build                    # compile, lint, unit + interaction tests
./gradlew :aestheticdialogs:testDebugUnitTest
./gradlew recordRoborazziDebug     # record screenshot baselines
./gradlew verifyRoborazziDebug     # compare against the committed baselines
./gradlew apiDump                  # update api/aestheticdialogs.api
./gradlew apiCheck                 # fail if the public API drifted from the dump
./gradlew spotlessApply            # format
./gradlew :app:installDebug        # the component catalog
```

### The rules that CI enforces

**The layer boundary.** `explicitApi()` is on, so every declaration states its
visibility. Variants, primitives and styling resolvers are `internal` — if you
find yourself making one `public` to use it somewhere, the thing you actually
need is a new variant or a new slot.

**No raw visual values.** A `dp`, an `sp` or a `Color` literal inside
`primitives/`, `variants/` or `components/` is a review failure. Add a token.

**No lambdas in UI models.** Callbacks are parameters — one per interaction,
named after it. See [docs/ARCHITECTURE.md §3](docs/ARCHITECTURE.md).

**A variant initialises one primitive.** It resolves the model into values and
passes them; it does not compose several primitives or build slot lambdas. If a
variant needs something drawn that its family primitive cannot draw, the
parameter goes on the primitive. One variant per file, named after it.

**`@Immutable` is a promise.** Do not put it on a class holding a `List`, a `Map`
or anything else Compose cannot prove. A slightly slower dialog is cheaper than a
skipped recomposition that should not have been skipped.

**The public API is tracked.** Any change to it must come with an updated
`api/aestheticdialogs.api` from `./gradlew apiDump`, in the same commit. That is
how a reviewer sees an addition to the public surface without reading the whole
diff.

### Adding a dialog or a variant

Follow [docs/ARCHITECTURE.md §12](docs/ARCHITECTURE.md). In short: model →
variant, in a file of its own → the branch in the component's `when` → previews →
a catalog entry → a gallery case → `apiDump`.

Every public component carries `@ThemePreviews`, and the ones whose layout is
sensitive to it also carry `@FontScalePreviews` or `@WindowSizePreviews`. The
previews are the catalogue you work against while building.

### Screenshots

Baselines live in `aestheticdialogs/src/test/screenshots` and are committed. When
a change moves pixels on purpose, re-record and commit the new images **with the
change that caused them**, so the review can see both together.

Keep the suite narrow. One representative state per component per theme, plus the
states that break layouts. A suite nobody reviews is a suite that catches
nothing.

### Releasing

Maintainers only. Coordinates are `com.gabrielthecode:aestheticdialogs:<version>`,
from `GROUP` / `POM_ARTIFACT_ID` / `VERSION_NAME` in `gradle.properties`.

**Credentials never go in `gradle.properties` — that file is tracked.** They live
in `~/.gradle/gradle.properties` (`chmod 600`), which sits outside every
repository and so cannot be committed: `mavenCentralUsername` and
`mavenCentralPassword` are a Central Portal *user token*, not an account
password. CI passes the same names as `ORG_GRADLE_PROJECT_*` environment
variables and signs with `signingInMemoryKey`.

The `com.gabrielthecode` namespace is verified on the Central Portal through a
DNS TXT record on `gabrielthecode.com`; publishing to an unverified namespace is
rejected.

```bash
./gradlew apiCheck                 # the public API is unchanged, or the diff is intended
./gradlew build                    # tests, lint, screenshot baselines
./gradlew publishToMavenCentral    # staged, then released from the portal
```

