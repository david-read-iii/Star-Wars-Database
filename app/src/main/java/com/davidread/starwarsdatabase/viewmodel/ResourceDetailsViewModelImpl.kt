package com.davidread.starwarsdatabase.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidread.starwarsdatabase.model.view.ResourceDetailListItem
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Base class for `{ResourceName}DetailsViewModelImpl` classes to extend. Contains common logic used
 * across all such `ViewModel`s.
 */
abstract class ResourceDetailsViewModelImpl : ResourceDetailsViewModel, ViewModel() {

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
    val disposable: CompositeDisposable = CompositeDisposable()

    /**
     * Called when this `ViewModel` is no longer used and will be destroyed. Clears any
     * subscriptions held by [disposable] to prevent this `ViewModel` from leaking.
     */
    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}