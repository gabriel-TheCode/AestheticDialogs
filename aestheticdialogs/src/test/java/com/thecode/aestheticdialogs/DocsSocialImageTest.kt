package com.thecode.aestheticdialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import com.thecode.aestheticdialogs.components.notification.AestheticNotification
import com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.tokens.AestheticPalette
import com.thecode.aestheticdialogs.tokens.AestheticSpacing
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Draws the release announcement card, at LinkedIn's 1200×627 link ratio.
 *
 * The right-hand side is the real notification component rendered once per status
 * tone, so the card cannot advertise something the library does not do — and a
 * palette change redraws it.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w1200dp-h627dp-mdpi")
@OptIn(ExperimentalRoborazziApi::class)
class DocsSocialImageTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun announcementCard() {
        composeRule.setContent {
            AestheticDialogsTheme(darkTheme = false) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backdrop())
                        .padding(horizontal = GUTTER, vertical = GUTTER),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Pitch(Modifier.weight(WORDMARK_WEIGHT))
                    Spacer(Modifier.width(GUTTER))
                    Proof(Modifier.weight(PROOF_WEIGHT))
                }
            }
        }
        captureScreenRoboImage("$DOCS_IMAGES/announcement-2.0.png")
    }

    private fun backdrop(): Brush = Brush.linearGradient(
        0.0f to AestheticPalette.White,
        0.5f to AestheticPalette.Grey50,
        1.0f to AestheticPalette.BlueTintLight,
        start = Offset.Zero,
        end = Offset(BACKDROP_SPAN, BACKDROP_SPAN),
    )

    @Composable
    private fun Pitch(modifier: Modifier) {
        val colors = AestheticDialogsTheme.colors
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Aesthetic",
                    style = TextStyle(
                        fontSize = TITLE,
                        fontWeight = FontWeight.Light,
                        color = colors.content.primary,
                    ),
                )
                Text(
                    text = "Dialogs",
                    style = TextStyle(
                        fontSize = TITLE,
                        fontWeight = FontWeight.Bold,
                        color = colors.content.primary,
                    ),
                )
                Spacer(Modifier.width(AestheticSpacing.lg))
                VersionChip()
            }

            Spacer(Modifier.height(AestheticSpacing.lg))
            Text(
                text = "Rebuilt in Jetpack Compose.",
                style = TextStyle(fontSize = TAGLINE, color = colors.content.secondary),
            )

            Spacer(Modifier.height(AestheticSpacing.xxxl))
            HIGHLIGHTS.forEach { line ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = AestheticSpacing.md),
                ) {
                    Box(
                        Modifier
                            .size(BULLET)
                            .background(colors.status.success.accent, RoundedCornerShape(BULLET)),
                    )
                    Spacer(Modifier.width(AestheticSpacing.md))
                    Text(
                        text = line,
                        style = TextStyle(fontSize = BODY, color = colors.content.primary),
                    )
                }
            }

            Spacer(Modifier.height(AestheticSpacing.lg))
            Box(
                modifier = Modifier
                    .background(colors.surface.container, AestheticDialogsTheme.shapes.control)
                    .padding(horizontal = AestheticSpacing.lg, vertical = AestheticSpacing.md),
            ) {
                Text(
                    text = "com.gabrielthecode:aestheticdialogs:2.0.0",
                    style = TextStyle(
                        fontSize = CODE,
                        fontFamily = FontFamily.Monospace,
                        color = colors.content.secondary,
                    ),
                )
            }

            Spacer(Modifier.height(AestheticSpacing.xl))
            Text(
                text = "github.com/gabriel-TheCode/AestheticDialogs",
                style = TextStyle(fontSize = FOOTER, color = colors.content.muted),
            )
        }
    }

    @Composable
    private fun VersionChip() {
        val tone = AestheticDialogsTheme.colors.status.forTone(DialogTone.Info)
        Box(
            modifier = Modifier
                .background(tone.container, AestheticDialogsTheme.shapes.button)
                .padding(horizontal = AestheticSpacing.lg, vertical = AestheticSpacing.xs),
        ) {
            Text(
                text = "2.0",
                style = TextStyle(
                    fontSize = CHIP,
                    fontWeight = FontWeight.SemiBold,
                    color = tone.accent,
                ),
            )
        }
    }

    /** Four real banners, one per status tone. Nothing here is a mockup. */
    @Composable
    private fun Proof(modifier: Modifier) {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(AestheticSpacing.lg, Alignment.CenterVertically),
        ) {
            AestheticNotification(
                uiModel = NotificationUiModel.Toaster(
                    title = "Saved",
                    message = "Your changes are on every device.",
                    tone = DialogTone.Success,
                ),
                onSignal = {},
            )
            AestheticNotification(
                uiModel = NotificationUiModel.Rainbow(
                    title = "Version 2.0 is out",
                    message = "Seven families, one design system.",
                    tone = DialogTone.Info,
                ),
                onSignal = {},
            )
            AestheticNotification(
                uiModel = NotificationUiModel.Connectify(
                    title = "You are offline",
                    message = "We will sync as soon as you reconnect.",
                    tone = DialogTone.Warning,
                ),
                onSignal = {},
            )
            AestheticNotification(
                uiModel = NotificationUiModel.Emotion(
                    title = "Payment declined",
                    message = "Your bank refused the charge.",
                    timestamp = "13:56",
                    tone = DialogTone.Error,
                ),
                onSignal = {},
            )
        }
    }

    private companion object {
        const val DOCS_IMAGES = "../docs/images"

        val GUTTER = 76.dp
        const val WORDMARK_WEIGHT = 1.15f
        const val PROOF_WEIGHT = 1.05f
        val BULLET = 7.dp

        val TITLE = 54.sp
        val TAGLINE = 23.sp
        val BODY = 18.sp
        val CHIP = 17.sp
        val CODE = 15.sp
        val FOOTER = 15.sp

        const val BACKDROP_SPAN = 1400f

        val HIGHLIGHTS = listOf(
            "Seven component families, one frame",
            "Component → Variant → Primitive → Tokens",
            "Accessible, adaptive, theme-agnostic",
        )
    }
}
