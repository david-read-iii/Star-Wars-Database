package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.davidread.starwarsdatabase.viewmodel.PersonNamesViewModelImpl
import com.davidread.starwarsdatabase.viewmodel.ResourceNamesViewModel

/**
 * Fragment representing a list of person names.
 */
class PersonNamesFragment : ResourceNamesFragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    override val viewModel: ResourceNamesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PersonNamesViewModelImpl::class.java]
    }

    /**
     * Called when a person name is clicked in the list. Launches [PersonDetailsFragment] while
     * passing the id of the clicked person.
     *
     * @param id Unique id of the person clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        val action =
            PersonNamesFragmentDirections.actionPersonNamesFragmentToPersonDetailsFragment(id)
        findNavController().navigate(action)
    }
}