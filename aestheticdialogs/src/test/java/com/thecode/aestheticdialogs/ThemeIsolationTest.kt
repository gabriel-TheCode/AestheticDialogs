package com.thecode.aestheticdialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.aestheticLightColors
import com.thecode.aestheticdialogs.foundation.withBrand
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The library's side of the contract with the applications that embed it.
 *
 * [AestheticDialogsTheme] is documented as safe to wrap around a whole
 * application, which is only true while it provides its own CompositionLocals
 * and touches nothing else. It used to install a `MaterialTheme` built from the
 * Aesthetic palette, which silently replaced the host's colour scheme and type
 * scale for that entire subtree — including the host's own components. These
 * tests are here so that cannot come back.
 */
@RunWith(AndroidJUnit4::class)
class ThemeIsolationTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `the host colour scheme survives the theme`() {
        var observed: Color? = null

        composeRule.setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = HOST_PRIMARY)) {
                AestheticDialogsTheme {
                    observed = MaterialTheme.colorScheme.primary
                }
            }
        }

        assertThat(observed).isEqualTo(HOST_PRIMARY)
    }

    @Test
    fun `the host type scale survives the theme`() {
        var observed: TextStyle? = null

        composeRule.setContent {
            MaterialTheme(typography = Typography(bodyLarge = HOST_BODY)) {
                AestheticDialogsTheme {
                    observed = MaterialTheme.typography.bodyLarge
                }
            }
        }

        assertThat(observed).isEqualTo(HOST_BODY)
    }

    @Test
    fun `the theme still provides its own tokens inside the host theme`() {
        var observed: Color? = null

        composeRule.setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = HOST_PRIMARY)) {
                AestheticDialogsTheme(
                    colors = aestheticLightColors().withBrand(
                        primary = BRAND,
                        onPrimary = Color.White,
                    ),
                ) {
                    observed = AestheticDialogsTheme.colors.action.primary
                }
            }
        }

        assertThat(observed).isEqualTo(BRAND)
    }

    @Test
    fun `withBrand leaves the status tones alone`() {
        val base = aestheticLightColors()
        val branded = base.withBrand(primary = BRAND, onPrimary = Color.White)

        assertThat(branded.action.primary).isEqualTo(BRAND)
        assertThat(branded.border.focus).isEqualTo(BRAND)
        // A brand may repaint the actions. It may not repaint what an error means.
        assertThat(branded.status).isEqualTo(base.status)
    }

    private companion object {
        val HOST_PRIMARY = Color(0xFFFF00FF)
        val BRAND = Color(0xFF00A97F)
        val HOST_BODY = TextStyle(fontSize = 37.sp)
    }
}
