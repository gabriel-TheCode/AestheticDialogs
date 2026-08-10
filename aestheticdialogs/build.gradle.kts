import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish)
}

// 1. Liaison explicite des propriétés de gradle.properties (Règle le problème "unspecified")
group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

mavenPublishing {
    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = SourcesJar.Sources(),
            javadocJar = JavadocJar.Dokka("dokkaGenerate"),
        ),
    )

    publishToMavenCentral()
    signAllPublications()

    // 2. Remplissage dynamique du bloc POM obligatoire via votre fichier properties
    pom {
        name.set(providers.gradleProperty("POM_NAME").get())
        description.set(providers.gradleProperty("POM_DESCRIPTION").get())
        url.set(providers.gradleProperty("POM_URL").get())

        licenses {
            license {
                name.set(providers.gradleProperty("POM_LICENSE_NAME").get())
                url.set(providers.gradleProperty("POM_LICENSE_URL").get())
                distribution.set(providers.gradleProperty("POM_LICENSE_DIST").get())
            }
        }

        developers {
            developer {
                id.set(providers.gradleProperty("POM_DEVELOPER_ID").get())
                name.set(providers.gradleProperty("POM_DEVELOPER_NAME").get())
                url.set(providers.gradleProperty("POM_DEVELOPER_URL").get())
            }
        }

        scm {
            connection.set(providers.gradleProperty("POM_SCM_CONNECTION").get())
            developerConnection.set(providers.gradleProperty("POM_SCM_DEV_CONNECTION").get())
            url.set(providers.gradleProperty("POM_SCM_URL").get())
        }
    }
}

extensions.configure<LibraryExtension> {
    namespace = "com.thecode.aestheticdialogs"
    compileSdk = libs.versions.compileSdk.get().toInt()

    buildFeatures {
        resValues = true
        buildConfig = true
        compose = true
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Screenshot tests run on the JVM through Robolectric + Roborazzi, which
    // needs the merged Android resources of the library under test.
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

plugins.withId("com.android.application") {
    extensions.configure<ApplicationExtension> {
        defaultConfig {
            versionCode = libs.versions.versionCode.get().toInt()
            versionName = libs.versions.versionName.get()
        }
    }
}

dokka {
    dokkaSourceSets {
        configureEach {
            suppress.set(true)
        }

        // Find whichever name is present at runtime without throwing a missing name exception
        val targetSourceSet = findByName("androidJvm") ?: findByName("release")
        targetSourceSet?.suppress?.set(false)
    }
}

dependencies {
    implementation(libs.screenshot.validation.api)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    testImplementation(composeBom)

    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.text)
    api(libs.androidx.compose.foundation)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.junit.rule)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
