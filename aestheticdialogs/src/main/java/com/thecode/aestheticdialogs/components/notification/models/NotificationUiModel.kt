package com.thecode.aestheticdialogs.components.notification.models

import androidx.compose.runtime.Immutable
import com.thecode.aestheticdialogs.foundation.DialogTone

/**
 * The visual state of a notification banner.
 *
 * A banner is not a dialog: it sits in your layout, announces itself to screen
 * readers as a live region, and never takes input away from the screen behind
 * it.
 *
 * Five silhouettes, each with the job it is meant for:
 *
 * | Variant      | Silhouette                              | Reach for it when                          |
 * |--------------|-----------------------------------------|--------------------------------------------|
 * | [Toaster]    | Card, tone bar down the leading edge     | Confirming quietly. The default            |
 * | [Rainbow]    | Tone-filled card, inverted copy          | It has to stop the eye. Use it rarely      |
 * | [Connectify] | Centred card under a tone rim            | Ambient system state: network, sync        |
 * | [Emoji]      | Card with a character instead of a mark  | The tone is light: a milestone, a streak   |
 * | [Emotion]    | Gradient card with a timestamp           | It comes from a person: a message, a photo |
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
     * The character is rendered by the platform emoji font, so it scales with
     * the user's font size and adds nothing to your APK. Leave [emoji] null and
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
     * [timestamp] is a formatted string you supply: the user's locale and their
     * 12/24-hour preference are a product decision, not a rendering one.
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
