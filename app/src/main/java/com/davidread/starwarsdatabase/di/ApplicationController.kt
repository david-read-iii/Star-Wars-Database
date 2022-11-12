package com.davidread.starwarsdatabase.di

import android.app.Application

/**
 * Defines an [Application] subclass that has a dependencies graph for the entire application.
 */
class ApplicationController : Application() {

    /**
     * Dependencies graph for the entire application.
     */
    val applicationGraph: ApplicationGraph by lazy {
        DaggerApplicationGraph.factory().create(applicationContext, this)
    }
}