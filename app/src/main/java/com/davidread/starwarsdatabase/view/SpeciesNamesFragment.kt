package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.davidread.starwarsdatabase.NavGraphSubDirections
import com.davidread.starwarsdatabase.viewmodel.ResourceNamesViewModel
import com.davidread.starwarsdatabase.viewmodel.SpeciesNamesViewModelImpl

/**
 * Fragment representing a list of species names.
 */
class SpeciesNamesFragment : ResourceNamesFragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    override val viewModel: ResourceNamesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SpeciesNamesViewModelImpl::class.java]
    }

    /**
     * Called when a species name is clicked in the list. It launches [SpeciesDetailsFragment] while
     * passing the id of the clicked species. If the master-detail layout is being used, then the
     * fragment is inflated next to the list instead of in its own screen.
     *
     * @param id Unique id of the species clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        viewModel.onResourceNameClick(id, resources.configuration.screenWidthDp)
        binding.subNavHostFragment?.let {
            val action = NavGraphSubDirections.actionGlobalSpeciesDetailsFragment(id)
            it.findNavController().navigate(action)
        } ?: run {
            val action =
                SpeciesNamesFragmentDirections.actionSpeciesNamesFragmentToSpeciesDetailsFragment(id)
            findNavController().navigate(action)
        }
    }
}