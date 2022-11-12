package com.davidread.starwarsdatabase.viewmodel

import androidx.lifecycle.LiveData
import com.davidread.starwarsdatabase.model.PersonListItem

/**
 * Defines the structure of [PeopleListFragmentViewModelImpl].
 */
interface PeopleListFragmentViewModel {
    val personListItems: LiveData<List<PersonListItem>>
}