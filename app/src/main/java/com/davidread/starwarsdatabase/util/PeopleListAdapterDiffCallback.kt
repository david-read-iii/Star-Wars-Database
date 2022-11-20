package com.davidread.starwarsdatabase.util

import androidx.recyclerview.widget.DiffUtil
import com.davidread.starwarsdatabase.model.view.PersonListItem

/**
 * Utility class provided to [com.davidread.starwarsdatabase.view.PeopleListAdapter] that handles
 * updating the `RecyclerView` when a new dataset is submitted to the adapter.
 */
class PeopleListAdapterDiffCallback : DiffUtil.ItemCallback<PersonListItem>() {

    /**
     * Called to check whether two objects represent the same item.
     */
    override fun areItemsTheSame(oldItem: PersonListItem, newItem: PersonListItem): Boolean =
        if (oldItem is PersonListItem.PersonItem && newItem is PersonListItem.PersonItem) {
            // Two PersonItems with the same id represent the same item.
            oldItem.id == newItem.id
        } else {
            // Two LoadingItems or two ErrorItems represent the same item.
            oldItem::class == newItem::class
        }

    /**
     * Called to check if two items have the same data. Items have static content, so this function
     * defaults to `true`.
     */
    override fun areContentsTheSame(oldItem: PersonListItem, newItem: PersonListItem): Boolean =
        true
}