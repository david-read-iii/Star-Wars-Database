package com.davidread.starwarsdatabase.viewmodel

import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.datasource.FilmsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PlanetsRemoteDataSource
import com.davidread.starwarsdatabase.model.view.ResourceDetailListItem
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Test

/**
 * Unit tests that verify the correctness of [PlanetDetailsViewModelImpl].
 */
class PlanetDetailsViewModelImplTest : BaseViewModelImplTest() {

    @Test
    fun `given that all datasources return success response, when viewmodel calls getResourceDetails(), then viewmodel emits the expected detail list items in the UI list`() {
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPlanetResponse())
        }
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            for (id in 1..3) {
                every { getPerson(id) } returns Single.just(
                    BaseViewModelImplTestConstants.getSuccessfulPersonResponse(id)
                )
            }
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            for (id in 1..3) {
                every { getFilm(id) } returns Single.just(
                    BaseViewModelImplTestConstants.getSuccessfulFilmResponse(id)
                )
            }
        }
        val viewModel = PlanetDetailsViewModelImpl(
            planetsRemoteDataSource,
            peopleRemoteDataSource,
            filmsRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val expectedList = listOf(
            ResourceDetailListItem(R.string.name_detail_label, "Planet 1"),
            ResourceDetailListItem(R.string.rotation_period_detail_label, "23"),
            ResourceDetailListItem(R.string.orbital_period_detail_label, "304"),
            ResourceDetailListItem(R.string.diameter_detail_label, "10465"),
            ResourceDetailListItem(R.string.climate_detail_label, "arid"),
            ResourceDetailListItem(R.string.gravity_detail_label, "1 standard"),
            ResourceDetailListItem(R.string.terrain_detail_label, "desert"),
            ResourceDetailListItem(R.string.surface_water_detail_label, "1"),
            ResourceDetailListItem(R.string.population_detail_label, "200000"),
            ResourceDetailListItem(R.string.residents_detail_label, "Person 1, Person 2, Person 3"),
            ResourceDetailListItem(R.string.films_detail_label, "Film 1, Film 2, Film 3")
        )
        val actualList = viewModel.resourceDetailsLiveData.value
        Assert.assertEquals(expectedList, actualList)
    }

    @Test
    fun `given that all datasources return success response, when viewmodel calls getResourceDetails(), then viewmodel does not emit an error status to the UI`() {
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPlanetResponse())
        }
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPersonResponse())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulFilmResponse())
        }
        val viewModel = PlanetDetailsViewModelImpl(
            planetsRemoteDataSource,
            peopleRemoteDataSource,
            filmsRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertFalse(actual!!)
    }

    @Test
    fun `given all datasources return error response, when viewmodel calls getResourceDetails(), then viewmodel emits an error status to the UI`() {
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.error(Throwable())
        }
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.error(Throwable())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.error(Throwable())
        }
        val viewModel = PlanetDetailsViewModelImpl(
            planetsRemoteDataSource,
            peopleRemoteDataSource,
            filmsRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertTrue(actual!!)
    }
}