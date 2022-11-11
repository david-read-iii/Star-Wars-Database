package com.davidread.starwarsdatabase.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davidread.starwarsdatabase.databinding.ListItemErrorBinding
import com.davidread.starwarsdatabase.databinding.ListItemLoadingBinding
import com.davidread.starwarsdatabase.databinding.ListItemPersonBinding
import com.davidread.starwarsdatabase.model.PersonListItem
import com.davidread.starwarsdatabase.view.PeopleListAdapter.ViewType

/**
 * Binds the [personListItems] dataset into a set of views that are displayed within a
 * [RecyclerView].
 *
 * @param personListItems Dataset for the adapter.
 * @param onPersonItemClick Function to invoke when a view of type [ViewType.PERSON_ITEM] is
 * clicked.
 * @param onErrorItemRetryClick Function to invoke when the retry button of a view of type
 * [ViewType.ERROR_ITEM] is clicked.
 */
class PeopleListAdapter(
    private val personListItems: List<PersonListItem>,
    private val onPersonItemClick: (id: Int) -> Unit,
    private val onErrorItemRetryClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Called when [RecyclerView] needs a new [RecyclerView.ViewHolder] of the given type to
     * represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.PERSON_ITEM.ordinal -> {
                val binding = ListItemPersonBinding.inflate(inflater, parent, false)
                PersonViewHolder(binding)
            }
            ViewType.LOADING_ITEM.ordinal -> {
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
            is PersonViewHolder -> {
                val personItem = personListItems[position] as PersonListItem.PersonItem
                holder.bind(personItem, onPersonItemClick)
            }
            is ErrorViewHolder -> {
                holder.bind(onErrorItemRetryClick)
            }
        }
    }

    /**
     * Total number of items in the dataset.
     */
    override fun getItemCount(): Int = personListItems.size

    /**
     * Returns the view type of the item at the given position.
     */
    override fun getItemViewType(position: Int): Int = when (personListItems[position]) {
        is PersonListItem.PersonItem -> ViewType.PERSON_ITEM.ordinal
        is PersonListItem.LoadingItem -> ViewType.LOADING_ITEM.ordinal
        is PersonListItem.ErrorItem -> ViewType.ERROR_ITEM.ordinal
    }

    /**
     * The possible view types of items.
     */
    private enum class ViewType {
        PERSON_ITEM, LOADING_ITEM, ERROR_ITEM
    }

    /**
     * Describes a view of type [ViewType.PERSON_ITEM] and metadata about its place within the
     * [RecyclerView].
     */
    private class PersonViewHolder(private val binding: ListItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the view held by this [PersonViewHolder].
         */
        fun bind(personItem: PersonListItem.PersonItem, onPersonItemClick: (id: Int) -> Unit) {
            binding.apply {
                this.personItem = personItem
                root.setOnClickListener { onPersonItemClick(personItem.id) }
            }
        }
    }

    /**
     * Describes a view of type [ViewType.LOADING_ITEM] and metadata about its place within the
     * [RecyclerView].
     */
    private class LoadingViewHolder(binding: ListItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Describes a view of type [ViewType.ERROR_ITEM] and metadata about its place within the
     * [RecyclerView].
     */
    private class ErrorViewHolder(private val binding: ListItemErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the view held by this [ErrorViewHolder].
         */
        fun bind(onErrorItemRetryClick: () -> Unit) {
            binding.retryButton.setOnClickListener { onErrorItemRetryClick() }
        }
    }
}