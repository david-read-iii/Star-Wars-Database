package com.davidread.starwarsdatabase.view

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.davidread.starwarsdatabase.viewmodel.ResourceDetailsViewModel
import com.davidread.starwarsdatabase.viewmodel.StarshipDetailsViewModelImpl

/**
 * Fragment representing a list of starship details.
 */
class StarshipDetailsFragment : ResourceDetailsFragment() {

    /**
     * Exposes state to the UI and encapsulates business logic for this fragment.
     */
    override val viewModel: ResourceDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[StarshipDetailsViewModelImpl::class.java]
    }

    /**
     * Id of the resource represented by this fragment.
     */
    override val resourceId: Int by lazy { arguments.id }

    /**
     * Arguments passed into this fragment.
     */
    private val arguments: FilmDetailsFragmentArgs by navArgs()
}