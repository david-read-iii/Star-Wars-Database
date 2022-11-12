package com.davidread.starwarsdatabase.di

import android.app.Application
import android.content.Context
import com.davidread.starwarsdatabase.view.PeopleListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Defines the dependencies graph for the entire application.
 */
@Singleton
@Component(modules = [ViewModelModule::class])
interface ApplicationGraph {

    /**
     * Factory for creating an instance of this class.
     */
    @Component.Factory
    interface Factory {

        /**
         * Creates an [ApplicationGraph] instance.
         */
        fun create(
            @BindsInstance context: Context,
            @BindsInstance application: Application
        ): ApplicationGraph
    }

    // Activities and fragments requesting injection.
    fun inject(fragment: PeopleListFragment)
}