package com.davidread.starwarsdatabase.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidread.starwarsdatabase.util.ViewModelFactory
import com.davidread.starwarsdatabase.viewmodel.PersonNamesViewModelImpl
import com.davidread.starwarsdatabase.viewmodel.PersonDetailsViewModelImpl
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
    @ViewModelKey(PersonNamesViewModelImpl::class)
    abstract fun bindPersonNamesViewModelImpl(viewModel: PersonNamesViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonDetailsViewModelImpl::class)
    abstract fun bindPersonDetailsViewModelImpl(viewModel: PersonDetailsViewModelImpl): ViewModel
}