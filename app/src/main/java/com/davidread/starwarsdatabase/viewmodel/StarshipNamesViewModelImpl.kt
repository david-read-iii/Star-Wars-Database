package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import android.view.View
import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.datasource.StarshipsRemoteDataSource
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import com.davidread.starwarsdatabase.util.extractIDFromURL
import com.davidread.starwarsdatabase.util.extractPageFromURL
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Exposes state and encapsulates business logic related to the starship names list.
 *
 * @property starshipsRemoteDataSource [StarshipsRemoteDataSource] implementation by `Retrofit` for
 * fetching starship data from SWAPI.
 */
class StarshipNamesViewModelImpl @Inject constructor(private val starshipsRemoteDataSource: StarshipsRemoteDataSource) :
    ResourceNamesViewModelImpl() {

    /**
     * Called when this `ViewModel` is initially created. It sets up the initial subscription for
     * getting page 1 of starship names to show in the UI.
     */
    init {
        getResourceNames(nextPage)
    }

    /**
     * Sets up a subscription for getting a page of starship names (10 in each page) from SWAPI to
     * show in the UI. Exposes the data via [resourceNamesLiveData] when done.
     *
     * @param page Which page of starship names to fetch.
     */
    override fun getResourceNames(@IntRange(from = 1) page: Int) {
        disposable.add(starshipsRemoteDataSource.getStarships(page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                resourceNames.apply {
                    remove(ResourceNameListItem.Error)
                    add(ResourceNameListItem.Loading)
                }
                resourceNamesLiveData.postValue(resourceNames)
            }
            .subscribe(
                { pageResponse ->
                    val newStarshipNames = pageResponse.results.map { starshipResponse ->
                        ResourceNameListItem.ResourceName(
                            id = starshipResponse.url.extractIDFromURL(),
                            name = starshipResponse.name,
                            backgroundAttrResId = android.R.attr.selectableItemBackground
                        )
                    }
                    resourceNames.apply {
                        remove(ResourceNameListItem.Loading)
                        addAll(newStarshipNames)
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
                    } ?: run {
                        isAllResourceNamesRequestedLiveData.postValue(true)
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
        private const val TAG = "StarshipNamesViewModelImpl"
    }
}