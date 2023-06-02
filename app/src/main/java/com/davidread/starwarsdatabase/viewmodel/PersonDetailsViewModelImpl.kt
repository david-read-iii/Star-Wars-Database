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
import com.davidread.starwarsdatabase.util.extractIDFromURL
import com.davidread.starwarsdatabase.util.extractIDsFromURLs
import com.davidread.starwarsdatabase.util.extractNames
import com.davidread.starwarsdatabase.util.toResourceDetailListItems
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Exposes state and encapsulates business logic related to the person details list.
 *
 * @property peopleRemoteDataSource [PeopleRemoteDataSource] implementation by `Retrofit` for
 * fetching people data from SWAPI.
 * @property planetsRemoteDataSource [PlanetsRemoteDataSource] implementation by `Retrofit` for
 * fetching planet data from SWAPI.
 * @property speciesRemoteDataSource [SpeciesRemoteDataSource] implementation by `Retrofit` for
 * fetching species data from SWAPI.
 * @property filmsRemoteDataSource [FilmsRemoteDataSource] implementation by `Retrofit` for
 * fetching film data from SWAPI.
 * @property starshipsRemoteDataSource [StarshipsRemoteDataSource] implementation by `Retrofit` for
 * fetching starship data from SWAPI.
 * @property vehiclesRemoteDataSource [VehiclesRemoteDataSource] implementation by `Retrofit` for
 * fetching vehicle data from SWAPI.
 */
class PersonDetailsViewModelImpl @Inject constructor(
    private val peopleRemoteDataSource: PeopleRemoteDataSource,
    private val planetsRemoteDataSource: PlanetsRemoteDataSource,
    private val speciesRemoteDataSource: SpeciesRemoteDataSource,
    private val filmsRemoteDataSource: FilmsRemoteDataSource,
    private val starshipsRemoteDataSource: StarshipsRemoteDataSource,
    private val vehiclesRemoteDataSource: VehiclesRemoteDataSource
) : ResourceDetailsViewModelImpl() {

    /**
     * Sets up a subscription for getting the details of a single person from SWAPI to show in the
     * UI. Exposes the data via [resourceDetailsLiveData] when done.
     *
     * @param id Unique id of the person to fetch.
     */
    override fun getResourceDetails(@IntRange(from = 1) id: Int) {
        disposable.add(peopleRemoteDataSource.getPerson(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                showLoadingLiveData.postValue(true)
                showErrorLiveData.postValue(false)
            }
            .flatMap { personResponse ->
                getMoreDetailsSingle(personResponse)
            }
            .subscribe(
                { response ->
                    val personResponse = response.first
                    val homeworldResponse = response.second
                    val speciesResponse = response.third
                    val filmsResponse = response.fourth
                    val starshipsResponse = response.fifth
                    val vehiclesResponse = response.sixth

                    val labelsToValues = mapOf(
                        R.string.name_detail_label to personResponse.name,
                        R.string.homeworld_detail_label to homeworldResponse.name,
                        R.string.birth_year_detail_label to personResponse.birthYear,
                        R.string.species_detail_label to speciesResponse.extractNames(),
                        R.string.gender_detail_label to personResponse.gender,
                        R.string.height_detail_label to personResponse.height,
                        R.string.mass_detail_label to personResponse.mass,
                        R.string.hair_color_detail_label to personResponse.hairColor,
                        R.string.eye_color_detail_label to personResponse.eyeColor,
                        R.string.skin_color_detail_label to personResponse.skinColor,
                        R.string.films_detail_label to filmsResponse.extractNames(),
                        R.string.starships_detail_label to starshipsResponse.extractNames(),
                        R.string.vehicles_detail_label to vehiclesResponse.extractNames()
                    )
                    val newPersonDetails = labelsToValues.toResourceDetailListItems()

                    showLoadingLiveData.postValue(false)
                    resourceDetailsLiveData.postValue(newPersonDetails)
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
     * person corresponding with the given the already fetched [ResourceResponse.Person]. The
     * original [ResourceResponse.Person] is also emitted by the return [Single].
     *
     * @param personResponse [ResourceResponse.Person] containing URLs specifying which network
     * calls to make.
     * @return Zipped [Single] for performing network calls for getting more details of a person.
     */
    private fun getMoreDetailsSingle(personResponse: ResourceResponse.Person): Single<Sextuple<ResourceResponse.Person,
            ResourceResponse.Planet,
            List<ResourceResponse.Species>,
            List<ResourceResponse.Film>,
            List<ResourceResponse.Starship>,
            List<ResourceResponse.Vehicle>>
            > {

        val homeworldSingle =
            planetsRemoteDataSource.getPlanet(personResponse.homeworldURL.extractIDFromURL())

        val speciesSingle = Observable.fromIterable(personResponse.speciesURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                speciesRemoteDataSource.getSingleSpecies(id)
            }
            .toList()

        val filmsSingle = Observable.fromIterable(personResponse.filmsURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                filmsRemoteDataSource.getFilm(id)
            }
            .toList()

        val starshipsSingle: Single<List<ResourceResponse.Starship>> =
            Observable.fromIterable(personResponse.starshipsURLs.extractIDsFromURLs())
                .flatMapSingle { id ->
                    starshipsRemoteDataSource.getStarship(id)
                }
                .toList()

        val vehiclesSingle =
            Observable.fromIterable(personResponse.vehiclesURLs.extractIDsFromURLs())
                .flatMapSingle { id ->
                    vehiclesRemoteDataSource.getVehicle(id)
                }
                .toList()

        val zipper =
            { homeworldResponse: ResourceResponse.Planet,
              speciesResponse: List<ResourceResponse.Species>,
              filmsResponse: List<ResourceResponse.Film>,
              starshipsResponse: List<ResourceResponse.Starship>,
              vehiclesResponse: List<ResourceResponse.Vehicle> ->
                Sextuple(
                    personResponse,
                    homeworldResponse,
                    speciesResponse,
                    filmsResponse,
                    starshipsResponse,
                    vehiclesResponse
                )
            }

        return Single.zip(
            homeworldSingle,
            speciesSingle,
            filmsSingle,
            starshipsSingle,
            vehiclesSingle,
            zipper
        )
    }

    companion object {
        private const val TAG = "PersonDetailsViewModelImpl"
    }
}