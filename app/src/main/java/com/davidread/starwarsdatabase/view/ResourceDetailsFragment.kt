package com.davidread.starwarsdatabase.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.davidread.starwarsdatabase.databinding.FragmentResourceDetailsBinding
import com.davidread.starwarsdatabase.di.ApplicationController
import com.davidread.starwarsdatabase.viewmodel.ResourceDetailsViewModel
import com.davidread.starwarsdatabase.viewmodel.PersonDetailsViewModelImpl
import javax.inject.Inject

/**
 * Fragment representing any list of resource details.
 */
class ResourceDetailsFragment : Fragment() {

    /**
     * Factory for instantiating `ViewModel` instances.
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Binding object for this fragment's layout.
     */
    private val binding: FragmentResourceDetailsBinding by lazy {
        FragmentResourceDetailsBinding.inflate(layoutInflater)
    }

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    private val viewModel: ResourceDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PersonDetailsViewModelImpl::class.java]
    }

    /**
     * Arguments passed into this fragment.
     */
    private val arguments: ResourceDetailsFragmentArgs by navArgs()

    /**
     * Invoked when this fragment is attached to it's associated activity. It just requests
     * dependency injection.
     */
    override fun onAttach(context: Context) {
        (activity?.application as ApplicationController).applicationGraph.inject(this)
        super.onAttach(context)
    }

    /**
     * Invoked when this fragment's view is to be created. It configures click listeners, sets up
     * observers, initializes the `ViewModel`, and returns the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            resourceDetailsViewModel = viewModel
            resourceDetailsRetryButton.setOnClickListener { onErrorRetryClick() }
        }
        setupObserver()
        if (savedInstanceState == null) {
            viewModel.getResourceDetails(arguments.id)
        }
        return binding.root
    }

    /**
     * Sets up observers for the fragment.
     */
    private fun setupObserver() {
        // Updates the adapter with the dataset when it becomes available.
        viewModel.resourceDetailsLiveData.observe(viewLifecycleOwner) { resourceDetails ->
            binding.resourceDetailsList.apply {
                adapter = ResourceDetailsAdapter(resourceDetails)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    /**
     * Called when the retry button is clicked when this fragment is in error mode. It requests the
     * details of the resource from SWAPI again.
     */
    private fun onErrorRetryClick() {
        viewModel.getResourceDetails(arguments.id)
    }
}