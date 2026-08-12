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
 * Four variants, one per stable visual configuration:
 *
 * | Variant    | Configuration                      | Reach for it when                     |
 * |------------|------------------------------------|---------------------------------------|
 * | [Default]  | Card, tone bar down the leading edge | Confirming quietly. The default      |
 * | [Filled]   | Tone-filled card, inverted copy     | It has to stop the eye. Use it rarely |
 * | [Gradient] | Tone ramp, inverted copy            | It comes from a person, or a moment   |
 * | [Ambient]  | Centred copy under a tone rim       | System state: network, sync           |
 *
 * [Default.emoji] and [Default.timestamp] are fields rather than variants of
 * their own: a character in the leading slot and a formatted time at the trailing
 * edge are *content*, and 2.0 encoding them as types (`Emoji`, `Emotion`) is the
 * over-abstraction the architecture warns about.
 */
public sealed interface NotificationUiModel {

    public val title: String
    public val message: String?
    public val tone: DialogTone

    /** Whether the banner shows its own close affordance. */
    public val showCloseButton: Boolean

    /** A single trailing action. `null` leaves the trailing edge to the timestamp or the cross. */
    public val action: NotificationAction?

    /** Card with a tone-coloured bar down the leading edge. */
    @Immutable
    public data class Default(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Success,
        /** A single trailing text action — `Undo`, usually. */
        override val action: NotificationAction? = null,
        /**
         * Drawn instead of the tone mark.
         *
         * Rendered by the platform emoji font, so it scales with the user's font
         * size and adds nothing to your APK. A banner carrying one drops the tone
         * bar down its leading edge: a character is not a status.
         */
        val emoji: String? = null,
        /**
         * A formatted time, shown at the trailing edge. The user's locale and
         * their 12/24-hour preference are a product decision, so the string is
         * yours.
         */
        val timestamp: String? = null,
        /** Presence dot drawn over the leading slot. For banners that come from a person. */
        val presence: NotificationPresence? = null,
        /**
         * Determinate progress bonded to the bottom edge, from `0f` to `1f`.
         *
         * For work that does not deserve a modal: a background upload, a sync.
         * Values outside the range are clamped.
         */
        val progress: Float? = null,
        override val showCloseButton: Boolean = true,
    ) : NotificationUiModel

    /** Tone-filled card with inverted copy. The loudest banner. */
    @Immutable
    public data class Filled(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Info,
        /** A single trailing text action — `Undo`, usually. */
        override val action: NotificationAction? = null,
        /**
         * Drawn instead of the tone mark.
         *
         * Rendered by the platform emoji font, so it scales with the user's font
         * size and adds nothing to your APK. A banner carrying one drops the tone
         * bar down its leading edge: a character is not a status.
         */
        val emoji: String? = null,
        /**
         * A formatted time, shown at the trailing edge. The user's locale and
         * their 12/24-hour preference are a product decision, so the string is
         * yours.
         */
        val timestamp: String? = null,
        /** Presence dot drawn over the leading slot. For banners that come from a person. */
        val presence: NotificationPresence? = null,
        /**
         * Determinate progress bonded to the bottom edge, from `0f` to `1f`.
         *
         * For work that does not deserve a modal: a background upload, a sync.
         * Values outside the range are clamped.
         */
        val progress: Float? = null,
        override val showCloseButton: Boolean = true,
    ) : NotificationUiModel

    /** Gradient card with inverted copy, for message-like notifications. */
    @Immutable
    public data class Gradient(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Success,
        /** A single trailing text action — `Undo`, usually. */
        override val action: NotificationAction? = null,
        /**
         * Drawn instead of the tone mark.
         *
         * Rendered by the platform emoji font, so it scales with the user's font
         * size and adds nothing to your APK. A banner carrying one drops the tone
         * bar down its leading edge: a character is not a status.
         */
        val emoji: String? = null,
        /**
         * A formatted time, shown at the trailing edge. The user's locale and
         * their 12/24-hour preference are a product decision, so the string is
         * yours.
         */
        val timestamp: String? = null,
        /** Presence dot drawn over the leading slot. For banners that come from a person. */
        val presence: NotificationPresence? = null,
        /**
         * Determinate progress bonded to the bottom edge, from `0f` to `1f`.
         *
         * For work that does not deserve a modal: a background upload, a sync.
         * Values outside the range are clamped.
         */
        val progress: Float? = null,
        override val showCloseButton: Boolean = false,
    ) : NotificationUiModel

    /**
     * Centred card under a gradient strip, for connectivity and other ambient
     * state changes.
     */
    @Immutable
    public data class Ambient(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Success,
        override val action: NotificationAction? = null,
        override val showCloseButton: Boolean = true,
    ) : NotificationUiModel

    /**
     * A docked strip: full width, square corners, no shadow.
     *
     * For a *condition* rather than an event — offline, degraded, read-only. A
     * condition does not float above the content and does not auto-dismiss,
     * because it is still true four seconds later; the host anchors it flush to
     * its edge and ignores its auto-dismiss delay.
     */
    @Immutable
    public data class Strip(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Warning,
        override val action: NotificationAction? = null,
        override val showCloseButton: Boolean = false,
    ) : NotificationUiModel
}

/**
 * The single trailing action of a banner.
 *
 * One label and nothing else: a banner is not a dialog, and the moment it needs
 * two actions it needs to be one.
 */
@Immutable
public data class NotificationAction(
    /** The label. Supplied by the caller, so it is localised by the caller. */
    val label: String,
)

/** Whether the person a banner comes from is available. Drawn as a dot on the leading slot. */
public enum class NotificationPresence {
    Online,
    Offline,
}

/**
 * What the host does when a banner arrives while another one is showing.
 *
 * Before this existed the answer was "silently replace it", which is a reasonable
 * default and a terrible accident.
 */
public enum class NotificationQueuePolicy {
    /** The newcomer takes over immediately. The default, now stated. */
    Replace,

    /**
     * The newcomer waits until the current banner goes away.
     *
     * The one policy where the host holds a banner your state no longer names:
     * it is timing, not ownership — the callbacks still reach you, and the queue
     * is capped so an emission loop cannot fill it.
     */
    Enqueue,

    /** The newcomer is discarded. For state that is re-emitted anyway. */
    Drop,
}

/** Which edge a notification host anchors its banner to. */
public enum class NotificationAlignment {
    Top,
    Bottom,
}
