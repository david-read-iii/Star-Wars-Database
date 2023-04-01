package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.davidread.starwarsdatabase.viewmodel.ResourceNamesViewModel
import com.davidread.starwarsdatabase.viewmodel.VehicleNamesViewModelImpl

/**
 * Fragment representing a list of vehicle names.
 */
class VehicleNamesFragment : ResourceNamesFragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    override val viewModel: ResourceNamesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[VehicleNamesViewModelImpl::class.java]
    }

    /**
     * Called when a vehicle name is clicked in the list.
     *
     * @param id Unique id of the vehicle clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        val action =
            VehicleNamesFragmentDirections.actionVehicleNamesFragmentToVehicleDetailsFragment(id)
        findNavController().navigate(action)
    }
}