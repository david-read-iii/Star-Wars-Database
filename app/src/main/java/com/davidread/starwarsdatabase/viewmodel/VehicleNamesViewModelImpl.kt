package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.datasource.VehiclesRemoteDataSource
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import com.davidread.starwarsdatabase.util.extractIDFromURL
import com.davidread.starwarsdatabase.util.extractPageFromURL
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Exposes state and encapsulates business logic related to the vehicle names list.
 *
 * @property vehiclesRemoteDataSource [VehiclesRemoteDataSource] implementation by `Retrofit` for
 * fetching vehicle data from SWAPI.
 */
class VehicleNamesViewModelImpl @Inject constructor(private val vehiclesRemoteDataSource: VehiclesRemoteDataSource) :
    ResourceNamesViewModelImpl() {

    /**
     * Called when this `ViewModel` is initially created. It sets up the initial subscription for
     * getting page 1 of planet names to show in the UI.
     */
    init {
        getResourceNames(nextPage)
    }

    /**
     * Sets up a subscription for getting a page of vehicle names (10 in each page) from SWAPI to
     * show in the UI. Exposes the data via [resourceNamesLiveData] when done.
     *
     * @param page Which page of vehicle names to fetch.
     */
    override fun getResourceNames(@IntRange(from = 1) page: Int) {
        disposable.add(vehiclesRemoteDataSource.getVehicles(page)
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
                    val newVehicleNames = pageResponse.results.map { vehicleResponse ->
                        ResourceNameListItem.ResourceName(
                            id = vehicleResponse.url.extractIDFromURL(),
                            name = vehicleResponse.name
                        )
                    }
                    resourceNames.apply {
                        remove(ResourceNameListItem.Loading)
                        addAll(newVehicleNames)
                    }
                    resourceNamesLiveData.postValue(resourceNames)
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
        private const val TAG = "VehicleNamesViewModelImpl"
    }
}