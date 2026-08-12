package com.thecode.aestheticdialogs.utils

import androidx.compose.ui.graphics.Color
import com.thecode.aestheticdialogs.components.notification.models.NotificationPresence
import com.thecode.aestheticdialogs.foundation.AestheticColors
import com.thecode.aestheticdialogs.foundation.DialogTone

/**
 * The hue of a presence dot in this scheme.
 *
 * A lookup, not a styling decision: availability maps onto the success tone and
 * absence onto muted content, in both schemes and under any brand.
 *
 * @param presence whether the person is available, or `null` for no dot.
 * @return the dot colour, or `null` when there is nothing to draw.
 */
internal fun AestheticColors.presenceColor(presence: NotificationPresence?): Color? =
    when (presence) {
        NotificationPresence.Online -> status.success.accent
        NotificationPresence.Offline -> content.muted
        null -> null
    }

/**
 * The hue of a progress ring in this scheme.
 *
 * A neutral wait carries no status, so it uses the action colour rather than the
 * neutral accent: a grey spinner reads as disabled.
 *
 * @param tone the tone the dialog carries.
 * @return the indicator colour.
 */
internal fun AestheticColors.indicatorColor(tone: DialogTone): Color =
    if (tone == DialogTone.Neutral) action.primary else status.forTone(tone).accent
