package com.davidread.starwarsdatabase.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Utility responsible for instantiating [ViewModel] subclasses in the entire application.
 */
class ViewModelFactory @Inject constructor(private val classProviderMap: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given `Class`.
     *
     * @param modelClass A `Class` whose instance is requested
     * @return A newly created ViewModel
     * @throws IllegalArgumentException Thrown if a mapping for [modelClass] doesn't exist in
     * [classProviderMap].
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = classProviderMap[modelClass]
        return viewModelProvider?.let {
            viewModelProvider.get() as T
        } ?: run {
            throw IllegalArgumentException("Unknown ViewModel class $modelClass")
        }
    }
}