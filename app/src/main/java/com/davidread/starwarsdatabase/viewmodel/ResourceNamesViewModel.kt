package com.davidread.starwarsdatabase.viewmodel

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem

/**
 * Defines the structure of any `{ResourceName}NamesViewModelImpl`.
 */
interface ResourceNamesViewModel {
    val resourceNamesLiveData: LiveData<List<ResourceNameListItem>>
    val smoothScrollToPositionInListLiveData: LiveData<Int>
    val subNavHostFragmentVisibility: LiveData<Int>
    val onNavigateToDetailsFragmentLiveData: LiveData<Int>
    val onShowDetailsFragmentInSubNavHostFragmentLiveData: LiveData<Int>
    fun onFragmentCreateView(screenWidthDp: Int)
    fun onResourceNamesListScroll(recyclerView: RecyclerView)
    fun onResourceNameClick(@IntRange(from = 1) id: Int, screenWidthDp: Int)
    fun onErrorRetryClick()
}