package com.thecode.aestheticdialogs.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme

/**
 * Light and dark in one annotation.
 *
 * Every public component carries this rather than a hand-rolled pair of
 * previews, so "does it survive dark mode" is answered by looking at the file
 * instead of by remembering to check.
 */
@Preview(name = "Light", showBackground = true, backgroundColor = 0xFFF6F7F9)
@Preview(name = "Dark", showBackground = true, backgroundColor = 0xFF121316, uiMode = Configuration.UI_MODE_NIGHT_YES)
internal annotation class ThemePreviews

/**
 * The two font scales that break dialog layouts: the smallest anyone uses and
 * the largest the system offers.
 */
@Preview(name = "Font 100%", showBackground = true, fontScale = 1.0f)
@Preview(name = "Font 200%", showBackground = true, fontScale = 2.0f)
internal annotation class FontScalePreviews

/**
 * A compact phone, a phone in landscape and a tablet — the three width buckets
 * the adaptive dialog frame distinguishes.
 */
@Preview(name = "Compact", showBackground = true, widthDp = 360, heightDp = 720)
@Preview(name = "Landscape", showBackground = true, widthDp = 720, heightDp = 360)
@Preview(name = "Expanded", showBackground = true, widthDp = 1000, heightDp = 800)
internal annotation class WindowSizePreviews

/**
 * Renders preview content on the Aesthetic canvas.
 *
 * Dialogs open a real platform window even in a preview, so the surface behind
 * them is what the preview actually shows: filling it with the theme's canvas
 * keeps light and dark previews honest.
 */
@Composable
internal fun AestheticPreviewSurface(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    AestheticDialogsTheme(darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AestheticDialogsTheme.colors.surface.sunken),
        ) {
            content()
        }
    }
}
