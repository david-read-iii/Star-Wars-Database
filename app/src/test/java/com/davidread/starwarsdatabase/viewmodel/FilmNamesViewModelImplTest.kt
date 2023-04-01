package com.davidread.starwarsdatabase.viewmodel

import com.davidread.starwarsdatabase.datasource.FilmsRemoteDataSource
import com.davidread.starwarsdatabase.model.datasource.PageResponse
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
    fun `given datasource that returns success response, when viewmodel calls init, then viewmodel emits the expected resource names in the UI list`() {
        val response = getSuccessfulPageResponseOfFilms()
        val dataSource = mockk<FilmsRemoteDataSource> {
            every { getFilms(any()) } returns Single.just(response)
        }
        val viewModel = FilmNamesViewModelImpl(dataSource)

        val expectedList = IntRange(1, 10).map { id ->
            ResourceNameListItem.ResourceName(id = id, name = "Film $id")
        }
        val actualList = viewModel.resourceNamesLiveData.value
        Assert.assertEquals(expectedList, actualList)
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

    private fun getSuccessfulPageResponseOfFilms(next: String? = null) = PageResponse(
        results = IntRange(1, 10).map { id ->
            BaseViewModelImplTestConstants.getSuccessfulFilmResponse(id)
        },
        next = next
    )
}