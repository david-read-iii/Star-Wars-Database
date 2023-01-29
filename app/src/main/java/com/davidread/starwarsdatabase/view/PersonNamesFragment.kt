package com.davidread.starwarsdatabase.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidread.starwarsdatabase.databinding.FragmentPersonNamesBinding
import com.davidread.starwarsdatabase.di.ApplicationController
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import com.davidread.starwarsdatabase.viewmodel.PersonNamesViewModel
import com.davidread.starwarsdatabase.viewmodel.PersonNamesViewModelImpl
import javax.inject.Inject

/**
 * Fragment representing a list of person names.
 */
class PersonNamesFragment : Fragment() {

    /**
     * Factory for instantiating `ViewModel` instances.
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Binding object for this fragment's layout.
     */
    private val binding: FragmentPersonNamesBinding by lazy {
        FragmentPersonNamesBinding.inflate(layoutInflater)
    }

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    private val viewModel: PersonNamesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PersonNamesViewModelImpl::class.java]
    }

    /**
     * Adapts a person names list dataset onto the [RecyclerView] in the UI.
     */
    private val personNamesAdapter = ResourceNamesAdapter(
        { id -> onPersonNameClick(id) },
        { onErrorRetryClick() }
    )

    /**
     * Detects whether the last view of the [RecyclerView] is visible. If so, it requests more
     * people from the [viewModel] to add onto the dataset from SWAPI. It also removes the on scroll
     * listener so duplicate requests are not made.
     */
    private val loadMorePersonNamesOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val isLastItemVisible = lastVisibleItemPosition == totalItemCount - 1

            if (isLastItemVisible) {
                recyclerView.removeOnScrollListener(this)
                val page = ((totalItemCount - 1) / 10) + 2
                viewModel.getPersonNames(page)
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
     * up an observer to the [ResourceNamesAdapter]'s dataset, and returns the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.personNamesList.apply {
            adapter = personNamesAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        setupObserver()
        return binding.root
    }

    /**
     * Invoked when this fragment's view is to be destroyed. It removes the on scroll listener so
     * duplicate requests don't happen when the user navigates back to this fragment.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding.personNamesList.removeOnScrollListener(loadMorePersonNamesOnScrollListener)
    }

    /**
     * Sets up an observer to the [ResourceNamesAdapter]'s dataset. It sets up two observers. The
     * first one is responsible for updating the adapter with the latest dataset from the
     * [viewModel]. The second is responsible for removing the scroll listener from the
     * [RecyclerView] when no more entries may be fetched for the dataset.
     */
    private fun setupObserver() {
        viewModel.personNamesLiveData.observe(viewLifecycleOwner) { personNames ->
            /* Shallow copy of the dataset needed for DiffCallback to calculate diffs of the old and
             * new lists. */
            personNamesAdapter.submitList(personNames.toList())

            when (personNames.lastOrNull()) {
                is ResourceNameListItem.ResourceName -> {
                    binding.personNamesList.addOnScrollListener(loadMorePersonNamesOnScrollListener)
                }
                is ResourceNameListItem.Loading -> {
                    binding.personNamesList.smoothScrollToPosition(personNames.lastIndex)
                }
                else -> {}
            }
        }

        viewModel.isAllPersonNamesRequestedLiveData.observe(viewLifecycleOwner) { isAllPersonNamesRequested ->
            if (isAllPersonNamesRequested) {
                binding.personNamesList.removeOnScrollListener(loadMorePersonNamesOnScrollListener)
            }
        }
    }

    /**
     * Called when a person name is clicked in the list. Launches [PersonDetailsFragment] while
     * passing the id of the clicked person.
     *
     * @param id Unique id of the person clicked in the list.
     */
    private fun onPersonNameClick(id: Int) {
        val action =
            PersonNamesFragmentDirections.actionPersonNamesFragmentToPersonDetailsFragment(id)
        findNavController().navigate(action)
    }

    /**
     * Called when the retry button of an error item is clicked in the list. It requests more
     * people from the [viewModel] to be added onto the dataset from SWAPI.
     */
    private fun onErrorRetryClick() {
        val page = ((personNamesAdapter.itemCount - 2) / 10) + 2
        viewModel.getPersonNames(page)
    }
}