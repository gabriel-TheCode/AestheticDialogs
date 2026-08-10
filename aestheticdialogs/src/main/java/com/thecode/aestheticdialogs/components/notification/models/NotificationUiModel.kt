package com.thecode.aestheticdialogs.components.notification.models

import androidx.compose.runtime.Immutable
import com.thecode.aestheticdialogs.foundation.DialogTone

/**
 * The visual state of a notification banner.
 *
 * These are the AestheticDialogs 1.x styles that were never really dialogs.
 * `TOASTER`, `CONNECTIFY`, `RAINBOW`, `EMOJI` and `EMOTION` all opened a modal
 * window pinned to an edge of the screen, which meant an informational toast
 * dimmed the app, stole focus and consumed the back gesture. In 2.0 they are
 * banners: ordinary composables that sit in your layout, announce themselves to
 * screen readers as a live region, and never take input away from the screen
 * behind them.
 *
 * The five variants are the five silhouettes, preserved as they were:
 *
 * | 1.x style     | 2.0 variant   | Silhouette                                   |
 * |---------------|---------------|----------------------------------------------|
 * | `TOASTER`     | [Toaster]     | Card, tone bar down the leading edge          |
 * | `RAINBOW`     | [Rainbow]     | Tone-filled card, inverted copy               |
 * | `CONNECTIFY`  | [Connectify]  | Card under a tone gradient strip, centred     |
 * | `EMOJI`       | [Emoji]       | Card with a large emoji                       |
 * | `EMOTION`     | [Emotion]     | Gradient card with a timestamp                |
 */
public sealed interface NotificationUiModel {

    public val title: String
    public val message: String?
    public val tone: DialogTone

    /** Whether the banner shows its own close affordance. */
    public val showCloseButton: Boolean

    /** Card with a tone-coloured bar down the leading edge. */
    @Immutable
    public data class Toaster(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Success,
        override val showCloseButton: Boolean = true,
    ) : NotificationUiModel

    /** Tone-filled card with inverted copy. The loudest banner. */
    @Immutable
    public data class Rainbow(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Info,
        override val showCloseButton: Boolean = true,
    ) : NotificationUiModel

    /**
     * Centred card under a gradient strip, for connectivity and other ambient
     * state changes.
     */
    @Immutable
    public data class Connectify(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Success,
        override val showCloseButton: Boolean = true,
    ) : NotificationUiModel

    /**
     * Card with a large emoji instead of a drawn mark.
     *
     * 1.x shipped PNG bitmaps for this. Passing the character means the platform
     * emoji font renders it, so it scales with the user's font size, follows the
     * system emoji style, and adds nothing to your APK. Leave [emoji] null and
     * the library picks one from the tone.
     */
    @Immutable
    public data class Emoji(
        override val title: String,
        override val message: String? = null,
        val emoji: String? = null,
        override val tone: DialogTone = DialogTone.Success,
        override val showCloseButton: Boolean = true,
    ) : NotificationUiModel

    /**
     * Gradient card with a timestamp, for message-like notifications.
     *
     * [timestamp] is a formatted string you supply. 1.x called
     * `SimpleDateFormat("HH:mm")` inside the dialog, which ignored the user's
     * 12/24-hour preference, ignored their locale and made the dialog impossible
     * to screenshot-test. Formatting a time is a product decision, not a
     * rendering one.
     */
    @Immutable
    public data class Emotion(
        override val title: String,
        override val message: String? = null,
        val timestamp: String? = null,
        override val tone: DialogTone = DialogTone.Success,
        override val showCloseButton: Boolean = false,
    ) : NotificationUiModel
}

/** What the user did with a notification banner. */
public sealed interface NotificationSignal {
    /** The banner body was tapped. */
    public data object Clicked : NotificationSignal

    /**
     * The banner should go away: the close affordance was pressed, or the
     * auto-dismiss delay elapsed.
     */
    public data object Dismissed : NotificationSignal
}

/** Which edge a notification host anchors its banner to. */
public enum class NotificationAlignment {
    Top,
    Bottom,
}
