plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.binary.compatibility.validator)
}

// Formatting is centralised here rather than in a convention plugin: the build
// has two modules, so a buildSrc/build-logic module would cost more than it saves.
spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get())
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get())
    }
}

// The catalog application is not published, so its API surface is not tracked.
apiValidation {
    ignoredProjects.add("app")
}
