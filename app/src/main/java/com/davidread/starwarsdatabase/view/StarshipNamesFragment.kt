package com.davidread.starwarsdatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.davidread.starwarsdatabase.NavGraphSubDirections
import com.davidread.starwarsdatabase.viewmodel.ResourceNamesViewModel
import com.davidread.starwarsdatabase.viewmodel.StarshipNamesViewModelImpl

/**
 * Fragment representing a list of starship names.
 */
class StarshipNamesFragment : ResourceNamesFragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    override val viewModel: ResourceNamesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[StarshipNamesViewModelImpl::class.java]
    }

    /**
     * Invoked when this fragment's view is to be created.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupObservers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * Sets up observers for the fragment.
     */
    private fun setupObservers() {
        viewModel.apply {
            onNavigateToDetailsFragmentLiveData.observe(viewLifecycleOwner) { id ->
                val action =
                    StarshipNamesFragmentDirections.actionStarshipNamesFragmentToStarshipDetailsFragment(
                        id
                    )
                findNavController().navigate(action)
            }

            onShowDetailsFragmentInSubNavHostFragmentLiveData.observe(viewLifecycleOwner) { id ->
                val action = NavGraphSubDirections.actionGlobalStarshipDetailsFragment(id)
                binding.subNavHostFragment?.findNavController()?.navigate(action)
            }
        }
    }
}