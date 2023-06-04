package com.davidread.starwarsdatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.davidread.starwarsdatabase.NavGraphSubDirections
import com.davidread.starwarsdatabase.viewmodel.FilmNamesViewModelImpl
import com.davidread.starwarsdatabase.viewmodel.ResourceNamesViewModel

/**
 * Fragment representing a list of film names.
 */
class FilmNamesFragment : ResourceNamesFragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    override val viewModel: ResourceNamesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FilmNamesViewModelImpl::class.java]
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
                    FilmNamesFragmentDirections.actionFilmNamesFragmentToFilmDetailsFragment(id)
                findNavController().navigate(action)
            }

            onShowDetailsFragmentInSubNavHostFragmentLiveData.observe(viewLifecycleOwner) { id ->
                val action = NavGraphSubDirections.actionGlobalFilmDetailsFragment(id)
                binding.subNavHostFragment?.findNavController()?.navigate(action)
            }
        }
    }
}