package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.davidread.starwarsdatabase.viewmodel.PlanetDetailsViewModelImpl
import com.davidread.starwarsdatabase.viewmodel.ResourceDetailsViewModel

/**
 * Fragment representing a list of planet details.
 */
class PlanetDetailsFragment : ResourceDetailsFragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    override val viewModel: ResourceDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PlanetDetailsViewModelImpl::class.java]
    }

    /**
     * Id of the resource represented by this fragment.
     */
    override val resourceId: Int by lazy { arguments.id }

    /**
     * Arguments passed into this fragment.
     */
    private val arguments: PlanetDetailsFragmentArgs by navArgs()
}