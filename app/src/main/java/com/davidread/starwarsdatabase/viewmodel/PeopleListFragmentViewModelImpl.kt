package com.davidread.starwarsdatabase.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**
 * Exposes state and encapsulates business logic related to the people category list.
 */
class PeopleListFragmentViewModelImpl @Inject constructor() : ViewModel(), PeopleListFragmentViewModel {
    override val testLiveData = MutableLiveData("This string is emitted from the viewmodel!")
}