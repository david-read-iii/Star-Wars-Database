package com.davidread.starwarsdatabase.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
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
     * Invoked when this fragment is attached to it's associated activity. It just requests
     * dependency injection.
     */
    override fun onAttach(context: Context) {
        (activity?.application as ApplicationController).applicationGraph.inject(this)
        super.onAttach(context)
    }

    /**
     * Invoked when this fragment's view is to be created. It initializes this fragment's binding,
     * sets up observers, and returns the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupBinding()
        setupObservers()
        viewModel.onFragmentCreateView(screenWidthDp = resources.configuration.screenWidthDp)
        return binding.root
    }

    /**
     * Invoked when this fragment's view is to be destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        destroyBinding()
    }

    /**
     * Sets up binding for the fragment.
     */
    private fun setupBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ResourceNamesFragment.viewModel
            resourceNamesList.apply {
                adapter = ResourceNamesAdapter(
                    onResourceNameClick = { id ->
                        this@ResourceNamesFragment.viewModel.onResourceNameClick(
                            id = id,
                            screenWidthDp = resources.configuration.screenWidthDp
                        )
                    },
                    onErrorRetryClick = { this@ResourceNamesFragment.viewModel.onErrorRetryClick() }
                )
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        this@ResourceNamesFragment.viewModel.onResourceNamesListScroll(recyclerView = recyclerView)
                    }
                })
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    /**
     * Destroys the binding for the fragment.
     */
    private fun destroyBinding() {
        binding.resourceNamesList.clearOnScrollListeners()
    }

    /**
     * Sets up observers for the fragment.
     */
    private fun setupObservers() {
        viewModel.resourceNamesLiveData.observe(viewLifecycleOwner) { resourceNames ->
            // Deep copy required otherwise DiffCallback will not notify adapter of changes.
            val resourceNamesDeepCopy = resourceNames.map { it.copySealedObject() }
            val adapter = binding.resourceNamesList.adapter as ResourceNamesAdapter
            adapter.submitList(resourceNamesDeepCopy)
        }

        viewModel.smoothScrollToPositionInListLiveData.observe(viewLifecycleOwner) { position ->
            binding.resourceNamesList.smoothScrollToPosition(position)
        }
    }
}