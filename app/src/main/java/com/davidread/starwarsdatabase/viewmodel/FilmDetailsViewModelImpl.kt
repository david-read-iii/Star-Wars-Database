package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.datasource.FilmsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PlanetsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.SpeciesRemoteDataSource
import com.davidread.starwarsdatabase.datasource.StarshipsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.VehiclesRemoteDataSource
import com.davidread.starwarsdatabase.model.Sextuple
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
 * Exposes state and encapsulates business logic related to the film details list.
 *
 * @property filmsRemoteDataSource [FilmsRemoteDataSource] implementation by `Retrofit` for
 * fetching film data from SWAPI.
 * @property peopleRemoteDataSource [PeopleRemoteDataSource] implementation by `Retrofit` for
 * fetching people data from SWAPI.
 * @property planetsRemoteDataSource [PlanetsRemoteDataSource] implementation by `Retrofit` for
 * fetching planet data from SWAPI.
 * @property speciesRemoteDataSource [SpeciesRemoteDataSource] implementation by `Retrofit` for
 * fetching species data from SWAPI.
 * @property starshipsRemoteDataSource [StarshipsRemoteDataSource] implementation by `Retrofit` for
 * fetching starship data from SWAPI.
 * @property vehiclesRemoteDataSource [VehiclesRemoteDataSource] implementation by `Retrofit` for
 * fetching vehicle data from SWAPI.
 */
class FilmDetailsViewModelImpl @Inject constructor(
    private val filmsRemoteDataSource: FilmsRemoteDataSource,
    private val peopleRemoteDataSource: PeopleRemoteDataSource,
    private val planetsRemoteDataSource: PlanetsRemoteDataSource,
    private val speciesRemoteDataSource: SpeciesRemoteDataSource,
    private val starshipsRemoteDataSource: StarshipsRemoteDataSource,
    private val vehiclesRemoteDataSource: VehiclesRemoteDataSource
) : ResourceDetailsViewModelImpl() {

    /**
     * Sets up a subscription for getting the details of a single film from SWAPI to show in the UI.
     * Exposes the data via [resourceDetailsLiveData] when done.
     *
     * @param id Unique id of the film to fetch.
     */
    override fun getResourceDetails(@IntRange(from = 1) id: Int) {
        disposable.add(filmsRemoteDataSource.getFilm(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                showLoadingLiveData.postValue(true)
                showErrorLiveData.postValue(false)
            }
            .flatMap { filmResponse ->
                getMoreDetailsSingle(filmResponse)
            }
            .subscribe(
                { response ->
                    val filmResponse = response.first
                    val charactersResponse = response.second
                    val planetsResponse = response.third
                    val speciesResponse = response.fourth
                    val starshipsResponse = response.fifth
                    val vehicleResponse = response.sixth

                    val labelsToValues = mapOf(
                        R.string.title_detail_label to filmResponse.title,
                        R.string.episode_detail_label to filmResponse.episodeID,
                        R.string.release_date_detail_label to filmResponse.releaseDate,
                        R.string.director_detail_label to filmResponse.director,
                        R.string.producer_detail_label to filmResponse.producer,
                        R.string.opening_crawl_detail_label to filmResponse.openingCrawl,
                        R.string.characters_detail_label to charactersResponse.extractNames(),
                        R.string.planets_detail_label to planetsResponse.extractNames(),
                        R.string.species_detail_label to speciesResponse.extractNames(),
                        R.string.starships_detail_label to starshipsResponse.extractNames(),
                        R.string.vehicles_detail_label to vehicleResponse.extractNames()
                    )
                    val newFilmDetails = labelsToValues.toResourceDetailListItems()

                    showLoadingLiveData.postValue(false)
                    resourceDetailsLiveData.postValue(newFilmDetails)
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
     * Returns a zipped [Single] for performing network calls for getting more details of the film
     * corresponding with the given the already fetched [ResourceResponse.Film]. The original
     * [ResourceResponse.Film] is also emitted by the return [Single].
     *
     * @param filmResponse [ResourceResponse.Film] containing URLs specifying which network calls to
     * make.
     * @return Zipped [Single] for performing network calls for getting more details of a film.
     */
    private fun getMoreDetailsSingle(filmResponse: ResourceResponse.Film): Single<Sextuple<ResourceResponse.Film,
            List<ResourceResponse.Person>,
            List<ResourceResponse.Planet>,
            List<ResourceResponse.Species>,
            List<ResourceResponse.Starship>,
            List<ResourceResponse.Vehicle>>
            > {

        val charactersSingle =
            Observable.fromIterable(filmResponse.characterURLs.extractIDsFromURLs())
                .flatMapSingle { id ->
                    peopleRemoteDataSource.getPerson(id)
                }
                .toList()

        val planetsSingle = Observable.fromIterable(filmResponse.planetURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                planetsRemoteDataSource.getPlanet(id)
            }
            .toList()

        val speciesSingle = Observable.fromIterable(filmResponse.speciesURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                speciesRemoteDataSource.getSingleSpecies(id)
            }
            .toList()

        val starshipsSingle =
            Observable.fromIterable(filmResponse.starshipURLs.extractIDsFromURLs())
                .flatMapSingle { id ->
                    starshipsRemoteDataSource.getStarship(id)
                }
                .toList()

        val vehiclesSingle = Observable.fromIterable(filmResponse.vehicleURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                vehiclesRemoteDataSource.getVehicle(id)
            }
            .toList()

        val zipper =
            { charactersResponse: List<ResourceResponse.Person>,
              planetsResponse: List<ResourceResponse.Planet>,
              speciesResponse: List<ResourceResponse.Species>,
              starshipsResponse: List<ResourceResponse.Starship>,
              vehiclesResponse: List<ResourceResponse.Vehicle> ->
                Sextuple(
                    filmResponse,
                    charactersResponse,
                    planetsResponse,
                    speciesResponse,
                    starshipsResponse,
                    vehiclesResponse
                )
            }

        return Single.zip(
            charactersSingle,
            planetsSingle,
            speciesSingle,
            starshipsSingle,
            vehiclesSingle,
            zipper
        )
    }

    companion object {
        private const val TAG = "FilmDetailsViewModelImpl"
    }
}