package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
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
     * Called when a film name is clicked in the list.
     *
     * @param id Unique id of the film clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        // TODO: Launch FilmDetailsFragment.
    }
}