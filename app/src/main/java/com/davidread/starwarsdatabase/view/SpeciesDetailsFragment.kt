package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.davidread.starwarsdatabase.viewmodel.ResourceDetailsViewModel
import com.davidread.starwarsdatabase.viewmodel.SpeciesDetailsViewModelImpl

/**
 * Fragment representing a list of species details.
 */
class SpeciesDetailsFragment : ResourceDetailsFragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    override val viewModel: ResourceDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SpeciesDetailsViewModelImpl::class.java]
    }

    /**
     * Id of the resource represented by this fragment.
     */
    override val resourceId: Int by lazy { arguments.id }

    /**
     * Arguments passed into this fragment.
     */
    private val arguments: SpeciesDetailsFragmentArgs by navArgs()
}