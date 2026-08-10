package com.thecode.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
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
 */
class CatalogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by rememberSaveable { mutableStateOf<Boolean?>(null) }
            val resolvedDark = darkTheme ?: isSystemInDarkTheme()

            AestheticDialogsTheme(darkTheme = resolvedDark) {
                CatalogScreen(
                    darkTheme = resolvedDark,
                    onToggleTheme = { darkTheme = !resolvedDark },
                )
            }
        }
    }
}
