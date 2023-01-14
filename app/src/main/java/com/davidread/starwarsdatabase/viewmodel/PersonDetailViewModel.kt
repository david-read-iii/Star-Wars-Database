package com.davidread.starwarsdatabase.viewmodel

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import com.davidread.starwarsdatabase.model.view.DetailListItem

/**
 * Defines the structure of [PersonDetailViewModelImpl].
 */
interface PersonDetailViewModel {
    val personDetailListItemsLiveData: LiveData<List<DetailListItem>>
    val showLoadingLiveData: LiveData<Boolean>
    val showErrorLiveData: LiveData<Boolean>
    fun getPerson(@IntRange(from = 1) id: Int)
}