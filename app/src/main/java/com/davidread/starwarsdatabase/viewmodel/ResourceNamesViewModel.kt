package com.davidread.starwarsdatabase.viewmodel

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem

/**
 * Defines the structure of any `{ResourceName}NamesViewModelImpl`.
 */
interface ResourceNamesViewModel {
    val resourceNamesLiveData: LiveData<List<ResourceNameListItem>>
    val isLoadMoreResourceNamesOnScrollListenerEnabledLiveData: LiveData<Boolean>
    val subNavHostFragmentVisibility: LiveData<Int>
    @setparam:IntRange(from = 1)
    var nextPage: Int
    fun onFragmentCreateView(screenWidthDp: Int)
    fun onResourceNameClick(@IntRange(from = 1) id: Int, screenWidthDp: Int)
    fun getResourceNames(@IntRange(from = 1) page: Int)
}