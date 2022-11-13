package com.davidread.starwarsdatabase.viewmodel

import androidx.lifecycle.LiveData
import com.davidread.starwarsdatabase.model.view.PersonListItem

/**
 * Defines the structure of [PeopleListFragmentViewModelImpl].
 */
interface PeopleListFragmentViewModel {
    val personListItemsLiveData: LiveData<List<PersonListItem>>
    fun getPeople(page: Int)
}