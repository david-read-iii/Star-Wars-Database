package com.davidread.starwarsdatabase.viewmodel

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem

/**
 * Defines the structure of any `{ResourceName}NamesViewModelImpl`.
 */
interface ResourceNamesViewModel {
    val resourceNamesLiveData: LiveData<List<ResourceNameListItem>>
    val subNavHostFragmentVisibilityLiveData: LiveData<Int>
    val smoothScrollToPositionInListLiveData: LiveData<Int>
    val onNavigateToDetailsFragmentLiveData: LiveData<Int>
    val onShowDetailsFragmentInSubNavHostFragmentLiveData: LiveData<Int>
    fun onFragmentCreateView(screenWidthDp: Int)
    fun onResourceNamesListScroll(isLastItemVisible: Boolean)
    fun onResourceNameClick(@IntRange(from = 1) id: Int, screenWidthDp: Int)
    fun onErrorRetryClick()
}