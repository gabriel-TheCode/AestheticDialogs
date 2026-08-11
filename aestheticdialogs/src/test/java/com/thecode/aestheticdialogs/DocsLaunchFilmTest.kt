package com.thecode.aestheticdialogs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import com.thecode.aestheticdialogs.film.FILM_DURATION_MS
import com.thecode.aestheticdialogs.film.LaunchFilm
import com.thecode.aestheticdialogs.film.loadPlates
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Renders the launch film, one PNG per frame.
 *
 * `scripts/generate-launch-film.sh` runs this and hands the frames to ffmpeg. The
 * film is a pure function of time, so a frame can be re-rendered in isolation and
 * the sequence is identical on every machine.
 *
 * Run `DocsFilmPlatesTest` first: this composites the plates it produces.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w1920dp-h1080dp-mdpi")
@OptIn(ExperimentalRoborazziApi::class)
class DocsLaunchFilmTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renderFrames() {
        val plates = loadPlates(PLATES)
        check(plates.isNotEmpty()) { "No plates in $PLATES — run DocsFilmPlatesTest first." }

        var timeMs by mutableStateOf(0)
        composeRule.setContent { LaunchFilm(timeMs = timeMs, plates = plates) }

        val frameCount = FILM_DURATION_MS * FPS / 1_000
        repeat(frameCount) { index ->
            timeMs = index * 1_000 / FPS
            composeRule.waitForIdle()
            captureScreenRoboImage("$FRAMES/frame_%04d.png".format(index))
        }
    }

    private companion object {
        const val PLATES = "build/film/cut"
        const val FRAMES = "build/film/frames"
        const val FPS = 24
    }
}
