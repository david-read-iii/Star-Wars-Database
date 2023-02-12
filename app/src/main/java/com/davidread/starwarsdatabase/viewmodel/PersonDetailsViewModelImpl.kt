package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.annotation.IntRange
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.datasource.*
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import com.davidread.starwarsdatabase.model.view.ResourceDetailListItem
import com.davidread.starwarsdatabase.model.Sextuple
import com.davidread.starwarsdatabase.util.extractIDFromURL
import com.davidread.starwarsdatabase.util.extractIDsFromURLs
import com.davidread.starwarsdatabase.util.extractNames
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
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
 * fetching planet data from SWAPI.
 * @property filmsRemoteDataSource [FilmsRemoteDataSource] implementation by `Retrofit` for
 * fetching planet data from SWAPI.
 * @property starshipsRemoteDataSource [StarshipsRemoteDataSource] implementation by `Retrofit` for
 * fetching planet data from SWAPI.
 * @property vehiclesRemoteDataSource [VehiclesRemoteDataSource] implementation by `Retrofit` for
 * fetching planet data from SWAPI.
 */
class PersonDetailsViewModelImpl @Inject constructor(
    private val peopleRemoteDataSource: PeopleRemoteDataSource,
    private val planetsRemoteDataSource: PlanetsRemoteDataSource,
    private val speciesRemoteDataSource: SpeciesRemoteDataSource,
    private val filmsRemoteDataSource: FilmsRemoteDataSource,
    private val starshipsRemoteDataSource: StarshipsRemoteDataSource,
    private val vehiclesRemoteDataSource: VehiclesRemoteDataSource
) : ResourceDetailsViewModel, ViewModel() {

    /**
     * Emits a [List] of [ResourceDetailListItem]s that should be shown on the UI.
     */
    override val resourceDetailsLiveData: MutableLiveData<List<ResourceDetailListItem>> =
        MutableLiveData()

    /**
     * Emits whether a loading state should be shown on the UI.
     */
    override val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Emits whether an error state should be shown on the UI.
     */
    override val showErrorLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Container for managing resources used by `Disposable`s or their subclasses.
     */
    private val disposable: CompositeDisposable = CompositeDisposable()

    /**
     * Called when this `ViewModel` is no longer used and will be destroyed. Clears any
     * subscriptions held by [disposable] to prevent this `ViewModel` from leaking.
     */
    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

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

                    val newPersonDetails = listOf(
                        ResourceDetailListItem(R.string.name_detail_label, personResponse.name),
                        ResourceDetailListItem(
                            R.string.homeworld_detail_label,
                            homeworldResponse.name
                        ),
                        ResourceDetailListItem(
                            R.string.birth_year_detail_label,
                            personResponse.birthYear
                        ),
                        ResourceDetailListItem(
                            R.string.species_detail_label,
                            speciesResponse.extractNames()
                        ),
                        ResourceDetailListItem(
                            R.string.gender_detail_label,
                            personResponse.gender
                        ),
                        ResourceDetailListItem(
                            R.string.height_detail_label,
                            personResponse.height
                        ),
                        ResourceDetailListItem(R.string.mass_detail_label, personResponse.mass),
                        ResourceDetailListItem(
                            R.string.hair_color_detail_label,
                            personResponse.hairColor
                        ),
                        ResourceDetailListItem(
                            R.string.eye_color_detail_label,
                            personResponse.eyeColor
                        ),
                        ResourceDetailListItem(
                            R.string.skin_color_detail_label,
                            personResponse.skinColor
                        ),
                        ResourceDetailListItem(
                            R.string.films_detail_label,
                            filmsResponse.extractNames()
                        ),
                        ResourceDetailListItem(
                            R.string.starships_detail_label,
                            starshipsResponse.extractNames()
                        ),
                        ResourceDetailListItem(
                            R.string.vehicles_detail_label,
                            vehiclesResponse.extractNames()
                        )
                    )
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