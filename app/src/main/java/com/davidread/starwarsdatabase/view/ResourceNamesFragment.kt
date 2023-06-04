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
import com.davidread.starwarsdatabase.databinding.FragmentResourceNamesBinding
import com.davidread.starwarsdatabase.di.ApplicationController
import com.davidread.starwarsdatabase.viewmodel.ResourceNamesViewModel
import javax.inject.Inject

/**
 * Fragment representing any list of resource names.
 */
abstract class ResourceNamesFragment : Fragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment. To be initialized
     * by the inheriting class.
     */
    abstract val viewModel: ResourceNamesViewModel

    /**
     * Factory for instantiating `ViewModel` instances.
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Binding object for this fragment's layout.
     */
    val binding: FragmentResourceNamesBinding by lazy {
        FragmentResourceNamesBinding.inflate(layoutInflater)
    }

    /**
     * Adapts a resource names list dataset onto the [RecyclerView] in the UI.
     */
    private val resourceNamesAdapter = ResourceNamesAdapter(
        { id -> onResourceNameClick(id) },
        { onErrorRetryClick() }
    )

    /**
     * Detects whether the last view of the [RecyclerView] is visible. If so, it requests more
     * resource names from the [viewModel] to add onto the dataset from SWAPI. It also removes the
     * on scroll listener so duplicate requests are not made.
     */
    private val loadMoreResourceNamesOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val isLastItemVisible = lastVisibleItemPosition == totalItemCount - 1

            if (isLastItemVisible) {
                recyclerView.removeOnScrollListener(this)
                viewModel.getResourceNames(viewModel.nextPage)
            }
        }
    }

    /**
     * Called when a resource name is clicked in the list. To be defined by the inheriting class.
     *
     * @param id Unique id of the resource clicked in the list.
     */
    abstract fun onResourceNameClick(id: Int)

    /**
     * Invoked when this fragment is attached to it's associated activity. It just requests
     * dependency injection.
     */
    override fun onAttach(context: Context) {
        (activity?.application as ApplicationController).applicationGraph.inject(this)
        super.onAttach(context)
    }

    /**
     * Invoked when this fragment's view is to be created. It initializes this fragment's binding,
     * sets up an observer to the [ResourceNamesAdapter]'s dataset, and returns the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.onFragmentCreateView(resources.configuration.screenWidthDp)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ResourceNamesFragment.viewModel
            resourceNamesList.apply {
                adapter = resourceNamesAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
        setupObservers()
        return binding.root
    }

    /**
     * Sets up observers for the fragment.
     */
    private fun setupObservers() {
        viewModel.resourceNamesLiveData.observe(viewLifecycleOwner) { resourceNames ->
            // Deep copy required otherwise DiffCallback will not notify adapter of changes.
            val resourceNamesDeepCopy = resourceNames.map { it.copySealedObject() }
            resourceNamesAdapter.submitList(resourceNamesDeepCopy)
        }

        viewModel.smoothScrollToPositionInListLiveData.observe(viewLifecycleOwner) { position ->
            binding.resourceNamesList.smoothScrollToPosition(position)
        }

        viewModel.isLoadMoreResourceNamesOnScrollListenerEnabledLiveData.observe(viewLifecycleOwner) { isLoadMoreResourceNamesOnScrollListenerEnabled ->
            if (isLoadMoreResourceNamesOnScrollListenerEnabled) {
                binding.resourceNamesList.apply {
                    clearOnScrollListeners()
                    addOnScrollListener(loadMoreResourceNamesOnScrollListener)
                }
            } else {
                binding.resourceNamesList.clearOnScrollListeners()
            }
        }
    }

    /**
     * Called when the retry button of an error item is clicked in the list. It requests more
     * people from the [viewModel] to be added onto the dataset from SWAPI.
     */
    private fun onErrorRetryClick() {
        viewModel.getResourceNames(viewModel.nextPage)
    }
}