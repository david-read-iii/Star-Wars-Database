package com.davidread.starwarsdatabase.viewmodel

import android.util.Log
import androidx.annotation.IntRange
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import com.davidread.starwarsdatabase.util.extractIDFromURL
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Exposes state and encapsulates business logic related to the person names list.
 *
 * @property peopleRemoteDataSource [PeopleRemoteDataSource] implementation by `Retrofit` for
 * fetching people data from SWAPI.
 */
class PersonNamesViewModelImpl @Inject constructor(private val peopleRemoteDataSource: PeopleRemoteDataSource) :
    PersonNamesViewModel, ViewModel() {

    /**
     * Emits a [List] of [ResourceNameListItem]s that should be shown on the UI.
     */
    override val personNamesLiveData: MutableLiveData<List<ResourceNameListItem>> =
        MutableLiveData()

    /**
     * Whether all [ResourceNameListItem]s have been fetched from SWAPI.
     */
    override val isAllPersonNamesRequestedLiveData: MutableLiveData<Boolean> =
        MutableLiveData(false)

    /**
     * [MutableList] of [ResourceNameListItem]s to be stored/modified here. Emitted via its `LiveData`
     * each time it is updated.
     */
    private val personNames: MutableList<ResourceNameListItem> = mutableListOf()

    /**
     * Container for managing resources used by `Disposable`s or their subclasses.
     */
    private val disposable: CompositeDisposable = CompositeDisposable()

    /**
     * Called when this `ViewModel` is initially created. It sets up the initial subscription for
     * getting page 1 of person names to show in the UI.
     */
    init {
        getPersonNames(1)
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
     * Sets up a subscription for getting a page of person names (10 in each page) from SWAPI to
     * show in the UI. Exposes the data via [personNamesLiveData] when done.
     *
     * @param page Which page of person names to fetch.
     */
    override fun getPersonNames(@IntRange(from = 1) page: Int) {
        disposable.add(peopleRemoteDataSource.getPeople(page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                personNames.apply {
                    remove(ResourceNameListItem.Error)
                    add(ResourceNameListItem.Loading)
                }
                personNamesLiveData.postValue(personNames)
            }
            .subscribe(
                { pageResponse ->
                    val newPersonNames = pageResponse.results.map { personResponse ->
                        ResourceNameListItem.ResourceName(
                            id = personResponse.url.extractIDFromURL(),
                            name = personResponse.name
                        )
                    }
                    personNames.apply {
                        remove(ResourceNameListItem.Loading)
                        addAll(newPersonNames)
                    }
                    personNamesLiveData.postValue(personNames)
                    if (pageResponse.next == null) {
                        isAllPersonNamesRequestedLiveData.postValue(true)
                    }
                },
                { throwable ->
                    personNames.apply {
                        remove(ResourceNameListItem.Loading)
                        add(ResourceNameListItem.Error)
                    }
                    personNamesLiveData.postValue(personNames)
                    Log.e(TAG, throwable.toString())
                }
            )
        )
    }

    companion object {
        private const val TAG = "PersonNamesViewModelImpl"
    }
}