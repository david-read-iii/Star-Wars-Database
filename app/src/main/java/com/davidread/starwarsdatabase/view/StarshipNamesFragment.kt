package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
     * Called when a starship name is clicked in the list. Launches [StarshipDetailsFragment] while
     * passing the id of the clicked starship.
     *
     * @param id Unique id of the starship clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        val action =
            StarshipNamesFragmentDirections.actionStarshipNamesFragmentToStarshipDetailsFragment(id)
        findNavController().navigate(action)
    }
}