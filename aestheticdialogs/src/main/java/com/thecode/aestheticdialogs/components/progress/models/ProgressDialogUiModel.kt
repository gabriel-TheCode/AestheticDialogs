package com.thecode.aestheticdialogs.components.progress.models

import androidx.compose.runtime.Immutable
import com.thecode.aestheticdialogs.foundation.DialogTone

/**
 * The visual state of a progress dialog: work is happening and the user has to
 * wait for it.
 *
 * `DialogAction.loading` covers a button that is busy; this covers a screen that
 * is. The two are not the same thing, and "Uploading 12 of 24" had nowhere to
 * live before.
 *
 * The dialog holds no timer and no counter: [Determinate.progress] is a value in
 * your state, like everything else the library renders.
 */
public sealed interface ProgressDialogUiModel {

    public val title: String
    public val message: String?
    public val tone: DialogTone

    /** Label of the cancel action. `null` leaves the dialog with no way out at all. */
    public val cancelLabel: String?

    /** Work whose end is not predictable: a request, a handshake, a search. */
    @Immutable
    public data class Default(
        override val title: String,
        override val message: String? = null,
        override val tone: DialogTone = DialogTone.Neutral,
        override val cancelLabel: String? = null,
    ) : ProgressDialogUiModel

    /**
     * Work with a known end: an upload, an export, a batch.
     *
     * @property progress how much is done, from `0f` to `1f`. Values outside the
     *   range are clamped rather than rejected, because a progress dialog is the
     *   worst possible place to throw.
     * @property progressLabel the count in your own words — "12 of 24", "3.4 MB
     *   of 12 MB". The library will not invent a format for a number it cannot
     *   localise.
     */
    @Immutable
    public data class Determinate(
        override val title: String,
        val progress: Float,
        override val message: String? = null,
        val progressLabel: String? = null,
        override val tone: DialogTone = DialogTone.Neutral,
        override val cancelLabel: String? = null,
    ) : ProgressDialogUiModel
}
