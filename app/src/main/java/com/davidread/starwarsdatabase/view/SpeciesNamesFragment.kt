package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
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
     * Called when a species name is clicked in the list.
     *
     * @param id Unique id of the species clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        TODO("Not yet implemented")
    }
}