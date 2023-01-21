package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.annotation.IntRange
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
class PeopleListViewModelImpl @Inject constructor(private val peopleRemoteDataSource: PeopleRemoteDataSource) :
    PeopleListViewModel, ViewModel() {

    /**
     * Emits a [List] of [PersonListItem]s that should be shown on the UI.
     */
    override val personListItemsLiveData: MutableLiveData<List<PersonListItem>> = MutableLiveData()

    /**
     * Whether all [PersonListItem]s have been fetched from SWAPI.
     */
    override val isAllPersonListItemsRequestedLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * [MutableList] of [PersonListItem]s to be stored/modified here. Emitted via its `LiveData`
     * each time it is updated.
     */
    private val personListItems: MutableList<PersonListItem> = mutableListOf()

    /**
     * Container for managing resources used by `Disposable`s or their subclasses.
     */
    private val disposable: CompositeDisposable = CompositeDisposable()

    /**
     * Called when this `ViewModel` is initially created. It sets up the initial subscription for
     * getting page 1 of people to show in the UI.
     */
    init {
        getPeople(1)
    }

    /**
     * Called when this `ViewModel` is no longer used and will be destroyed. Clears any
     * subscriptions held by [disposable] to prevent this `ViewModel` from leaking.
     */
    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    /**
     * Sets up a subscription for getting a page of people (10 people in each page) from SWAPI to
     * show in the UI. Exposes the data via [personListItemsLiveData] when done.
     *
     * @param page Which page of people to fetch.
     */
    override fun getPeople(@IntRange(from = 1) page: Int) {
        disposable.add(peopleRemoteDataSource.getPeople(page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                personListItems.apply {
                    remove(PersonListItem.ErrorItem)
                    add(PersonListItem.LoadingItem)
                }
                personListItemsLiveData.postValue(personListItems)
            }
            .subscribe(
                { pageResponse ->
                    val newPersonItems = pageResponse.results.map { personResponse ->
                        PersonListItem.PersonItem(
                            id = personResponse.url.extractIDFromURL(),
                            name = personResponse.name
                        )
                    }
                    personListItems.apply {
                        remove(PersonListItem.LoadingItem)
                        addAll(newPersonItems)
                    }
                    personListItemsLiveData.postValue(personListItems)
                    if (pageResponse.next == null) {
                        isAllPersonListItemsRequestedLiveData.postValue(true)
                    }
                },
                { throwable ->
                    personListItems.apply {
                        remove(PersonListItem.LoadingItem)
                        add(PersonListItem.ErrorItem)
                    }
                    personListItemsLiveData.postValue(personListItems)
                    Log.e(TAG, throwable.toString())
                }
            )
        )
    }

    companion object {
        private const val TAG = "PeopleListViewModelImpl"
    }
}