package com.davidread.starwarsdatabase.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.davidread.starwarsdatabase.RxImmediateSchedulerRule
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.model.datasource.Person
import com.davidread.starwarsdatabase.model.datasource.PersonResponse
import com.davidread.starwarsdatabase.model.view.PersonListItem
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests that verify the correctness of [PeopleViewModelImpl].
 */
class PeopleViewModelImplTest {

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
    fun `given datasource that returns success response, when viewmodel calls init, then viewmodel emits 10 person items in the UI list`() {
        val response = getSuccessfulPersonResponse()
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PeopleViewModelImpl(dataSource)

        val actualList = viewModel.personListItemsLiveData.value
        Assert.assertEquals(response.results.size, actualList!!.size)
        for (item in actualList) {
            Assert.assertTrue(item is PersonListItem.PersonItem)
        }
    }

    @Test
    fun `given datasource that returns error response, when viewmodel calls init, then viewmodel emits an error item in the UI list`() {
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.error(Throwable())
        }
        val viewModel = PeopleViewModelImpl(dataSource)

        val actualList = viewModel.personListItemsLiveData.value
        Assert.assertEquals(1, actualList!!.size)
        Assert.assertTrue(actualList.first() is PersonListItem.ErrorItem)
    }

    @Test
    fun `given datasource that returns a non-null next status, when viewmodel calls init, then viewmodel emits list not full status`() {
        val next = "https://swapi.dev/api/people/?page=2"
        val response = getSuccessfulPersonResponse(next)
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PeopleViewModelImpl(dataSource)

        Assert.assertFalse(viewModel.isAllPersonListItemsRequestedLiveData.value!!)
    }

    @Test
    fun `given datasource that returns a null next status, when viewmodel calls init, then viewmodel emits list full status`() {
        val next = null
        val response = getSuccessfulPersonResponse(next)
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PeopleViewModelImpl(dataSource)

        Assert.assertTrue(viewModel.isAllPersonListItemsRequestedLiveData.value!!)
    }

    @Test
    fun `given datasource that returns error response, when viewmodel calls init, then viewmodel emits list not full status`() {
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.error(Throwable())
        }
        val viewModel = PeopleViewModelImpl(dataSource)

        Assert.assertFalse(viewModel.isAllPersonListItemsRequestedLiveData.value!!)
    }

    private fun getSuccessfulPersonResponse(next: String? = null) = PersonResponse(
        listOf(
            Person("Luke Skywalker", "https://swapi.dev/api/people/1/"),
            Person("C-3PO", "https://swapi.dev/api/people/2/"),
            Person("R2-D2", "https://swapi.dev/api/people/3/"),
            Person("Darth Vader", "https://swapi.dev/api/people/4/"),
            Person("Leia Organa", "https://swapi.dev/api/people/5/"),
            Person("Owen Lars", "https://swapi.dev/api/people/6/"),
            Person("Owen Lars", "https://swapi.dev/api/people/7/"),
            Person("R5-D4", "https://swapi.dev/api/people/8/"),
            Person("Biggs Darklighter", "https://swapi.dev/api/people/9/"),
            Person("Obi-Wan Kenobi", "https://swapi.dev/api/people/10/")
        ),
        next
    )
}