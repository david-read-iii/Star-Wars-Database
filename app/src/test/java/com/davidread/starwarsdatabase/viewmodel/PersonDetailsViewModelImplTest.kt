package com.davidread.starwarsdatabase.viewmodel

import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.datasource.*
import com.davidread.starwarsdatabase.model.view.ResourceDetailListItem
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Test

/**
 * Unit tests that verify the correctness of [PersonDetailsViewModelImpl].
 */
class PersonDetailsViewModelImplTest : BaseViewModelImplTest() {

    @Test
    fun `given that all datasources return success response, when viewmodel calls getResourceDetails(), then viewmodel emits the expected detail list items in the UI list`() {
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPersonResponse())
        }
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPlanetResponse())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            for (id in 1..3) {
                every { getFilm(id) } returns Single.just(
                    BaseViewModelImplTestConstants.getSuccessfulFilmResponse(id)
                )
            }
        }
        val speciesRemoteDataSource = mockk<SpeciesRemoteDataSource> {
            for (id in 1..3) {
                every { getSingleSpecies(id) } returns Single.just(
                    BaseViewModelImplTestConstants.getSuccessfulSpeciesResponse(id)
                )
            }
        }
        val vehiclesRemoteDataSource = mockk<VehiclesRemoteDataSource> {
            for (id in 1..3) {
                every { getVehicle(id) } returns Single.just(
                    BaseViewModelImplTestConstants.getSuccessfulVehicleResponse(id)
                )
            }
        }
        val starshipsRemoteDataSource = mockk<StarshipsRemoteDataSource> {
            for (id in 1..3) {
                every { getStarship(id) } returns Single.just(
                    BaseViewModelImplTestConstants.getSuccessfulStarshipResponse(id)
                )
            }
        }
        val viewModel = PersonDetailsViewModelImpl(
            peopleRemoteDataSource,
            planetsRemoteDataSource,
            speciesRemoteDataSource,
            filmsRemoteDataSource,
            starshipsRemoteDataSource,
            vehiclesRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val expectedList = listOf(
            ResourceDetailListItem(R.string.name_detail_label, "Person 1"),
            ResourceDetailListItem(R.string.homeworld_detail_label, "Planet 1"),
            ResourceDetailListItem(R.string.birth_year_detail_label, "200BBY"),
            ResourceDetailListItem(
                R.string.species_detail_label,
                "Species 1, Species 2, Species 3"
            ),
            ResourceDetailListItem(R.string.gender_detail_label, "male"),
            ResourceDetailListItem(R.string.height_detail_label, "200"),
            ResourceDetailListItem(R.string.mass_detail_label, "100"),
            ResourceDetailListItem(R.string.hair_color_detail_label, "brown"),
            ResourceDetailListItem(R.string.eye_color_detail_label, "blue"),
            ResourceDetailListItem(R.string.skin_color_detail_label, "unknown"),
            ResourceDetailListItem(R.string.films_detail_label, "Film 1, Film 2, Film 3"),
            ResourceDetailListItem(
                R.string.starships_detail_label,
                "Starship 1, Starship 2, Starship 3"
            ),
            ResourceDetailListItem(
                R.string.vehicles_detail_label,
                "Vehicle 1, Vehicle 2, Vehicle 3"
            )
        )
        val actualList = viewModel.resourceDetailsLiveData.value
        Assert.assertEquals(expectedList, actualList)
    }

    @Test
    fun `given that all datasources return success response, when viewmodel calls getResourceDetails(), then viewmodel does not emit an error status to the UI`() {
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPersonResponse())
        }
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPlanetResponse())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulFilmResponse())
        }
        val speciesRemoteDataSource = mockk<SpeciesRemoteDataSource> {
            every { getSingleSpecies(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulSpeciesResponse())
        }
        val vehiclesRemoteDataSource = mockk<VehiclesRemoteDataSource> {
            every { getVehicle(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulVehicleResponse())
        }
        val starshipsRemoteDataSource = mockk<StarshipsRemoteDataSource> {
            every { getStarship(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulStarshipResponse())
        }
        val viewModel = PersonDetailsViewModelImpl(
            peopleRemoteDataSource,
            planetsRemoteDataSource,
            speciesRemoteDataSource,
            filmsRemoteDataSource,
            starshipsRemoteDataSource,
            vehiclesRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertFalse(actual!!)
    }

    @Test
    fun `given all datasources return error response, when viewmodel calls getResourceDetails(), then viewmodel emits an error status to the UI`() {
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.error(Throwable())
        }
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.error(Throwable())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.error(Throwable())
        }
        val speciesRemoteDataSource = mockk<SpeciesRemoteDataSource> {
            every { getSingleSpecies(any()) } returns Single.error(Throwable())
        }
        val vehiclesRemoteDataSource = mockk<VehiclesRemoteDataSource> {
            every { getVehicle(any()) } returns Single.error(Throwable())
        }
        val starshipsRemoteDataSource = mockk<StarshipsRemoteDataSource> {
            every { getStarship(any()) } returns Single.error(Throwable())
        }
        val viewModel = PersonDetailsViewModelImpl(
            peopleRemoteDataSource,
            planetsRemoteDataSource,
            speciesRemoteDataSource,
            filmsRemoteDataSource,
            starshipsRemoteDataSource,
            vehiclesRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertTrue(actual!!)
    }
}