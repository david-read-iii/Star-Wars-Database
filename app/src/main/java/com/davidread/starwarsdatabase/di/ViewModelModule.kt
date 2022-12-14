package com.davidread.starwarsdatabase.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidread.starwarsdatabase.util.ViewModelFactory
import com.davidread.starwarsdatabase.viewmodel.PeopleListFragmentViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

/**
 * Defines which [ViewModelProvider.Factory] and [ViewModel] instances to inject for the entire
 * application.
 */
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @Singleton
    @IntoMap
    @ViewModelKey(PeopleListFragmentViewModelImpl::class)
    abstract fun bindPeopleListFragmentViewModelImpl(viewModel: PeopleListFragmentViewModelImpl): ViewModel
}