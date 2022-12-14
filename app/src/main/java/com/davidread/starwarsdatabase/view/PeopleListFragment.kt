package com.davidread.starwarsdatabase.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidread.starwarsdatabase.databinding.FragmentPeopleListBinding
import com.davidread.starwarsdatabase.di.ApplicationController
import com.davidread.starwarsdatabase.model.view.PersonListItem
import com.davidread.starwarsdatabase.viewmodel.PeopleListFragmentViewModel
import com.davidread.starwarsdatabase.viewmodel.PeopleListFragmentViewModelImpl
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

/**
 * Fragment representing a list of entries in the people category.
 */
class PeopleListFragment : Fragment() {

    /**
     * Factory for instantiating `ViewModel` instances.
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Binding object for this fragment's layout.
     */
    private val binding: FragmentPeopleListBinding by lazy {
        FragmentPeopleListBinding.inflate(layoutInflater)
    }

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    private val viewModel: PeopleListFragmentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PeopleListFragmentViewModelImpl::class.java]
    }

    /**
     * Adapts a people list dataset onto the [RecyclerView] in the UI.
     */
    private val peopleListAdapter = PeopleListAdapter(
        { id -> onPersonItemClick(id) },
        { onErrorItemRetryClick() }
    )

    /**
     * Detects whether the last view of the [RecyclerView] is visible. If so, it requests more
     * people from the [viewModel] to add onto the dataset from SWAPI. It also removes the on scroll
     * listener so duplicate requests are not made.
     */
    private val loadMorePeopleOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val isLastItemVisible = lastVisibleItemPosition == totalItemCount - 1

            if (isLastItemVisible) {
                recyclerView.removeOnScrollListener(this)
                val page = ((totalItemCount - 1) / 10) + 2
                viewModel.getPeople(page)
            }
        }
    }

    /**
     * Invoked when this fragment is attached to it's associated activity. It just requests
     * dependency injection.
     */
    override fun onAttach(context: Context) {
        (activity?.application as ApplicationController).applicationGraph.inject(this)
        super.onAttach(context)
    }

    /**
     * Invoked when this fragment's view is to be created. It initializes the [RecyclerView], sets
     * up an observer to the [PeopleListAdapter]'s dataset, and returns the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.peopleList.apply {
            adapter = peopleListAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        setupObserver()
        return binding.root
    }

    /**
     * Sets up an observer to the [PeopleListAdapter]'s dataset. It sets up two observers. The first
     * one is responsible for updating the adapter with the latest dataset from the [viewModel]. The
     * second is responsible for removing the scroll listener from the [RecyclerView] when no more
     * entries may be fetched for the dataset.
     */
    private fun setupObserver() {
        viewModel.personListItemsLiveData.observe(viewLifecycleOwner) { personListItems ->
            /* Shallow copy of the dataset needed for DiffCallback to calculate diffs of the old and
             * new lists. */
            peopleListAdapter.submitList(personListItems.toList())

            when (personListItems.lastOrNull()) {
                is PersonListItem.PersonItem -> {
                    binding.peopleList.addOnScrollListener(loadMorePeopleOnScrollListener)
                }
                is PersonListItem.LoadingItem -> {
                    binding.peopleList.smoothScrollToPosition(personListItems.lastIndex)
                }
                else -> {}
            }
        }

        viewModel.isAllPersonListItemsRequestedLiveData.observe(viewLifecycleOwner) { isAllPersonListItemsRequested ->
            if (isAllPersonListItemsRequested) {
                binding.peopleList.removeOnScrollListener(loadMorePeopleOnScrollListener)
            }
        }
    }

    /**
     * Called when a person item is clicked in the list. Does nothing for now.
     *
     * @param id Id of the person item clicked.
     */
    private fun onPersonItemClick(id: Int) {
        Snackbar.make(binding.root, "Person click with id=$id", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Called when the retry button of an error item is clicked in the list. It requests more
     * people from the [viewModel] to be added onto the dataset from SWAPI.
     */
    private fun onErrorItemRetryClick() {
        val page = ((peopleListAdapter.itemCount - 2) / 10) + 2
        viewModel.getPeople(page)
    }
}