package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
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
     * Called when a planet name is clicked in the list.
     *
     * @param id Unique id of the planet clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        TODO("Not yet implemented")
    }
}