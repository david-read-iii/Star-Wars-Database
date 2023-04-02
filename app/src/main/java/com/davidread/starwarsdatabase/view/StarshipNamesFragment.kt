package com.davidread.starwarsdatabase.view

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
     * Called when a starship name is clicked in the list. It launches [StarshipDetailsFragment]
     * while passing the id of the clicked starship. If the master-detail layout is being used, then
     * the fragment is inflated next to the list instead of in its own screen.
     *
     * @param id Unique id of the starship clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        binding.subNavHostFragment?.let {
            val action = NavGraphSubDirections.actionGlobalStarshipDetailsFragment(id)
            it.findNavController().navigate(action)
        } ?: run {
            val action =
                StarshipNamesFragmentDirections.actionStarshipNamesFragmentToStarshipDetailsFragment(id)
            findNavController().navigate(action)
        }
    }
}