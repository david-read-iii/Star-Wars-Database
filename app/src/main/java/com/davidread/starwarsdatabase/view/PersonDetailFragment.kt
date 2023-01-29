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
import com.davidread.starwarsdatabase.databinding.FragmentPersonDetailBinding
import com.davidread.starwarsdatabase.di.ApplicationController
import com.davidread.starwarsdatabase.viewmodel.PersonDetailsViewModel
import com.davidread.starwarsdatabase.viewmodel.PersonDetailsViewModelImpl
import javax.inject.Inject

/**
 * Fragment representing the detail view of a person.
 */
class PersonDetailFragment : Fragment() {

    /**
     * Factory for instantiating `ViewModel` instances.
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Binding object for this fragment's layout.
     */
    private val binding: FragmentPersonDetailBinding by lazy {
        FragmentPersonDetailBinding.inflate(layoutInflater)
    }

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    private val viewModel: PersonDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PersonDetailsViewModelImpl::class.java]
    }

    /**
     * Arguments passed into this fragment.
     */
    private val arguments: PersonDetailFragmentArgs by navArgs()

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
        binding.personDetailRetryButton.setOnClickListener { onRetryClick() }
        setupObservers()
        if (savedInstanceState == null) {
            viewModel.getPersonDetails(arguments.id)
        }
        return binding.root
    }

    /**
     * Sets up an observer to [ResourceDetailsAdapter]'s dataset, to this fragment's loading state,
     * and this fragment's error state.
     */
    private fun setupObservers() {
        viewModel.personDetailsLiveData.observe(viewLifecycleOwner) { personDetails ->
            binding.personDetailList.apply {
                adapter = ResourceDetailsAdapter(personDetails)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }

        viewModel.showLoadingLiveData.observe(viewLifecycleOwner) { showLoading ->
            binding.personDetailProgressBar.visibility = if (showLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModel.showErrorLiveData.observe(viewLifecycleOwner) { showError ->
            binding.personDetailErrorLayout.visibility = if (showError) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    /**
     * Called when the retry button is clicked when this fragment is in error mode. It requests the
     * details of the person from SWAPI again.
     */
    private fun onRetryClick() {
        viewModel.getPersonDetails(arguments.id)
    }
}