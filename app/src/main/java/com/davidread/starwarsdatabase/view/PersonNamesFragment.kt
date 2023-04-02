package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.davidread.starwarsdatabase.NavGraphSubDirections
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
     * Called when a person name is clicked in the list. It launches [PersonDetailsFragment] while
     * passing the id of the clicked person. If the master-detail layout is being used, then the
     * fragment is inflated next to the list instead of in its own screen.
     *
     * @param id Unique id of the person clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        binding.subNavHostFragment?.let {
            val action = NavGraphSubDirections.actionGlobalPersonDetailsFragment(id)
            it.findNavController().navigate(action)
        } ?: run {
            val action =
                PersonNamesFragmentDirections.actionPersonNamesFragmentToPersonDetailsFragment(id)
            findNavController().navigate(action)
        }
    }
}