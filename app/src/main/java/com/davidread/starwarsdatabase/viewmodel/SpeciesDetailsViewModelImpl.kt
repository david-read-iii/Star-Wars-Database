package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.datasource.FilmsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PlanetsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.SpeciesRemoteDataSource
import com.davidread.starwarsdatabase.model.Quad
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
 * Exposes state and encapsulates business logic related to the vehicle details list.
 *
 * @property speciesRemoteDataSource [SpeciesRemoteDataSource] implementation by `Retrofit` for
 * fetching species data from SWAPI.
 * @property planetsRemoteDataSource [PlanetsRemoteDataSource] implementation by `Retrofit` for
 * fetching planet data from SWAPI.
 * @property peopleRemoteDataSource [PeopleRemoteDataSource] implementation by `Retrofit` for
 * fetching people data from SWAPI.
 * @property filmsRemoteDataSource [FilmsRemoteDataSource] implementation by `Retrofit` for
 * fetching film data from SWAPI.
 */
class SpeciesDetailsViewModelImpl @Inject constructor(
    private val speciesRemoteDataSource: SpeciesRemoteDataSource,
    private val planetsRemoteDataSource: PlanetsRemoteDataSource,
    private val peopleRemoteDataSource: PeopleRemoteDataSource,
    private val filmsRemoteDataSource: FilmsRemoteDataSource
) : ResourceDetailsViewModelImpl() {

    /**
     * Sets up a subscription for getting the details of a single species from SWAPI to show in the
     * UI. Exposes the data via [resourceDetailsLiveData] when done.
     *
     * @param id Unique id of the species to fetch.
     */
    override fun getResourceDetails(@IntRange(from = 1) id: Int) {
        disposable.add(speciesRemoteDataSource.getSingleSpecies(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                showLoadingLiveData.postValue(true)
                showErrorLiveData.postValue(false)
            }
            .flatMap { speciesResponse ->
                getMoreDetailsSingle(speciesResponse)
            }
            .subscribe(
                { response ->
                    val speciesResponse = response.first
                    val homeworldResponse = response.second
                    val peopleResponse = response.third
                    val filmsResponse = response.fourth

                    val labelsToValues = mapOf(
                        R.string.name_detail_label to speciesResponse.name,
                        R.string.classification_detail_label to speciesResponse.classification,
                        R.string.designation_detail_label to speciesResponse.designation,
                        R.string.average_height_detail_label to speciesResponse.averageHeight,
                        R.string.skin_colors_detail_label to speciesResponse.skinColors,
                        R.string.hair_colors_detail_label to speciesResponse.hairColors,
                        R.string.eye_colors_detail_label to speciesResponse.eyeColors,
                        R.string.average_lifespan_detail_label to speciesResponse.averageLifespan,
                        R.string.homeworld_detail_label to homeworldResponse.name,
                        R.string.language_detail_label to speciesResponse.language,
                        R.string.people_detail_label to peopleResponse.extractNames(),
                        R.string.films_detail_label to filmsResponse.extractNames()
                    )
                    val newSpeciesDetails = labelsToValues.toResourceDetailListItems()

                    showLoadingLiveData.postValue(false)
                    resourceDetailsLiveData.postValue(newSpeciesDetails)
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
     * species corresponding with the given the already fetched [ResourceResponse.Species]. The
     * original [ResourceResponse.Species] is also emitted by the return [Single].
     *
     * @param speciesResponse [ResourceResponse.Species] containing URLs specifying which network
     * calls to make.
     * @return Zipped [Single] for performing network calls for getting more details of a species.
     */
    private fun getMoreDetailsSingle(speciesResponse: ResourceResponse.Species): Single<Quad<ResourceResponse.Species,
            ResourceResponse.Planet,
            List<ResourceResponse.Person>,
            List<ResourceResponse.Film>>
            > {

        val homeworldSingle =
            planetsRemoteDataSource.getPlanet(speciesResponse.homeworldURL.extractIDFromURL())

        val peopleSingle = Observable.fromIterable(speciesResponse.peopleURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                peopleRemoteDataSource.getPerson(id)
            }
            .toList()

        val filmsSingle = Observable.fromIterable(speciesResponse.filmURLs.extractIDsFromURLs())
            .flatMapSingle { id ->
                filmsRemoteDataSource.getFilm(id)
            }
            .toList()

        val zipper =
            { homeworldResponse: ResourceResponse.Planet, peopleResponse: List<ResourceResponse.Person>, filmsResponse: List<ResourceResponse.Film> ->
                Quad(speciesResponse, homeworldResponse, peopleResponse, filmsResponse)
            }

        return Single.zip(homeworldSingle, peopleSingle, filmsSingle, zipper)
    }

    companion object {
        private const val TAG = "SpeciesDetailsViewModelImpl"
    }
}