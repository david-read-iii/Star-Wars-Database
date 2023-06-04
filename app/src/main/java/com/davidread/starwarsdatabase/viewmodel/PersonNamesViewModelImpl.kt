package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import android.view.View
import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import com.davidread.starwarsdatabase.util.extractIDFromURL
import com.davidread.starwarsdatabase.util.extractPageFromURL
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Exposes state and encapsulates business logic related to the person names list.
 *
 * @property peopleRemoteDataSource [PeopleRemoteDataSource] implementation by `Retrofit` for
 * fetching people data from SWAPI.
 */
class PersonNamesViewModelImpl @Inject constructor(private val peopleRemoteDataSource: PeopleRemoteDataSource) :
    ResourceNamesViewModelImpl() {

    /**
     * Called when this `ViewModel` is initially created. It sets up the initial subscription for
     * getting page 1 of person names to show in the UI.
     */
    init {
        getResourceNames(nextPage)
    }

    /**
     * Sets up a subscription for getting a page of person names (10 in each page) from SWAPI to
     * show in the UI. Exposes the data via [resourceNamesLiveData] when done.
     *
     * @param page Which page of person names to fetch.
     */
    override fun getResourceNames(@IntRange(from = 1) page: Int) {
        disposable.add(peopleRemoteDataSource.getPeople(page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                resourceNames.apply {
                    remove(ResourceNameListItem.Error)
                    add(ResourceNameListItem.Loading)
                }
                resourceNamesLiveData.postValue(resourceNames)
                isLoadMoreResourceNamesOnScrollListenerEnabledLiveData.postValue(false)
                smoothScrollToPositionInListLiveData.postValue(resourceNames.lastIndex)
            }
            .subscribe(
                { pageResponse ->
                    val newPersonNames = pageResponse.results.map { personResponse ->
                        ResourceNameListItem.ResourceName(
                            id = personResponse.url.extractIDFromURL(),
                            name = personResponse.name,
                            backgroundAttrResId = android.R.attr.selectableItemBackground
                        )
                    }
                    resourceNames.apply {
                        remove(ResourceNameListItem.Loading)
                        addAll(newPersonNames)
                    }
                    resourceNamesLiveData.postValue(resourceNames)
                    subNavHostFragmentVisibility.postValue(View.VISIBLE)
                    pageResponse.next?.let { next ->
                        nextPage = try {
                            next.extractPageFromURL()
                        } catch (e: IllegalArgumentException) {
                            Log.e(TAG, e.toString())
                            nextPage
                        }
                        isLoadMoreResourceNamesOnScrollListenerEnabledLiveData.postValue(true)
                    }
                },
                { throwable ->
                    resourceNames.apply {
                        remove(ResourceNameListItem.Loading)
                        add(ResourceNameListItem.Error)
                    }
                    resourceNamesLiveData.postValue(resourceNames)
                    Log.e(TAG, throwable.toString())
                }
            )
        )
    }

    companion object {
        private const val TAG = "PersonNamesViewModelImpl"
    }
}