package com.thecode.aestheticdialogs.components.selection.models

import androidx.compose.runtime.Immutable
import com.thecode.aestheticdialogs.model.DialogDismissBehavior

/**
 * One row of a selection dialog.
 *
 * Holds only primitives, so the item genuinely satisfies the `@Immutable`
 * contract and lazy list rows skip recomposition when their neighbours change.
 *
 * [id] is the caller's identifier — a language tag, a database key — and is what
 * comes back in [SelectionDialogSignal.ItemClicked]. The library never gives an
 * item an identity of its own.
 */
@Immutable
public data class SelectionItem(
    val id: String,
    val label: String,
    val supportingText: String? = null,
    val enabled: Boolean = true,
)

/**
 * The visual state of a selection dialog.
 *
 * The dialog renders a list and reports taps. It does not own the selection, it
 * does not filter the list, and it does not sort it: [items] is what you want
 * shown, in the order you want it, and [searchQuery] is the text currently in
 * the field. Filtering is a product decision — case folding, accent folding,
 * fuzzy matching, remote search — and a UI library that guessed would be wrong
 * for most callers and impossible to override for the rest.
 *
 * Note that the enclosing models are *not* annotated `@Immutable`: they hold a
 * `List`, which Compose cannot prove is immutable, and annotating them would be
 * a promise the type system does not back. The cost is one recomposition of the
 * dialog when its parent recomposes; the rows themselves are keyed by [SelectionItem.id]
 * and skip individually.
 */
public sealed interface SelectionDialogUiModel {

    public val title: String
    public val items: List<SelectionItem>
    public val cancelLabel: String

    /** Text currently in the search field, or `null` for a dialog with no search. */
    public val searchQuery: String?

    /** Shown in place of the list when [items] is empty. */
    public val emptyText: String?
    public val dismissBehavior: DialogDismissBehavior

    /**
     * Pick one.
     *
     * With [confirmLabel] set the choice is committed by the button, which is
     * what you want when the selection is expensive to apply. Leave it `null`
     * and the dialog has no confirm button: the caller is expected to close the
     * dialog on [SelectionDialogSignal.ItemClicked], the "tap to choose"
     * behaviour of a language or sort-order picker.
     */
    public data class Single(
        override val title: String,
        override val items: List<SelectionItem>,
        val selectedId: String?,
        override val cancelLabel: String,
        val confirmLabel: String? = null,
        override val searchQuery: String? = null,
        override val emptyText: String? = null,
        val isConfirmEnabled: Boolean = true,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : SelectionDialogUiModel

    /**
     * Pick any number.
     *
     * Always confirmed: multi-selection with no commit step leaves the user
     * unsure whether their last tap counted.
     */
    public data class Multiple(
        override val title: String,
        override val items: List<SelectionItem>,
        val selectedIds: Set<String>,
        val confirmLabel: String,
        override val cancelLabel: String,
        override val searchQuery: String? = null,
        override val emptyText: String? = null,
        val isConfirmEnabled: Boolean = true,
        override val dismissBehavior: DialogDismissBehavior = DialogDismissBehavior.Default,
    ) : SelectionDialogUiModel
}

/**
 * What the user did with a selection dialog.
 *
 * [ItemClicked] reports a tap, not a new selection: the caller owns the selected
 * set and decides whether a tap adds, removes or replaces. That is the
 * difference between a design system and a state machine.
 */
public sealed interface SelectionDialogSignal {
    public data class ItemClicked(val id: String) : SelectionDialogSignal
    public data class SearchQueryChanged(val query: String) : SelectionDialogSignal
    public data object Confirmed : SelectionDialogSignal
    public data object Cancelled : SelectionDialogSignal
    public data object Dismissed : SelectionDialogSignal
}
