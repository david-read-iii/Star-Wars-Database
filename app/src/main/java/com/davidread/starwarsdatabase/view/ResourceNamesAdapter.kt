package com.davidread.starwarsdatabase.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davidread.starwarsdatabase.databinding.ListItemErrorBinding
import com.davidread.starwarsdatabase.databinding.ListItemLoadingBinding
import com.davidread.starwarsdatabase.databinding.ListItemResourceNameBinding
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import com.davidread.starwarsdatabase.util.ResourceNamesAdapterDiffCallback
import com.davidread.starwarsdatabase.view.ResourceNamesAdapter.ViewType

/**
 * Binds a [List] of [ResourceNameListItem] dataset into a set of views that are displayed within a
 * [RecyclerView].
 *
 * @property onResourceNameClick Function to invoke when a view of type [ViewType.RESOURCE_NAME] is
 * clicked.
 * @property onErrorRetryClick Function to invoke when the retry button of a view of type
 * [ViewType.ERROR] is clicked.
 */
class ResourceNamesAdapter(
    private val onResourceNameClick: (id: Int) -> Unit,
    private val onErrorRetryClick: () -> Unit
) : ListAdapter<ResourceNameListItem, RecyclerView.ViewHolder>(ResourceNamesAdapterDiffCallback()) {

    /**
     * Called when [RecyclerView] needs a new [RecyclerView.ViewHolder] of the given type to
     * represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.RESOURCE_NAME.ordinal -> {
                val binding = ListItemResourceNameBinding.inflate(inflater, parent, false)
                ResourceNameViewHolder(binding)
            }
            ViewType.LOADING.ordinal -> {
                val binding = ListItemLoadingBinding.inflate(inflater, parent, false)
                LoadingViewHolder(binding)
            }
            else -> {
                val binding = ListItemErrorBinding.inflate(inflater, parent, false)
                ErrorViewHolder(binding)
            }
        }
    }

    /**
     * Called by [RecyclerView] to bind data to the [RecyclerView.ViewHolder] to reflect the item at
     * the given position.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ResourceNameViewHolder -> {
                val resourceName = getItem(position) as ResourceNameListItem.ResourceName
                holder.bind(resourceName, onResourceNameClick)
            }
            is ErrorViewHolder -> {
                holder.bind(onErrorRetryClick)
            }
        }
    }

    /**
     * Returns the view type of the item at the given position.
     */
    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is ResourceNameListItem.ResourceName -> ViewType.RESOURCE_NAME.ordinal
        is ResourceNameListItem.Loading -> ViewType.LOADING.ordinal
        is ResourceNameListItem.Error -> ViewType.ERROR.ordinal
    }

    /**
     * The possible view types of items.
     */
    enum class ViewType {
        RESOURCE_NAME, LOADING, ERROR
    }

    /**
     * Describes a view of type [ViewType.RESOURCE_NAME] and metadata about its place within the
     * [RecyclerView].
     */
    private class ResourceNameViewHolder(private val binding: ListItemResourceNameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the view held by this [ResourceNameViewHolder].
         */
        fun bind(
            resourceName: ResourceNameListItem.ResourceName,
            onResourceNameClick: (id: Int) -> Unit
        ) {
            binding.apply {
                this.resourceName = resourceName
                root.setOnClickListener {
                    onResourceNameClick(resourceName.id)
                }
            }
        }
    }

    /**
     * Describes a view of type [ViewType.LOADING] and metadata about its place within the
     * [RecyclerView].
     */
    private class LoadingViewHolder(binding: ListItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Describes a view of type [ViewType.ERROR] and metadata about its place within the
     * [RecyclerView].
     */
    private class ErrorViewHolder(private val binding: ListItemErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the view held by this [ErrorViewHolder].
         */
        fun bind(onErrorRetryClick: () -> Unit) {
            binding.retryButton.setOnClickListener { onErrorRetryClick() }
        }
    }
}