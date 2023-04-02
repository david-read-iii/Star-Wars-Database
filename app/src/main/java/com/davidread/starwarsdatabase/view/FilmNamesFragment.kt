package com.davidread.starwarsdatabase.view

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
     * Called when a film name is clicked in the list. It launches [FilmDetailsFragment] while
     * passing the id of the clicked film. If the master-detail layout is being used, then the
     * fragment is inflated next to the list instead of in its own screen.
     *
     * @param id Unique id of the film clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        binding.subNavHostFragment?.let {
            val action = NavGraphSubDirections.actionGlobalFilmDetailsFragment(id)
            it.findNavController().navigate(action)
        } ?: run {
            val action =
                FilmNamesFragmentDirections.actionFilmNamesFragmentToFilmDetailsFragment(id)
            findNavController().navigate(action)
        }
    }
}