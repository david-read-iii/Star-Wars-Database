package com.davidread.starwarsdatabase.viewmodel

import androidx.lifecycle.LiveData
import com.davidread.starwarsdatabase.model.view.PersonListItem

/**
 * Defines the structure of [PeopleViewModelImpl].
 */
interface PeopleViewModel {
    val personListItemsLiveData: LiveData<List<PersonListItem>>
    val isAllPersonListItemsRequestedLiveData: LiveData<Boolean>
    var selectedPersonItemPosition: Int?
    fun getPeople(page: Int)
}