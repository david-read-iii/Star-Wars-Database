package com.davidread.starwarsdatabase.viewmodel

import android.view.View
import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidread.starwarsdatabase.model.FragmentResourceNamesLayoutType
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
     * Whether the scroll listener is enabled.
     */
    override val isLoadMoreResourceNamesOnScrollListenerEnabledLiveData: MutableLiveData<Boolean> =
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

    /**
     * Called when the 'Fragment' invokes its 'onCreateView()' function. If the 'Fragment' is using
     * the single fragment layout, then set [android.R.attr.selectableItemBackground] on every
     * [ResourceNameListItem].
     *
     * @param screenWidthDp Screen width dp of the 'Fragment'.
     */
    override fun onFragmentCreateView(screenWidthDp: Int) {
        if (getFragmentLayoutType(screenWidthDp) == FragmentResourceNamesLayoutType.SINGLE_FRAGMENT) {
            setSelectableItemBackgroundOnAllResourceNames()
            resourceNamesLiveData.postValue(resourceNames)
        }
    }

    /**
     * Called when a resource name is clicked. If the device is using a master-detail fragment
     * layout, then it sets [android.R.attr.selectableItemBackground] on all [ResourceNameListItem]s
     * and then sets [android.R.attr.colorControlHighlight] on the clicked [ResourceNameListItem].
     *
     * @param id Id of the clicked [ResourceNameListItem.ResourceName].
     * @param screenWidthDp Screen width dp of the 'Fragment'.
     */
    override fun onResourceNameClick(@IntRange(from = 1) id: Int, screenWidthDp: Int) {
        if (getFragmentLayoutType(screenWidthDp) == FragmentResourceNamesLayoutType.MASTER_DETAIL) {
            setSelectableItemBackgroundOnAllResourceNames()
            resourceNames.filterIsInstance<ResourceNameListItem.ResourceName>()
                .filter { resourceName ->
                    resourceName.id == id
                }.map { resourceName ->
                    resourceName.backgroundAttrResId = android.R.attr.colorControlHighlight
                }
            resourceNamesLiveData.postValue(resourceNames)
        }
    }

    /**
     * Returns a [FragmentResourceNamesLayoutType] that corresponds with the given [screenWidthDp].
     * A [screenWidthDp] of `< 600dp` corresponds with a single-fragment layout. A [screenWidthDp]
     * of '>= 600dp' corresponds with a master-detail layout.
     */
    private fun getFragmentLayoutType(screenWidthDp: Int): FragmentResourceNamesLayoutType =
        if (screenWidthDp >= MASTER_DETAIL_LAYOUT_SCREEN_WIDTH_DP) {
            FragmentResourceNamesLayoutType.MASTER_DETAIL
        } else {
            FragmentResourceNamesLayoutType.SINGLE_FRAGMENT
        }

    /**
     * Sets [android.R.attr.selectableItemBackground] on every [ResourceNameListItem.ResourceName]
     * in [resourceNames].
     */
    private fun setSelectableItemBackgroundOnAllResourceNames() {
        resourceNames.filterIsInstance<ResourceNameListItem.ResourceName>()
            .filter { resourceName ->
                resourceName.backgroundAttrResId != android.R.attr.selectableItemBackground
            }.map { resourceName ->
                resourceName.backgroundAttrResId = android.R.attr.selectableItemBackground
            }
    }

    companion object {
        const val MASTER_DETAIL_LAYOUT_SCREEN_WIDTH_DP = 600
    }
}