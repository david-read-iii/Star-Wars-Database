package com.davidread.starwarsdatabase.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

/**
 * Base class for `...ViewModelImplTest` classes to inherit. Provides rules for the inheriting test
 * class to use in order for it to conduct unit tests on `ViewModel` subclasses that use
 * Architecture Components and RxJava.
 */
abstract class BaseViewModelImplTest {

    /**
     * Rule that swaps the background executor used by the Architecture Components with one that
     * executes each task synchronously.
     */
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    /**
     * Rule that forces RxJava to execute it's load on the main thread.
     */
    @Rule
    @JvmField
    val testSchedulerRule = RxImmediateSchedulerRule()

}