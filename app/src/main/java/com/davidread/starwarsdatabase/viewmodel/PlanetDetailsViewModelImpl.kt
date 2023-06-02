package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.datasource.FilmsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PlanetsRemoteDataSource
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
 * @property planetsRemoteDataSource [PlanetsRemoteDataSource] implementation by `Retrofit` for
 * fetching planet data from SWAPI.
 * @property peopleRemoteDataSource [PeopleRemoteDataSource] implementation by `Retrofit` for
 * fetching people data from SWAPI.
 * @property filmsRemoteDataSource [FilmsRemoteDataSource] implementation by `Retrofit` for
 * fetching film data from SWAPI.
 */
class PlanetDetailsViewModelImpl @Inject constructor(
    private val planetsRemoteDataSource: PlanetsRemoteDataSource,
    private val peopleRemoteDataSource: PeopleRemoteDataSource,
    private val filmsRemoteDataSource: FilmsRemoteDataSource
) : ResourceDetailsViewModelImpl() {

    /**
     * Sets up a subscription for getting the details of a single planet from SWAPI to show in the
     * UI. Exposes the data via [resourceDetailsLiveData] when done.
     *
     * @param id Unique id of the planet to fetch.
     */
    override fun getResourceDetails(@IntRange(from = 1) id: Int) {
        disposable.add(planetsRemoteDataSource.getPlanet(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                showLoadingLiveData.postValue(true)
                showErrorLiveData.postValue(false)
            }
            .flatMap { planetResponse ->
                getMoreDetailsSingle(planetResponse)
            }
            .subscribe(
                { response ->
                    val planetResponse = response.first
                    val peopleResponse = response.second
                    val filmsResponse = response.third

                    val labelsToValues = mapOf(
                        R.string.name_detail_label to planetResponse.name,
                        R.string.rotation_period_detail_label to planetResponse.rotationPeriod,
                        R.string.orbital_period_detail_label to planetResponse.orbitalPeriod,
                        R.string.diameter_detail_label to planetResponse.diameter,
                        R.string.climate_detail_label to planetResponse.climate,
                        R.string.gravity_detail_label to planetResponse.gravity,
                        R.string.terrain_detail_label to planetResponse.terrain,
                        R.string.surface_water_detail_label to planetResponse.surfaceWater,
                        R.string.population_detail_label to planetResponse.population,
                        R.string.residents_detail_label to peopleResponse.extractNames(),
                        R.string.films_detail_label to filmsResponse.extractNames()
                    )
                    val newPlanetDetails = labelsToValues.toResourceDetailListItems()

                    showLoadingLiveData.postValue(false)
                    resourceDetailsLiveData.postValue(newPlanetDetails)
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
     * planet corresponding with the given the already fetched [ResourceResponse.Planet]. The
     * original [ResourceResponse.Planet] is also emitted by the return [Single].
     *
     * @param planetResponse [ResourceResponse.Planet] containing URLs specifying which network
     * calls to make.
     * @return Zipped [Single] for performing network calls for getting more details of a planet.
     */
    private fun getMoreDetailsSingle(planetResponse: ResourceResponse.Planet): Single<Triple<ResourceResponse.Planet,
            List<ResourceResponse.Person>,
            List<ResourceResponse.Film>>
            > {

        val residentsSingle =
            Observable.fromIterable(planetResponse.residentURLs.extractIDsFromURLs())
                .flatMapSingle { id ->
                    peopleRemoteDataSource.getPerson(id)
                }
                .toList()

        val filmsSingle = Observable.fromIterable(planetResponse.filmURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                filmsRemoteDataSource.getFilm(id)
            }
            .toList()

        val zipper =
            { residentsResponse: List<ResourceResponse.Person>, filmsResponse: List<ResourceResponse.Film> ->
                Triple(planetResponse, residentsResponse, filmsResponse)
            }

        return Single.zip(residentsSingle, filmsSingle, zipper)
    }

    companion object {
        private const val TAG = "PlanetDetailsViewModelImpl"
    }
}