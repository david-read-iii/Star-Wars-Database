package com.davidread.starwarsdatabase.viewmodel

import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Test

/**
 * Unit tests that verify the correctness of [PersonNamesViewModelImpl].
 */
class PersonNamesViewModelImplTest : BaseViewModelImplTest() {

    @Test
    fun `given datasource that returns success response, when viewmodel calls init, then viewmodel emits 10 person items in the UI list`() {
        val response = getSuccessfulPageResponseOfPeople()
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PersonNamesViewModelImpl(dataSource)

        val actualList = viewModel.personNamesLiveData.value
        Assert.assertEquals(response.results.size, actualList!!.size)
        for (item in actualList) {
            Assert.assertTrue(item is ResourceNameListItem.ResourceName)
        }
    }

    @Test
    fun `given datasource that returns error response, when viewmodel calls init, then viewmodel emits an error item in the UI list`() {
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.error(Throwable())
        }
        val viewModel = PersonNamesViewModelImpl(dataSource)

        val actualList = viewModel.personNamesLiveData.value
        Assert.assertEquals(1, actualList!!.size)
        Assert.assertTrue(actualList.first() is ResourceNameListItem.Error)
    }

    @Test
    fun `given datasource that returns a non-null next status, when viewmodel calls init, then viewmodel emits list not full status`() {
        val next = "https://swapi.dev/api/people/?page=2"
        val response = getSuccessfulPageResponseOfPeople(next)
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PersonNamesViewModelImpl(dataSource)

        Assert.assertFalse(viewModel.isAllPersonNamesRequestedLiveData.value!!)
    }

    @Test
    fun `given datasource that returns a null next status, when viewmodel calls init, then viewmodel emits list full status`() {
        val next = null
        val response = getSuccessfulPageResponseOfPeople(next)
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PersonNamesViewModelImpl(dataSource)

        Assert.assertTrue(viewModel.isAllPersonNamesRequestedLiveData.value!!)
    }

    @Test
    fun `given datasource that returns error response, when viewmodel calls init, then viewmodel emits list not full status`() {
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.error(Throwable())
        }
        val viewModel = PersonNamesViewModelImpl(dataSource)

        Assert.assertFalse(viewModel.isAllPersonNamesRequestedLiveData.value!!)
    }

    @Test
    fun `given datasource that returns a success next URL, when viewmodel calls init, then viewmodel increments nextPage`() {
        val next = "https://swapi.dev/api/people/?page=2"
        val response = getSuccessfulPageResponseOfPeople(next)
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PersonNamesViewModelImpl(dataSource)

        Assert.assertEquals(2, viewModel.nextPage)
    }

    @Test
    fun `given datasource that returns a null next URL, when viewmodel calls init, then viewmodel does not increment nextPage`() {
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.error(Throwable())
        }
        val viewModel = PersonNamesViewModelImpl(dataSource)

        Assert.assertEquals(1, viewModel.nextPage)
    }

    @Test
    fun `given datasource that returns an incorrect next URL, when viewmodel calls init, then viewmodel does not increment nextPage`() {
        val next = "Oops I am not a URL"
        val response = getSuccessfulPageResponseOfPeople(next)
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PersonNamesViewModelImpl(dataSource)

        Assert.assertEquals(1, viewModel.nextPage)
    }

    private fun getSuccessfulPageResponseOfPeople(next: String? = null): PageResponse<ResourceResponse.Person> {
        val results = listOf<ResourceResponse.Person>(
            mockk {
                every { name } returns "Luke Skywalker"
                every { url } returns "https://swapi.dev/api/people/1/"
            },
            mockk {
                every { name } returns "C-3PO"
                every { url } returns "https://swapi.dev/api/people/2/"
            },
            mockk {
                every { name } returns "R2-D2"
                every { url } returns "https://swapi.dev/api/people/3/"
            },
            mockk {
                every { name } returns "Darth Vader"
                every { url } returns "https://swapi.dev/api/people/4/"
            },
            mockk {
                every { name } returns "Leia Organa"
                every { url } returns "https://swapi.dev/api/people/5/"
            },
            mockk {
                every { name } returns "Owen Lars"
                every { url } returns "https://swapi.dev/api/people/6/"
            },
            mockk {
                every { name } returns "Beru Whitesun lars"
                every { url } returns "https://swapi.dev/api/people/7/"
            },
            mockk {
                every { name } returns "R5-D4"
                every { url } returns "https://swapi.dev/api/people/8/"
            },
            mockk {
                every { name } returns "R5-D4"
                every { url } returns "https://swapi.dev/api/people/9/"
            },
            mockk {
                every { name } returns "Obi-Wan Kenobi"
                every { url } returns "https://swapi.dev/api/people/10/"
            }
        )
        return PageResponse(results, next)
    }
}