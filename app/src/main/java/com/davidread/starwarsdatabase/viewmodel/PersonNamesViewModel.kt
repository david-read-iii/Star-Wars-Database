package com.davidread.starwarsdatabase.viewmodel

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem

/**
 * Defines the structure of [PersonNamesViewModelImpl].
 */
interface PersonNamesViewModel {
    val personNamesLiveData: LiveData<List<ResourceNameListItem>>
    val isAllPersonNamesRequestedLiveData: LiveData<Boolean>
    @setparam:IntRange(from = 1)
    var nextPage: Int
    fun getPersonNames(@IntRange(from = 1) page: Int)
}