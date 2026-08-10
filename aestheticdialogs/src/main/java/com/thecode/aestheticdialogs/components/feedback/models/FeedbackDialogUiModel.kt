package com.thecode.aestheticdialogs.components.feedback.models

import androidx.compose.runtime.Immutable
import com.thecode.aestheticdialogs.foundation.DialogTone
import com.thecode.aestheticdialogs.model.DialogDismissBehavior

/**
 * The visual state of a feedback dialog — the modal "here is what happened"
 * surface.
 *
 * Two treatments: a card that sits quietly on the surface, and a colour-filled
 * panel that does not. Both carry a tone, a title, a message and one way out.
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
    public data class Flat(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Info,
        override val actionLabel: String? = null,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : FeedbackDialogUiModel

    /**
     * A gradient-filled panel: the loud one, for moments that deserve it.
     *
     * The gradient is derived from the tone, so all five tones are available and
     * a rebranded theme keeps them consistent.
     */
    @Immutable
    public data class Flash(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Success,
        override val actionLabel: String? = null,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : FeedbackDialogUiModel
}

/** What the user did with a feedback dialog. */
public sealed interface FeedbackDialogSignal {
    public data object ActionClicked : FeedbackDialogSignal
    public data object Dismissed : FeedbackDialogSignal
}
