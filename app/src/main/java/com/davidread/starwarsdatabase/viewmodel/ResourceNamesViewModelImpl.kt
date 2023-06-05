package com.davidread.starwarsdatabase.viewmodel

import android.view.View
import androidx.annotation.IntRange
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidread.starwarsdatabase.model.FragmentResourceNamesLayoutType
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import com.davidread.starwarsdatabase.util.MutableSingleEventLiveData
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
     * Emits the view visibility of `subNavHostFragment`.
     */
    override val subNavHostFragmentVisibilityLiveData: MutableLiveData<Int> =
        MutableLiveData(View.GONE)

    /**
     * Emits event when the end of the list should be smooth scrolled to.
     */
    override val smoothScrollToPositionInListLiveData: MutableSingleEventLiveData<Int> =
        MutableSingleEventLiveData()

    /**
     * Emits event when the the details fragment should be navigated to. Passes the `id` of the
     * resource to show.
     */
    override val onNavigateToDetailsFragmentLiveData: MutableSingleEventLiveData<Int> =
        MutableSingleEventLiveData()

    /**
     * Emits event when the details fragment should be shown in `subNavHostFragment`. Passes the
     * `id` of the resource to show.
     */
    override val onShowDetailsFragmentInSubNavHostFragmentLiveData: MutableSingleEventLiveData<Int> =
        MutableSingleEventLiveData()

    /**
     * Next page of resource names to fetch from SWAPI. Holds `null` when no more resource names
     * can be fetched.
     */
    @IntRange(from = 1)
    protected var nextPage: Int? = 1

    /**
     * [MutableList] of [ResourceNameListItem]s to be stored/modified here. Emitted via its
     * `LiveData` each time it is updated.
     */
    protected val resourceNames: MutableList<ResourceNameListItem> = mutableListOf()

    /**
     * Container for managing resources used by `Disposable`s or their subclasses.
     */
    protected val disposable: CompositeDisposable = CompositeDisposable()

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
     * Called when the `Fragment` resource name list is scrolled on. If the last list item is
     * visible, the last list item represents a [ResourceNameListItem.ResourceName], and the data
     * source has another page to get, then loading will be shown on the UI and a subscription will
     * be started to get another page from the data source.
     *
     * @param isLastItemVisible Whether the last view visible in the `RecyclerView` is visible.
     */
    override fun onResourceNamesListScroll(isLastItemVisible: Boolean) {
        val isLastItemResourceName = resourceNames.last() is ResourceNameListItem.ResourceName
        val doesDataSourceHaveNextPage = nextPage != null
        if (isLastItemVisible && isLastItemResourceName && doesDataSourceHaveNextPage) {
            resourceNames.add(ResourceNameListItem.Loading)
            resourceNamesLiveData.postValue(resourceNames)
            smoothScrollToPositionInListLiveData.postValue(resourceNames.lastIndex)
            nextPage?.let { getResourceNames(it) }
        }
    }

    /**
     * Called when a resource name is clicked. If a single fragment layout is being used, then it
     * just emits an event to show the details fragment in the UI. If a master-detail fragment
     * layout is being used, it emits an event to show the details fragment in `subNavHostFragment`.
     * It also will emit a new resource names list where the selected resource name is highlighted.
     *
     * @param id Id of the clicked [ResourceNameListItem.ResourceName].
     * @param screenWidthDp Screen width dp of the 'Fragment'.
     */
    override fun onResourceNameClick(@IntRange(from = 1) id: Int, screenWidthDp: Int) {
        when (getFragmentLayoutType(screenWidthDp)) {
            FragmentResourceNamesLayoutType.SINGLE_FRAGMENT -> {
                onNavigateToDetailsFragmentLiveData.postValue(id)
            }

            FragmentResourceNamesLayoutType.MASTER_DETAIL -> {
                setSelectableItemBackgroundOnAllResourceNames()
                resourceNames.filterIsInstance<ResourceNameListItem.ResourceName>()
                    .filter { resourceName ->
                        resourceName.id == id
                    }.map { resourceName ->
                        resourceName.backgroundAttrResId = android.R.attr.colorControlHighlight
                    }
                resourceNamesLiveData.postValue(resourceNames)
                onShowDetailsFragmentInSubNavHostFragmentLiveData.postValue(id)
            }
        }
    }

    /**
     * Called when the retry button of an error item is clicked in the list. It replaces the error
     * UI with a loading UI and starts a subscription for getting another page from the data source.
     */
    override fun onErrorRetryClick() {
        resourceNames.apply {
            remove(ResourceNameListItem.Error)
            add(ResourceNameListItem.Loading)
        }
        resourceNamesLiveData.postValue(resourceNames)
        nextPage?.let { getResourceNames(it) }
    }

    /**
     * Invoked when a subscription to get another page of resource names should be made. Should be
     * defined by the subclass as each subclass gets different types of resource name.
     */
    protected abstract fun getResourceNames(@IntRange(from = 1) page: Int)

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