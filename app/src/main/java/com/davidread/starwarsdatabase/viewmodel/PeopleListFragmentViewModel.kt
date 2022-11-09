package com.davidread.starwarsdatabase.viewmodel

import androidx.lifecycle.LiveData

/**
 * Defines the structure of [PeopleListFragmentViewModelImpl].
 */
interface PeopleListFragmentViewModel {
    val testLiveData: LiveData<String>
}