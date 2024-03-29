package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.davidread.starwarsdatabase.NavGraphSubDirections
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
     * Called when a vehicle name is clicked in the list. It launches [VehicleNamesFragment] while
     * passing the id of the clicked vehicle. If the master-detail layout is being used, then the
     * fragment is inflated next to the list instead of in its own screen.
     *
     * @param id Unique id of the vehicle clicked in the list.
     */
    override fun onResourceNameClick(id: Int) {
        viewModel.onResourceNameClick(id, resources.configuration.screenWidthDp)
        binding.subNavHostFragment?.let {
            val action = NavGraphSubDirections.actionGlobalVehicleDetailsFragment(id)
            it.findNavController().navigate(action)
        } ?: run {
            val action =
                VehicleNamesFragmentDirections.actionVehicleNamesFragmentToVehicleDetailsFragment(id)
            findNavController().navigate(action)
        }
    }
}