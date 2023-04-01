package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.davidread.starwarsdatabase.viewmodel.ResourceDetailsViewModel
import com.davidread.starwarsdatabase.viewmodel.VehicleDetailsViewModelImpl

/**
 * Fragment representing a list of vehicle details.
 */
class VehicleDetailsFragment : ResourceDetailsFragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    override val viewModel: ResourceDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[VehicleDetailsViewModelImpl::class.java]
    }

    /**
     * Id of the resource represented by this fragment.
     */
    override val resourceId: Int by lazy { arguments.id }

    /**
     * Arguments passed into this fragment.
     */
    private val arguments: VehicleDetailsFragmentArgs by navArgs()
}