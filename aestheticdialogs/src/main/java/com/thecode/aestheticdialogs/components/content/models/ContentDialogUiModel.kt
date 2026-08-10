package com.thecode.aestheticdialogs.components.content.models

import androidx.compose.runtime.Immutable
import com.thecode.aestheticdialogs.model.DialogAction
import com.thecode.aestheticdialogs.model.DialogDismissBehavior

/**
 * The visual state of a rich content dialog.
 *
 * This is the library's escape hatch, and it is a narrow one on purpose. The
 * caller owns the middle of the dialog; AestheticDialogs still owns the window,
 * the adaptive width, the scrim, the dismissal contract, the header, the action
 * row and the accessibility pane. A dialog that handed all of that over would
 * not be an escape hatch, it would be a `Dialog {}` with extra steps — and every
 * one written that way is another surface that drifts from the design system.
 */
public sealed interface ContentDialogUiModel {

    @Immutable
    public data class Default(
        val title: String,
        val subtitle: String? = null,
        val primaryAction: DialogAction? = null,
        val secondaryAction: DialogAction? = null,
        val showCloseButton: Boolean = true,
        /**
         * Whether the frame scrolls your content for you.
         *
         * Set it to `false` when the slot contains its own scrolling container —
         * a `LazyColumn`, a pager — because nesting two vertical scrollers is a
         * measurement error rather than a preference.
         */
        val scrollContent: Boolean = true,
        val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : ContentDialogUiModel
}

/** What the user did with a rich content dialog. */
public sealed interface ContentDialogSignal {
    public data object PrimaryActionClicked : ContentDialogSignal
    public data object SecondaryActionClicked : ContentDialogSignal
    public data object Dismissed : ContentDialogSignal
}
