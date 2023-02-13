package com.davidread.starwarsdatabase.viewmodel

import com.davidread.starwarsdatabase.datasource.FilmsRemoteDataSource
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Test

/**
 * Unit tests that verify the correctness of [FilmNamesViewModelImpl].
 */
class FilmNamesViewModelImplTest : BaseViewModelImplTest() {

    @Test
    fun `given datasource that returns success response, when viewmodel calls init, then viewmodel emits 6 resource names in the UI list`() {
        val response = getSuccessfulPageResponseOfFilms()
        val dataSource = mockk<FilmsRemoteDataSource> {
            every { getFilms(any()) } returns Single.just(response)
        }
        val viewModel = FilmNamesViewModelImpl(dataSource)

        val actualList = viewModel.resourceNamesLiveData.value
        Assert.assertEquals(response.results.size, actualList!!.size)
        for (item in actualList) {
            Assert.assertTrue(item is ResourceNameListItem.ResourceName)
        }
    }

    @Test
    fun `given datasource that returns error response, when viewmodel calls init, then viewmodel emits an error in the UI list`() {
        val dataSource = mockk<FilmsRemoteDataSource> {
            every { getFilms(any()) } returns Single.error(Throwable())
        }
        val viewModel = FilmNamesViewModelImpl(dataSource)

        val actualList = viewModel.resourceNamesLiveData.value
        Assert.assertEquals(1, actualList!!.size)
        Assert.assertTrue(actualList.first() is ResourceNameListItem.Error)
    }

    @Test
    fun `given datasource that returns a non-null next status, when viewmodel calls init, then viewmodel emits list not full status`() {
        val next = "https://swapi.dev/api/films/?page=2"
        val response = getSuccessfulPageResponseOfFilms(next)
        val dataSource = mockk<FilmsRemoteDataSource> {
            every { getFilms(any()) } returns Single.just(response)
        }
        val viewModel = FilmNamesViewModelImpl(dataSource)

        Assert.assertFalse(viewModel.isAllResourceNamesRequestedLiveData.value!!)
    }

    @Test
    fun `given datasource that returns a null next status, when viewmodel calls init, then viewmodel emits list full status`() {
        val next = null
        val response = getSuccessfulPageResponseOfFilms(next)
        val dataSource = mockk<FilmsRemoteDataSource> {
            every { getFilms(any()) } returns Single.just(response)
        }
        val viewModel = FilmNamesViewModelImpl(dataSource)

        Assert.assertTrue(viewModel.isAllResourceNamesRequestedLiveData.value!!)
    }

    @Test
    fun `given datasource that returns error response, when viewmodel calls init, then viewmodel emits list not full status`() {
        val dataSource = mockk<FilmsRemoteDataSource> {
            every { getFilms(any()) } returns Single.error(Throwable())
        }
        val viewModel = FilmNamesViewModelImpl(dataSource)

        Assert.assertFalse(viewModel.isAllResourceNamesRequestedLiveData.value!!)
    }

    @Test
    fun `given datasource that returns a success next URL, when viewmodel calls init, then viewmodel increments nextPage`() {
        val next = "https://swapi.dev/api/films/?page=2"
        val response = getSuccessfulPageResponseOfFilms(next)
        val dataSource = mockk<FilmsRemoteDataSource> {
            every { getFilms(any()) } returns Single.just(response)
        }
        val viewModel = FilmNamesViewModelImpl(dataSource)

        Assert.assertEquals(2, viewModel.nextPage)
    }

    @Test
    fun `given datasource that returns a null next URL, when viewmodel calls init, then viewmodel does not increment nextPage`() {
        val dataSource = mockk<FilmsRemoteDataSource> {
            every { getFilms(any()) } returns Single.error(Throwable())
        }
        val viewModel = FilmNamesViewModelImpl(dataSource)

        Assert.assertEquals(1, viewModel.nextPage)
    }

    @Test
    fun `given datasource that returns an incorrect next URL, when viewmodel calls init, then viewmodel does not increment nextPage`() {
        val next = "Oops I am not a URL"
        val response = getSuccessfulPageResponseOfFilms(next)
        val dataSource = mockk<FilmsRemoteDataSource> {
            every { getFilms(any()) } returns Single.just(response)
        }
        val viewModel = FilmNamesViewModelImpl(dataSource)

        Assert.assertEquals(1, viewModel.nextPage)
    }

    private fun getSuccessfulPageResponseOfFilms(next: String? = null): PageResponse<ResourceResponse.Film> {
        val results = listOf<ResourceResponse.Film>(
            mockk {
                every { title } returns "A New Hope"
                every { url } returns "https://swapi.dev/api/films/1/"
            },
            mockk {
                every { title } returns "The Empire Strikes Back"
                every { url } returns "https://swapi.dev/api/films/2/"
            },
            mockk {
                every { title } returns "Return of the Jedi"
                every { url } returns "https://swapi.dev/api/films/3/"
            },
            mockk {
                every { title } returns "The Phantom Menace"
                every { url } returns "https://swapi.dev/api/films/4/"
            },
            mockk {
                every { title } returns "Attack of the Clones"
                every { url } returns "https://swapi.dev/api/films/5/"
            },
            mockk {
                every { title } returns "Revenge of the Sith"
                every { url } returns "https://swapi.dev/api/films/6/"
            }
        )
        return PageResponse(results, next)
    }
}