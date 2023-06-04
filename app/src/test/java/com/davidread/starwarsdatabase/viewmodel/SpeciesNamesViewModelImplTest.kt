package com.davidread.starwarsdatabase.viewmodel

import android.view.View
import androidx.lifecycle.Observer
import com.davidread.starwarsdatabase.datasource.SpeciesRemoteDataSource
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Test

/**
 * Unit tests that verify the correctness of [SpeciesNamesViewModelImpl].
 */
class SpeciesNamesViewModelImplTest : BaseViewModelImplTest() {

    @Test
    fun `given datasource that returns success response, when viewmodel calls init, then viewmodel emits the expected resource names in the UI list`() {
        val response = getSuccessfulPageResponseOfSpecies()
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.just(response)
        }
        val viewModel = SpeciesNamesViewModelImpl(dataSource)

        val expectedList = IntRange(1, 10).map { id ->
            ResourceNameListItem.ResourceName(
                id = id,
                name = "Species $id",
                backgroundAttrResId = android.R.attr.selectableItemBackground
            )
        }
        val actualList = viewModel.resourceNamesLiveData.value
        Assert.assertEquals(expectedList, actualList)
    }

    @Test
    fun `given datasource that returns error response, when viewmodel calls init, then viewmodel emits an error in the UI list`() {
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.error(Throwable())
        }
        val viewModel = SpeciesNamesViewModelImpl(dataSource)

        val actualList = viewModel.resourceNamesLiveData.value
        Assert.assertEquals(1, actualList!!.size)
        Assert.assertTrue(actualList.first() is ResourceNameListItem.Error)
    }

    @Test
    fun `when viewmodel calls init, then viewmodel emits exactly one event via smoothScrollToPositionInListLiveData`() {
        val response = getSuccessfulPageResponseOfSpecies()
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.just(response)
        }
        val liveDataObserver = mockk<Observer<in Int>>(relaxed = true)
        SpeciesNamesViewModelImpl(dataSource).apply {
            smoothScrollToPositionInListLiveData.observeForever(liveDataObserver)
        }

        verify(exactly = 1) {
            liveDataObserver.onChanged(0)
        }
    }

    @Test
    fun `given datasource that returns a non-null next status, when viewmodel calls init, then viewmodel emits true isLoadMoreResourceNamesOnScrollListenerEnabledLiveData`() {
        val next = "https://swapi.dev/api/species/?page=2"
        val response = getSuccessfulPageResponseOfSpecies(next)
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.just(response)
        }
        val viewModel = SpeciesNamesViewModelImpl(dataSource)

        Assert.assertTrue(viewModel.isLoadMoreResourceNamesOnScrollListenerEnabledLiveData.value!!)
    }

    @Test
    fun `given datasource that returns a null next status, when viewmodel calls init, then viewmodel emits false isLoadMoreResourceNamesOnScrollListenerEnabledLiveData`() {
        val next = null
        val response = getSuccessfulPageResponseOfSpecies(next)
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.just(response)
        }
        val viewModel = SpeciesNamesViewModelImpl(dataSource)

        Assert.assertFalse(viewModel.isLoadMoreResourceNamesOnScrollListenerEnabledLiveData.value!!)
    }

    @Test
    fun `given datasource that returns error response, when viewmodel calls init, then viewmodel emits false isLoadMoreResourceNamesOnScrollListenerEnabledLiveData`() {
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.error(Throwable())
        }
        val viewModel = SpeciesNamesViewModelImpl(dataSource)

        Assert.assertFalse(viewModel.isLoadMoreResourceNamesOnScrollListenerEnabledLiveData.value!!)
    }

    @Test
    fun `given datasource that returns a success response, when viewmodel calls init, then viewmodel emits visible status for subNavHostFragment`() {
        val response = getSuccessfulPageResponseOfSpecies()
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.just(response)
        }
        val viewModel = SpeciesNamesViewModelImpl(dataSource)

        Assert.assertEquals(View.VISIBLE, viewModel.subNavHostFragmentVisibility.value)
    }

    @Test
    fun `given datasource that returns an error response, when viewmodel calls init, then viewmodel emits gone status for subNavHostFragment`() {
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.error(Throwable())
        }
        val viewModel = SpeciesNamesViewModelImpl(dataSource)

        Assert.assertEquals(View.GONE, viewModel.subNavHostFragmentVisibility.value)
    }

    @Test
    fun `given datasource that returns a success next URL, when viewmodel calls init, then viewmodel increments nextPage`() {
        val next = "https://swapi.dev/api/species/?page=2"
        val response = getSuccessfulPageResponseOfSpecies(next)
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.just(response)
        }
        val viewModel = SpeciesNamesViewModelImpl(dataSource)

        Assert.assertEquals(2, viewModel.nextPage)
    }

    @Test
    fun `given datasource that returns a null next URL, when viewmodel calls init, then viewmodel does not increment nextPage`() {
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.error(Throwable())
        }
        val viewModel = SpeciesNamesViewModelImpl(dataSource)

        Assert.assertEquals(1, viewModel.nextPage)
    }

    @Test
    fun `given datasource that returns an incorrect next URL, when viewmodel calls init, then viewmodel does not increment nextPage`() {
        val next = "Oops I am not a URL"
        val response = getSuccessfulPageResponseOfSpecies(next)
        val dataSource = mockk<SpeciesRemoteDataSource> {
            every { getSpecies(any()) } returns Single.just(response)
        }
        val viewModel = SpeciesNamesViewModelImpl(dataSource)

        Assert.assertEquals(1, viewModel.nextPage)
    }

    private fun getSuccessfulPageResponseOfSpecies(next: String? = null) = PageResponse(
        results = IntRange(1, 10).map { id ->
            BaseViewModelImplTestConstants.getSuccessfulSpeciesResponse(id)
        },
        next = next
    )
}