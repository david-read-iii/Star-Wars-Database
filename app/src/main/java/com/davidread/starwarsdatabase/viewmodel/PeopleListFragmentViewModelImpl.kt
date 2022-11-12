package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.model.view.PersonListItem
import com.davidread.starwarsdatabase.util.extractIDFromURL
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Exposes state and encapsulates business logic related to the people category list.
 *
 * @property peopleRemoteDataSource [PeopleRemoteDataSource] implementation by `Retrofit` for
 * fetching people data from SWAPI.
 */
class PeopleListFragmentViewModelImpl @Inject constructor(private val peopleRemoteDataSource: PeopleRemoteDataSource) :
    ViewModel(), PeopleListFragmentViewModel {

    /**
     * Emits a [List] of [PersonListItem]s that should be displayed in the UI.
     */
    override val personListItemsLiveData = MutableLiveData<List<PersonListItem>>()

    /**
     * Container for managing resources used by `Disposable`s or their subclasses.
     */
    private val disposable: CompositeDisposable = CompositeDisposable()

    /**
     * Called when this `ViewModel` is initialized. It sets up a subscription for getting a simple
     * list of people from SWAPI to show in the UI.
     */
    init {
        disposable.add(peopleRemoteDataSource.getPeople()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                personListItemsLiveData.postValue(listOf(PersonListItem.LoadingItem))
            }
            .subscribe(
                { personResponse ->
                    val personListItems = personResponse.results.map { person ->
                        PersonListItem.PersonItem(person.url.extractIDFromURL(), person.name)
                    }
                    personListItemsLiveData.postValue(personListItems)
                },
                { throwable ->
                    personListItemsLiveData.postValue(listOf(PersonListItem.ErrorItem))
                    Log.e(TAG, throwable.toString())
                }
            )
        )
    }

    /**
     * Called when this `ViewModel` is no longer used and will be destroyed. Clears any
     * subscriptions held by [disposable] to prevent this `ViewModel` from leaking.
     */
    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    companion object {
        private const val TAG = "PeopleListFragmentViewModelImpl"
    }
}