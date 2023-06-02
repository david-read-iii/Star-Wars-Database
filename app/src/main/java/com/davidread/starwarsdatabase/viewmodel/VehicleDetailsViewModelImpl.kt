package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.datasource.FilmsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.datasource.VehiclesRemoteDataSource
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import com.davidread.starwarsdatabase.util.extractIDsFromURLs
import com.davidread.starwarsdatabase.util.extractNames
import com.davidread.starwarsdatabase.util.toResourceDetailListItems
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Exposes state and encapsulates business logic related to the vehicle details list.
 *
 * @property vehiclesRemoteDataSource [VehiclesRemoteDataSource] implementation by `Retrofit` for
 * fetching vehicle data from SWAPI.
 * @property peopleRemoteDataSource [PeopleRemoteDataSource] implementation by `Retrofit` for
 * fetching people data from SWAPI.
 * @property filmsRemoteDataSource [FilmsRemoteDataSource] implementation by `Retrofit` for
 * fetching film data from SWAPI.
 */
class VehicleDetailsViewModelImpl @Inject constructor(
    private val vehiclesRemoteDataSource: VehiclesRemoteDataSource,
    private val peopleRemoteDataSource: PeopleRemoteDataSource,
    private val filmsRemoteDataSource: FilmsRemoteDataSource
) : ResourceDetailsViewModelImpl() {

    /**
     * Sets up a subscription for getting the details of a single vehicle from SWAPI to show in the
     * UI. Exposes the data via [resourceDetailsLiveData] when done.
     *
     * @param id Unique id of the vehicle to fetch.
     */
    override fun getResourceDetails(@IntRange(from = 1) id: Int) {
        disposable.add(vehiclesRemoteDataSource.getVehicle(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                showLoadingLiveData.postValue(true)
                showErrorLiveData.postValue(false)
            }
            .flatMap { vehicleResponse ->
                getMoreDetailsSingle(vehicleResponse)
            }
            .subscribe(
                { response ->
                    val vehiclesResponse = response.first
                    val pilotsResponse = response.second
                    val filmsResponse = response.third

                    val labelsToValues = mapOf(
                        R.string.name_detail_label to vehiclesResponse.name,
                        R.string.model_detail_label to vehiclesResponse.model,
                        R.string.manufacturer_detail_label to vehiclesResponse.manufacturer,
                        R.string.cost_detail_label to vehiclesResponse.costInCredits,
                        R.string.length_detail_label to vehiclesResponse.length,
                        R.string.max_atmosphering_speed_detail_label to vehiclesResponse.maxAtmospheringSpeed,
                        R.string.crew_detail_label to vehiclesResponse.crew,
                        R.string.passengers_detail_label to vehiclesResponse.passengers,
                        R.string.cargo_capacity_detail_label to vehiclesResponse.cargoCapacity,
                        R.string.consumables_detail_label to vehiclesResponse.consumables,
                        R.string.vehicle_class_detail_label to vehiclesResponse.vehicleClass,
                        R.string.pilots_detail_label to pilotsResponse.extractNames(),
                        R.string.films_detail_label to filmsResponse.extractNames()
                    )
                    val newVehicleDetails = labelsToValues.toResourceDetailListItems()

                    showLoadingLiveData.postValue(false)
                    resourceDetailsLiveData.postValue(newVehicleDetails)
                },
                { throwable ->
                    showLoadingLiveData.postValue(false)
                    showErrorLiveData.postValue(true)
                    Log.e(TAG, throwable.toString())
                }
            )
        )
    }

    /**
     * Returns a zipped [Single] for performing network calls for getting more details of the
     * vehicle corresponding with the given the already fetched [ResourceResponse.Vehicle]. The
     * original [ResourceResponse.Vehicle] is also emitted by the return [Single].
     *
     * @param vehicleResponse [ResourceResponse.Vehicle] containing URLs specifying which network
     * calls to make.
     * @return Zipped [Single] for performing network calls for getting more details of a vehicle.
     */
    private fun getMoreDetailsSingle(vehicleResponse: ResourceResponse.Vehicle): Single<Triple<ResourceResponse.Vehicle,
            List<ResourceResponse.Person>,
            List<ResourceResponse.Film>>
            > {

        val pilotsSingle = Observable.fromIterable(vehicleResponse.pilotURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                peopleRemoteDataSource.getPerson(id)
            }
            .toList()

        val filmsSingle = Observable.fromIterable(vehicleResponse.filmURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                filmsRemoteDataSource.getFilm(id)
            }
            .toList()

        val zipper =
            { pilotsResponse: List<ResourceResponse.Person>, filmsResponse: List<ResourceResponse.Film> ->
                Triple(vehicleResponse, pilotsResponse, filmsResponse)
            }

        return Single.zip(pilotsSingle, filmsSingle, zipper)
    }

    companion object {
        private const val TAG = "VehicleDetailsViewModelImpl"
    }
}