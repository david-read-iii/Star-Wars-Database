package com.davidread.starwarsdatabase.di

import android.app.Application
import android.content.Context
import com.davidread.starwarsdatabase.view.ResourceNamesFragment
import com.davidread.starwarsdatabase.view.ResourceDetailsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Defines the dependencies graph for the entire application.
 */
@Singleton
@Component(modules = [RemoteDataSourceModule::class, ViewModelModule::class])
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
    fun inject(fragment: ResourceNamesFragment)
    fun inject(fragment: ResourceDetailsFragment)
}