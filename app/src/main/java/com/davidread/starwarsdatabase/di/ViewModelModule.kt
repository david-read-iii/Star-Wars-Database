package com.davidread.starwarsdatabase.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidread.starwarsdatabase.util.ViewModelFactory
import com.davidread.starwarsdatabase.viewmodel.*
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

    @Binds
    @IntoMap
    @ViewModelKey(FilmNamesViewModelImpl::class)
    abstract fun bindFilmNamesViewModelImpl(viewModel: FilmNamesViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilmDetailsViewModelImpl::class)
    abstract fun bindFilmDetailsViewModelImpl(viewModel: FilmDetailsViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StarshipNamesViewModelImpl::class)
    abstract fun bindStarshipNamesViewModelImpl(viewModel: StarshipNamesViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StarshipDetailsViewModelImpl::class)
    abstract fun bindStarshipDetailsViewModelImpl(viewModel: StarshipDetailsViewModelImpl): ViewModel
}