package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.datasource.FilmsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.datasource.StarshipsRemoteDataSource
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
 * Exposes state and encapsulates business logic related to the starship details list.
 *
 * @property starshipsRemoteDataSource [StarshipsRemoteDataSource] implementation by `Retrofit` for
 * fetching starship data from SWAPI.
 * @property peopleRemoteDataSource [PeopleRemoteDataSource] implementation by `Retrofit` for
 * fetching people data from SWAPI.
 * @property filmsRemoteDataSource [FilmsRemoteDataSource] implementation by `Retrofit` for
 * fetching film data from SWAPI.
 */
class StarshipDetailsViewModelImpl @Inject constructor(
    private val starshipsRemoteDataSource: StarshipsRemoteDataSource,
    private val peopleRemoteDataSource: PeopleRemoteDataSource,
    private val filmsRemoteDataSource: FilmsRemoteDataSource,
) : ResourceDetailsViewModelImpl() {

    /**
     * Sets up a subscription for getting the details of a single starship from SWAPI to show in the
     * UI. Exposes the data via [resourceDetailsLiveData] when done.
     *
     * @param id Unique id of the starship to fetch.
     */
    override fun getResourceDetails(@IntRange(from = 1) id: Int) {
        disposable.add(starshipsRemoteDataSource.getStarship(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                showLoadingLiveData.postValue(true)
                showErrorLiveData.postValue(false)
            }
            .flatMap { starshipResponse ->
                getMoreDetailsSingle(starshipResponse)
            }
            .subscribe(
                { response ->
                    val starshipResponse = response.first
                    val pilotsResponse = response.second
                    val filmsResponse = response.third

                    val labelsToValues = mapOf(
                        R.string.name_detail_label to starshipResponse.name,
                        R.string.model_detail_label to starshipResponse.model,
                        R.string.starship_class_detail_label to starshipResponse.starshipClass,
                        R.string.manufacturer_detail_label to starshipResponse.manufacturer,
                        R.string.cost_detail_label to starshipResponse.costInCredits,
                        R.string.length_detail_label to starshipResponse.length,
                        R.string.hyperdrive_rating_detail_label to starshipResponse.hyperdriveRating,
                        R.string.mglt_detail_label to starshipResponse.mglt,
                        R.string.max_atmosphering_speed_detail_label to starshipResponse.maxAtmospheringSpeed,
                        R.string.crew_detail_label to starshipResponse.crew,
                        R.string.passengers_detail_label to starshipResponse.passengers,
                        R.string.cargo_capacity_detail_label to starshipResponse.cargoCapacity,
                        R.string.consumables_detail_label to starshipResponse.consumables,
                        R.string.pilots_detail_label to pilotsResponse.extractNames(),
                        R.string.films_detail_label to filmsResponse.extractNames()
                    )
                    val newStarshipDetails = labelsToValues.toResourceDetailListItems()

                    showLoadingLiveData.postValue(false)
                    resourceDetailsLiveData.postValue(newStarshipDetails)
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
     * starship corresponding with the given the already fetched [ResourceResponse.Starship]. The
     * original [ResourceResponse.Starship] is also emitted by the return [Single].
     *
     * @param starshipResponse [ResourceResponse.Starship] containing URLs specifying which network
     * calls to make.
     * @return Zipped [Single] for performing network calls for getting more details of a starship.
     */
    private fun getMoreDetailsSingle(starshipResponse: ResourceResponse.Starship): Single<Triple<ResourceResponse.Starship,
            List<ResourceResponse.Person>,
            List<ResourceResponse.Film>>
            > {

        val pilotsSingle = Observable.fromIterable(starshipResponse.pilotURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                peopleRemoteDataSource.getPerson(id)
            }
            .toList()

        val filmsSingle = Observable.fromIterable(starshipResponse.filmURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                filmsRemoteDataSource.getFilm(id)
            }
            .toList()

        val zipper =
            { pilotsResponse: List<ResourceResponse.Person>, filmsResponse: List<ResourceResponse.Film> ->
                Triple(starshipResponse, pilotsResponse, filmsResponse)
            }

        return Single.zip(pilotsSingle, filmsSingle, zipper)
    }

    companion object {
        private const val TAG = "StarshipDetailsViewModelImpl"
    }
}