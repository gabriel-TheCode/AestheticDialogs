package com.thecode.aestheticdialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.components.input.AestheticInputDialog
import com.thecode.aestheticdialogs.components.input.models.InputDialogUiModel
import com.thecode.aestheticdialogs.components.selection.AestheticSelectionDialog
import com.thecode.aestheticdialogs.components.selection.models.SelectionDialogUiModel
import com.thecode.aestheticdialogs.components.selection.models.SelectionItem
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * The other half of the contract in [ThemeIsolationTest].
 *
 * That test proves the library does not *write* to the host's `MaterialTheme`.
 * This one proves it does not *read* from it: the components that lean hardest on
 * Material internals — the text field and the selection controls — are rendered
 * twice, once inside a deliberately garish host theme and once with no
 * `MaterialTheme` at all, and the two images must be identical byte for byte.
 *
 * Reading the sources cannot answer this. `OutlinedTextField`, `Checkbox` and
 * `Surface` resolve some of their own colours, and a parameter left unpassed
 * silently falls back to the ambient scheme. Only the pixels settle it.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w411dp-h700dp-xhdpi")
@OptIn(ExperimentalRoborazziApi::class)
class ThemeAgnosticRenderingTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `the input dialog renders identically with and without a host theme`() {
        assertIdenticalUnderAnyHostTheme("input") {
            AestheticInputDialog(
                uiModel = InputDialogUiModel.Text(
                    title = "Add a recipient",
                    value = "not-an-address",
                    label = "Email",
                    errorText = "That does not look like an email address.",
                    confirmLabel = "Add",
                    cancelLabel = "Cancel",
                ),
                onValueChange = {},
                onSignal = {},
            )
        }
    }

    @Test
    fun `the selection dialog renders identically with and without a host theme`() {
        assertIdenticalUnderAnyHostTheme("selection") {
            AestheticSelectionDialog(
                uiModel = SelectionDialogUiModel.Multiple(
                    title = "Notify me about",
                    items = listOf(
                        SelectionItem("a", "Mentions"),
                        SelectionItem("b", "Releases"),
                        SelectionItem("c", "Digest", enabled = false),
                    ),
                    selectedIds = setOf("a"),
                    confirmLabel = "Save",
                    cancelLabel = "Cancel",
                    searchQuery = "",
                ),
                onSignal = {},
            )
        }
    }

    private fun assertIdenticalUnderAnyHostTheme(name: String, content: @Composable () -> Unit) {
        // One composition, one `setContent`: the rule allows no more. The host
        // theme is a state flag so both renderings happen in the same test.
        var hosted by mutableStateOf(true)

        composeRule.setContent {
            if (hosted) {
                MaterialTheme(
                    colorScheme = lightColorScheme(
                        primary = Color.Magenta,
                        onPrimary = Color.Yellow,
                        surface = Color.Cyan,
                        onSurface = Color.Red,
                        outline = Color.Green,
                    ),
                    typography = Typography(
                        bodyLarge = TextStyle(
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.Black,
                            fontSize = 31.sp,
                        ),
                    ),
                ) {
                    AestheticDialogsTheme(darkTheme = false, content = content)
                }
            } else {
                AestheticDialogsTheme(darkTheme = false, content = content)
            }
        }

        val withHost = File("build/theme-agnostic/$name-hosted.png")
        captureScreenRoboImage(withHost.path)

        hosted = false
        composeRule.waitForIdle()

        val withoutHost = File("build/theme-agnostic/$name-bare.png")
        captureScreenRoboImage(withoutHost.path)

        assertThat(withHost.readBytes()).isEqualTo(withoutHost.readBytes())
    }
}
