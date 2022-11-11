package com.davidread.starwarsdatabase.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidread.starwarsdatabase.model.PersonListItem
import javax.inject.Inject

/**
 * Exposes state and encapsulates business logic related to the people category list.
 */
class PeopleListFragmentViewModelImpl @Inject constructor() : ViewModel(),
    PeopleListFragmentViewModel {

    /**
     * Emits a [List] of [PersonListItem]s that should be displayed in the UI.
     */
    override val personListItems = MutableLiveData(getDummyList())

    /**
     * Returns a dummy [List] of [PersonListItem]s.
     */
    private fun getDummyList(): List<PersonListItem> = listOf(
        PersonListItem.PersonItem(1, "Luke Skywalker"),
        PersonListItem.PersonItem(2, "C-3P0"),
        PersonListItem.PersonItem(3, "R2-D2"),
        PersonListItem.PersonItem(4, "Darth Vader"),
        PersonListItem.PersonItem(5, "Leia Organa"),
        PersonListItem.PersonItem(6, "Owen Lars"),
        PersonListItem.PersonItem(7, "Beru Whitesun lars"),
        PersonListItem.PersonItem(8, "R5-D4"),
        PersonListItem.PersonItem(9, "Biggs Darklighter"),
        PersonListItem.PersonItem(10, "Obi-Wan Kenobi")
    )
}