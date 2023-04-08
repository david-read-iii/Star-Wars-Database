package com.davidread.starwarsdatabase.viewmodel

import android.view.View
import androidx.annotation.IntRange
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Base class for `{ResourceName}NamesViewModelImpl` classes to extend. Contains common logic used
 * across all such `ViewModel`s.
 */
abstract class ResourceNamesViewModelImpl : ResourceNamesViewModel, ViewModel() {

    /**
     * Emits a [List] of [ResourceNameListItem]s that should be shown on the UI.
     */
    override val resourceNamesLiveData: MutableLiveData<List<ResourceNameListItem>> =
        MutableLiveData()

    /**
     * Whether all [ResourceNameListItem]s have been fetched from SWAPI.
     */
    override val isAllResourceNamesRequestedLiveData: MutableLiveData<Boolean> =
        MutableLiveData(false)

    /**
     * The view visibility of `subNavHostFragment`.
     */
    override val subNavHostFragmentVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)

    /**
     * Next page of resource names to fetch from SWAPI.
     */
    @IntRange(from = 1)
    override var nextPage: Int = 1

    /**
     * [MutableList] of [ResourceNameListItem]s to be stored/modified here. Emitted via its
     * `LiveData` each time it is updated.
     */
    val resourceNames: MutableList<ResourceNameListItem> = mutableListOf()

    /**
     * Container for managing resources used by `Disposable`s or their subclasses.
     */
    val disposable: CompositeDisposable = CompositeDisposable()

    /**
     * Called when this `ViewModel` is no longer used and will be destroyed. Clears any
     * subscriptions held by [disposable] to prevent this `ViewModel` from leaking.
     */
    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}