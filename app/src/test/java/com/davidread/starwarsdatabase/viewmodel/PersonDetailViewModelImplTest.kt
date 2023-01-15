package com.davidread.starwarsdatabase.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.davidread.starwarsdatabase.RxImmediateSchedulerRule
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests that verify the correctness of [PersonDetailViewModelImpl].
 */
class PersonDetailViewModelImplTest {

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

    @Test
    fun `given datasource that returns success response, when viewmodel calls getPerson(), then viewmodel emits 13 detail list items in the UI list`() {
        val response = getSuccessfulPersonResponse()
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.just(response)
        }
        val viewModel = PersonDetailViewModelImpl(dataSource)
        viewModel.getPerson(1)

        val actualList = viewModel.personDetailListItemsLiveData.value
        Assert.assertEquals(13, actualList!!.size)
    }

    @Test
    fun `given datasource that returns success response, when viewmodel calls getPerson(), then viewmodel does not emit an error status to the UI`() {
        val response = getSuccessfulPersonResponse()
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.just(response)
        }
        val viewModel = PersonDetailViewModelImpl(dataSource)
        viewModel.getPerson(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertFalse(actual!!)
    }

    @Test
    fun `given datasource that returns error response, when viewmodel calls getPerson(), then viewmodel emits an error status to the UI`() {
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.error(Throwable())
        }
        val viewModel = PersonDetailViewModelImpl(dataSource)
        viewModel.getPerson(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertTrue(actual!!)
    }

    private fun getSuccessfulPersonResponse() = ResourceResponse.Person(
        "Luke Skywalker",
        "19BBY",
        "blue",
        "male",
        "blond",
        "172",
        "77",
        "fair",
        "https://swapi.dev/api/planets/1/",
        listOf(
            "https://swapi.dev/api/films/1/",
            "https://swapi.dev/api/films/2/",
            "https://swapi.dev/api/films/3/",
            "https://swapi.dev/api/films/6/"
        ),
        listOf(),
        listOf(
            "https://swapi.dev/api/starships/12/",
            "https://swapi.dev/api/starships/22/"
        ),
        listOf(
            "https://swapi.dev/api/vehicles/14/",
            "https://swapi.dev/api/vehicles/30/"
        ),
        "https://swapi.dev/api/people/1/"
    )
}