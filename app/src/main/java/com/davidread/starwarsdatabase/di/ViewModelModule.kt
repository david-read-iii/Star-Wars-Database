package com.davidread.starwarsdatabase.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidread.starwarsdatabase.util.ViewModelFactory
import com.davidread.starwarsdatabase.viewmodel.PeopleListViewModelImpl
import com.davidread.starwarsdatabase.viewmodel.PersonDetailViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Defines which [ViewModelProvider.Factory] and [ViewModel] instances to inject for the entire
 * application.
 */
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PeopleListViewModelImpl::class)
    abstract fun bindPeopleListViewModelImpl(viewModel: PeopleListViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonDetailViewModelImpl::class)
    abstract fun bindPersonDetailViewModelImpl(viewModel: PersonDetailViewModelImpl): ViewModel
}