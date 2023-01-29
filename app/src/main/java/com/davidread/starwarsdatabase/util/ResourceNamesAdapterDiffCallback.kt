package com.davidread.starwarsdatabase.util

import androidx.recyclerview.widget.DiffUtil
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem

/**
 * Utility class provided to [com.davidread.starwarsdatabase.view.ResourceNamesAdapter] that handles
 * updating the `RecyclerView` when a new dataset is submitted to the adapter.
 */
class ResourceNamesAdapterDiffCallback : DiffUtil.ItemCallback<ResourceNameListItem>() {

    /**
     * Called to check whether two objects represent the same item.
     */
    override fun areItemsTheSame(
        oldItem: ResourceNameListItem,
        newItem: ResourceNameListItem
    ): Boolean =
        if (oldItem is ResourceNameListItem.ResourceName && newItem is ResourceNameListItem.ResourceName) {
            // Two ResourceNames with the same id represent the same item.
            oldItem.id == newItem.id
        } else {
            // Two Loadings or two Errors represent the same item.
            oldItem::class == newItem::class
        }

    /**
     * Called to check if two items have the same data. List items contain static content, so this
     * function defaults to `true`.
     */
    override fun areContentsTheSame(
        oldItem: ResourceNameListItem,
        newItem: ResourceNameListItem
    ): Boolean =
        true
}