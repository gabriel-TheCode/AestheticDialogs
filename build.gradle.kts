plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.binary.compatibility.validator)
}

// Formatting is centralised here rather than in a convention plugin: the build
// has two modules, so a buildSrc/build-logic module would cost more than it saves.
// ktlint 1.x defaults to the `ktlint_official` code style, which rewrites every
// multi-line assignment and collapses composable signatures onto one line, and
// its function-naming rule does not know that composables are PascalCase. The
// same values are in `.editorconfig` for the IDE; they are repeated here because
// Spotless resolves ktlint's configuration itself and does not pick that file up.
val ktlintRules = mapOf(
    "ktlint_code_style" to "intellij_idea",
    "ktlint_function_naming_ignore_when_annotated_with" to "Composable, Preview, Test",
    "max_line_length" to "120",
    // The `ktlint_official` rules that rewrite working code into a different
    // house style. Everything that catches a real defect stays on.
    "ktlint_standard_function-signature" to "disabled",
    "ktlint_standard_class-signature" to "disabled",
    "ktlint_standard_function-expression-body" to "disabled",
    "ktlint_standard_multiline-expression-wrapping" to "disabled",
    "ktlint_standard_string-template-indent" to "disabled",
    "ktlint_standard_chain-method-continuation" to "disabled",
    "ktlint_standard_blank-line-before-declaration" to "disabled",
    "ktlint_standard_blank-line-between-when-conditions" to "disabled",
    "ktlint_standard_no-empty-first-line-in-class-body" to "disabled",
    "ktlint_standard_if-else-wrapping" to "disabled",
    "ktlint_standard_condition-wrapping" to "disabled",
)

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get()).editorConfigOverride(ktlintRules)
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get()).editorConfigOverride(ktlintRules)
    }
}

// The catalog application is not published, so its API surface is not tracked.
apiValidation {
    ignoredProjects.add("app")
}
