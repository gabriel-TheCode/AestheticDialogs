package com.thecode.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme

/**
 * The AestheticDialogs component catalog.
 *
 * It depends on `:aestheticdialogs` the way any other application would — the
 * published API only — which makes it the cheapest test of whether the public
 * surface is actually sufficient. Anything the catalog cannot build without
 * reaching for an internal is a hole in the API.
 *
 * The nesting below is the recommended one, and is here to be copied: the
 * application's own `MaterialTheme` on the outside, `AestheticDialogsTheme`
 * within it. The inner theme provides the library's tokens and leaves the outer
 * one alone, so an application keeps its own palette everywhere outside a dialog.
 */
class CatalogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by rememberSaveable { mutableStateOf<Boolean?>(null) }
            val resolvedDark = darkTheme ?: isSystemInDarkTheme()

            MaterialTheme(
                colorScheme = if (resolvedDark) darkColorScheme() else lightColorScheme(),
            ) {
                AestheticDialogsTheme(darkTheme = resolvedDark) {
                    CatalogScreen(
                        darkTheme = resolvedDark,
                        onDarkThemeChange = { darkTheme = it },
                    )
                }
            }
        }
    }
}
