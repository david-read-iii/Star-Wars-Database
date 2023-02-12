package com.davidread.starwarsdatabase.viewmodel

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import com.davidread.starwarsdatabase.model.view.ResourceDetailListItem

/**
 * Defines the structure of any `{ResourceName}DetailsViewModelImpl`.
 */
interface ResourceDetailsViewModel {
    val resourceDetailsLiveData: LiveData<List<ResourceDetailListItem>>
    val showLoadingLiveData: LiveData<Boolean>
    val showErrorLiveData: LiveData<Boolean>
    fun getResourceDetails(@IntRange(from = 1) id: Int)
}