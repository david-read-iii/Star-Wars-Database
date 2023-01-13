package com.davidread.starwarsdatabase.viewmodel

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import com.davidread.starwarsdatabase.model.view.PersonListItem

/**
 * Defines the structure of [PeopleListViewModelImpl].
 */
interface PeopleListViewModel {
    val personListItemsLiveData: LiveData<List<PersonListItem>>
    val isAllPersonListItemsRequestedLiveData: LiveData<Boolean>
    fun getPeople(@IntRange(from = 1) page: Int)
}