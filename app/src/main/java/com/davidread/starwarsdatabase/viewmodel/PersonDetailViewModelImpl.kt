package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.annotation.IntRange
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.model.view.DetailListItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Exposes state and encapsulates business logic related to the person detail list.
 *
 * @property peopleRemoteDataSource [PeopleRemoteDataSource] implementation by `Retrofit` for
 * fetching people data from SWAPI.
 */
class PersonDetailViewModelImpl @Inject constructor(private val peopleRemoteDataSource: PeopleRemoteDataSource) :
    PersonDetailViewModel, ViewModel() {

    /**
     * Emits a [List] of [DetailListItem]s that should be shown on the UI.
     */
    override val personDetailListItemsLiveData: MutableLiveData<List<DetailListItem>> =
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
     * UI. Exposes the data via [personDetailListItemsLiveData] when done.
     *
     * @param id Unique id of the person to fetch.
     */
    override fun getPerson(@IntRange(from = 1) id: Int) {
        disposable.add(peopleRemoteDataSource.getPerson(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                showLoadingLiveData.postValue(true)
                showErrorLiveData.postValue(false)
            }
            .subscribe(
                { personResponse ->
                    showLoadingLiveData.postValue(false)
                    val newDetailListItems = listOf(
                        DetailListItem(R.string.name_detail_label, personResponse.name),
                        DetailListItem(
                            R.string.homeworld_detail_label,
                            personResponse.homeworldURL
                        ),
                        DetailListItem(
                            R.string.birth_year_detail_label,
                            personResponse.birthYear
                        ),
                        DetailListItem(
                            R.string.species_detail_label,
                            personResponse.speciesURLs.toString()
                        ),
                        DetailListItem(R.string.gender_detail_label, personResponse.gender),
                        DetailListItem(R.string.height_detail_label, personResponse.height),
                        DetailListItem(R.string.mass_detail_label, personResponse.mass),
                        DetailListItem(
                            R.string.hair_color_detail_label,
                            personResponse.hairColor
                        ),
                        DetailListItem(
                            R.string.eye_color_detail_label,
                            personResponse.eyeColor
                        ),
                        DetailListItem(
                            R.string.skin_color_detail_label,
                            personResponse.skinColor
                        ),
                        DetailListItem(
                            R.string.films_detail_label,
                            personResponse.filmsURLs.toString()
                        ),
                        DetailListItem(
                            R.string.starships_detail_label,
                            personResponse.starshipsURLs.toString()
                        ),
                        DetailListItem(
                            R.string.vehicles_detail_label,
                            personResponse.vehiclesURLs.toString()
                        )
                    )
                    personDetailListItemsLiveData.postValue(newDetailListItems)
                },
                { throwable ->
                    showLoadingLiveData.postValue(false)
                    showErrorLiveData.postValue(true)
                    Log.e(TAG, throwable.toString())
                }
            )
        )
    }

    companion object {
        private const val TAG = "PersonDetailViewModelImpl"
    }
}