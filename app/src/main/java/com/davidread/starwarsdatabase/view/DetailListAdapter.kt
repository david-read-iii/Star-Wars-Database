package com.davidread.starwarsdatabase.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davidread.starwarsdatabase.databinding.ListItemDetailBinding
import com.davidread.starwarsdatabase.model.view.DetailListItem

/**
 * Binds a [List] of [DetailListItem] into a set of views that are displayed within a
 * [RecyclerView].
 *
 * @property detailListItems Data source for the list.
 */
class DetailListAdapter(private val detailListItems: List<DetailListItem>) :
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
        val detailItem = detailListItems[position]
        holder.bind(detailItem)
    }

    /**
     * Returns the count of items in the data source.
     */
    override fun getItemCount(): Int = detailListItems.size

    /**
     * Describes a detail view and metadata about its place within the [RecyclerView].
     */
    class DetailViewHolder(private val binding: ListItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the view held by this [DetailViewHolder].
         */
        fun bind(detailItem: DetailListItem) {
            binding.detailItem = detailItem
        }
    }
}