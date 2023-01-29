package com.davidread.starwarsdatabase.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davidread.starwarsdatabase.databinding.ListItemDetailBinding
import com.davidread.starwarsdatabase.model.view.ResourceDetailListItem

/**
 * Binds a [List] of [ResourceDetailListItem] into a set of views that are displayed within a
 * [RecyclerView].
 *
 * @property resourceDetails Data source for the list.
 */
class DetailListAdapter(private val resourceDetails: List<ResourceDetailListItem>) :
    RecyclerView.Adapter<DetailListAdapter.DetailViewHolder>() {

    /**
     * Called when [RecyclerView] needs a new [DetailViewHolder] to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemDetailBinding.inflate(inflater, parent, false)
        return DetailViewHolder(binding)
    }

    /**
     * Called by [RecyclerView] to bind data to the [DetailViewHolder] to reflect the item at
     * the given position.
     */
    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
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
    class DetailViewHolder(private val binding: ListItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the view held by this [DetailViewHolder].
         */
        fun bind(resourceDetail: ResourceDetailListItem) {
            binding.resourceDetail = resourceDetail
        }
    }
}