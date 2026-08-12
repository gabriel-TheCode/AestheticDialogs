package com.thecode.aestheticdialogs.utils

import androidx.compose.ui.semantics.LiveRegionMode
import com.thecode.aestheticdialogs.foundation.DialogTone

/**
 * How urgently a surface carrying [this] tone should interrupt a screen reader.
 *
 * Errors interrupt; everything else waits for a pause in speech. This is the one
 * accessibility decision the library makes on the caller's behalf, because
 * getting it wrong is invisible to a sighted developer.
 *
 * @return the live-region mode to attach to the surface.
 */
internal fun DialogTone.liveRegionMode(): LiveRegionMode = when (this) {
    DialogTone.Error -> LiveRegionMode.Assertive
    else -> LiveRegionMode.Polite
}
