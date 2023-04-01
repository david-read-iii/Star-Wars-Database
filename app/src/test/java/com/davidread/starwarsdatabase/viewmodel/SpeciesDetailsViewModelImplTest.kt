package com.davidread.starwarsdatabase.viewmodel

import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.datasource.FilmsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.datasource.PlanetsRemoteDataSource
import com.davidread.starwarsdatabase.datasource.SpeciesRemoteDataSource
import com.davidread.starwarsdatabase.model.view.ResourceDetailListItem
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Test

/**
 * Unit tests that verify the correctness of [SpeciesDetailsViewModelImpl].
 */
class SpeciesDetailsViewModelImplTest : BaseViewModelImplTest() {

    @Test
    fun `given that all datasources return success response, when viewmodel calls getResourceDetails(), then viewmodel emits the expected detail list items in the UI list`() {
        val speciesRemoteDataSource = mockk<SpeciesRemoteDataSource> {
            every { getSingleSpecies(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulSpeciesResponse())
        }
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
        val viewModel = SpeciesDetailsViewModelImpl(
            speciesRemoteDataSource,
            planetsRemoteDataSource,
            peopleRemoteDataSource,
            filmsRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val expectedList = listOf(
            ResourceDetailListItem(R.string.name_detail_label, "Species 1"),
            ResourceDetailListItem(R.string.classification_detail_label, "mammal"),
            ResourceDetailListItem(R.string.designation_detail_label, "sentient"),
            ResourceDetailListItem(R.string.average_height_detail_label, "180"),
            ResourceDetailListItem(
                R.string.skin_colors_detail_label,
                "caucasian, black, asian, hispanic"
            ),
            ResourceDetailListItem(R.string.hair_colors_detail_label, "blonde, brown, black, red"),
            ResourceDetailListItem(
                R.string.eye_colors_detail_label,
                "brown, blue, green, hazel, grey, amber"
            ),
            ResourceDetailListItem(R.string.average_lifespan_detail_label, "120"),
            ResourceDetailListItem(R.string.homeworld_detail_label, "Planet 1"),
            ResourceDetailListItem(R.string.language_detail_label, "Galactic Basic"),
            ResourceDetailListItem(R.string.people_detail_label, "Person 1, Person 2, Person 3"),
            ResourceDetailListItem(R.string.films_detail_label, "Film 1, Film 2, Film 3")
        )
        val actualList = viewModel.resourceDetailsLiveData.value
        Assert.assertEquals(expectedList, actualList)
    }

    @Test
    fun `given that all datasources return success response, when viewmodel calls getResourceDetails(), then viewmodel does not emit an error status to the UI`() {
        val speciesRemoteDataSource = mockk<SpeciesRemoteDataSource> {
            every { getSingleSpecies(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulSpeciesResponse())
        }
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPlanetResponse())
        }
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPersonResponse())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulFilmResponse())
        }
        val viewModel = SpeciesDetailsViewModelImpl(
            speciesRemoteDataSource,
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
        val speciesRemoteDataSource = mockk<SpeciesRemoteDataSource> {
            every { getSingleSpecies(any()) } returns Single.error(Throwable())
        }
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.error(Throwable())
        }
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.error(Throwable())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.error(Throwable())
        }
        val viewModel = SpeciesDetailsViewModelImpl(
            speciesRemoteDataSource,
            planetsRemoteDataSource,
            peopleRemoteDataSource,
            filmsRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertTrue(actual!!)
    }
}