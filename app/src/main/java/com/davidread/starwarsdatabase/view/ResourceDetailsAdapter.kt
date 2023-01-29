package com.davidread.starwarsdatabase.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davidread.starwarsdatabase.databinding.ListItemResourceDetailBinding
import com.davidread.starwarsdatabase.model.view.ResourceDetailListItem

/**
 * Binds a [List] of [ResourceDetailListItem] into a set of views that are displayed within a
 * [RecyclerView].
 *
 * @property resourceDetails Data source for the list.
 */
class ResourceDetailsAdapter(private val resourceDetails: List<ResourceDetailListItem>) :
    RecyclerView.Adapter<ResourceDetailsAdapter.ResourceDetailViewHolder>() {

    /**
     * Called when [RecyclerView] needs a new [ResourceDetailViewHolder] to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemResourceDetailBinding.inflate(inflater, parent, false)
        return ResourceDetailViewHolder(binding)
    }

    /**
     * Called by [RecyclerView] to bind data to the [ResourceDetailViewHolder] to reflect the item
     * at the given position.
     */
    override fun onBindViewHolder(holder: ResourceDetailViewHolder, position: Int) {
        val resourceDetail = resourceDetails[position]
        holder.bind(resourceDetail)
    }

    /**
     * Returns the count of items in the data source.
     */
    override fun getItemCount(): Int = resourceDetails.size

    /**
     * Describes a detail view and metadata about its place within the [RecyclerView].
     */
    class ResourceDetailViewHolder(private val binding: ListItemResourceDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the view held by this [ResourceDetailViewHolder].
         */
        fun bind(resourceDetail: ResourceDetailListItem) {
            binding.resourceDetail = resourceDetail
        }
    }
}