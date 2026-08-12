package com.thecode.aestheticdialogs.components.feedback.models

import androidx.compose.runtime.Immutable
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogDismissBehavior

/**
 * The visual state of a feedback dialog — the modal "here is what happened"
 * surface.
 *
 * Three shapes: a card that sits quietly on the surface, a gradient panel that
 * does not, and a single line for the confirmations neither of them deserves.
 *
 * The edge-anchored styles are banners rather than dialogs and live on
 * [com.thecode.aestheticdialogs.components.notification.models.NotificationUiModel].
 */
public sealed interface FeedbackDialogUiModel {

    public val title: String
    public val message: String?
    public val tone: DialogTone

    /** The single action. `null` leaves the dialog with only its dismiss gestures. */
    public val actionLabel: String?
    public val dismissBehavior: DialogDismissBehavior

    /**
     * A card on the dialog surface, with the tone carried by the mark and the
     * action button.
     */
    @Immutable
    public data class Default(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Info,
        override val actionLabel: String? = null,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : FeedbackDialogUiModel

    /**
     * One line, one action, and the mark shrunk to sit beside them.
     *
     * The small "are you sure" that [Default] over-dresses: a card the height of
     * a list row, for the confirmations that do not deserve a title, a paragraph
     * and a badge the size of a thumbnail.
     */
    @Immutable
    public data class Compact(
        override val title: String,
        override val tone: DialogTone = DialogTone.Neutral,
        override val actionLabel: String? = null,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : FeedbackDialogUiModel {
        /** A compact dialog is one line. There is nowhere for a message to go. */
        override val message: String? get() = null
    }

    /**
     * A gradient-filled panel: the loud one, for moments that deserve it.
     *
     * The gradient is derived from the tone, so all five tones are available and
     * a rebranded theme keeps them consistent.
     */
    @Immutable
    public data class Gradient(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Success,
        override val actionLabel: String? = null,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : FeedbackDialogUiModel
}
