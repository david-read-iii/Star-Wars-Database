package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.davidread.starwarsdatabase.NavGraphSubDirections
import com.davidread.starwarsdatabase.viewmodel.PlanetNamesViewModelImpl
import com.davidread.starwarsdatabase.viewmodel.ResourceNamesViewModel

/**
 * Fragment representing a list of planet names.
 */
class PlanetNamesFragment : ResourceNamesFragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    override val viewModel: ResourceNamesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PlanetNamesViewModelImpl::class.java]
    }

    /**
     * Called when a planet name is clicked in the list. It launches [PlanetDetailsFragment] while
     * passing the id of the clicked planet. If the master-detail layout is being used, then the
     * fragment is inflated next to the list instead of in its own screen.
     *
     * @param id Unique id of the planet clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        viewModel.onResourceNameClick(id, resources.configuration.screenWidthDp)
        binding.subNavHostFragment?.let {
            val action = NavGraphSubDirections.actionGlobalPlanetDetailsFragment(id)
            it.findNavController().navigate(action)
        } ?: run {
            val action =
                PlanetNamesFragmentDirections.actionPlanetNamesFragmentToPlanetDetailsFragment(id)
            findNavController().navigate(action)
        }
    }
}