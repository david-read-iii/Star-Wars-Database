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
 * Unit tests that verify the correctness of [FilmDetailsViewModelImpl].
 */
class FilmDetailsViewModelImplTest : BaseViewModelImplTest() {

    @Test
    fun `given that all datasources return success response, when viewmodel calls getResourceDetails(), then viewmodel emits the expected detail list items in the UI list`() {
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulFilmResponse())
        }
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            for (id in 1..3) {
                every { getPerson(id) } returns Single.just(
                    BaseViewModelImplTestConstants.getSuccessfulPersonResponse(id)
                )
            }
        }
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            for (id in 1..3) {
                every { getPlanet(id) } returns Single.just(
                    BaseViewModelImplTestConstants.getSuccessfulPlanetResponse(id)
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
        val starshipsRemoteDataSource = mockk<StarshipsRemoteDataSource> {
            for (id in 1..3) {
                every { getStarship(id) } returns Single.just(
                    BaseViewModelImplTestConstants.getSuccessfulStarshipResponse(id)
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
        val viewModel = FilmDetailsViewModelImpl(
            filmsRemoteDataSource,
            peopleRemoteDataSource,
            planetsRemoteDataSource,
            speciesRemoteDataSource,
            starshipsRemoteDataSource,
            vehiclesRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val expectedList = listOf(
            ResourceDetailListItem(R.string.title_detail_label, "Film 1"),
            ResourceDetailListItem(R.string.episode_detail_label, "1"),
            ResourceDetailListItem(R.string.release_date_detail_label, "1977-05-25"),
            ResourceDetailListItem(R.string.director_detail_label, "George Lucas"),
            ResourceDetailListItem(R.string.producer_detail_label, "Gary Kurtz, Rick McCallum"),
            ResourceDetailListItem(
                R.string.opening_crawl_detail_label,
                "Very long opening crawl. Tells a lot of story. Always three paragraphs."
            ),
            ResourceDetailListItem(
                R.string.characters_detail_label,
                "Person 1, Person 2, Person 3"
            ),
            ResourceDetailListItem(R.string.planets_detail_label, "Planet 1, Planet 2, Planet 3"),
            ResourceDetailListItem(
                R.string.species_detail_label,
                "Species 1, Species 2, Species 3"
            ),
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
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulFilmResponse())
        }
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPersonResponse())
        }
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPlanetResponse())
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
        val viewModel = FilmDetailsViewModelImpl(
            filmsRemoteDataSource,
            peopleRemoteDataSource,
            planetsRemoteDataSource,
            speciesRemoteDataSource,
            starshipsRemoteDataSource,
            vehiclesRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertFalse(actual!!)
    }

    @Test
    fun `given all datasources return error response, when viewmodel calls getResourceDetails(), then viewmodel emits an error status to the UI`() {
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.error(Throwable())
        }
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.error(Throwable())
        }
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.error(Throwable())
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
        val viewModel = FilmDetailsViewModelImpl(
            filmsRemoteDataSource,
            peopleRemoteDataSource,
            planetsRemoteDataSource,
            speciesRemoteDataSource,
            starshipsRemoteDataSource,
            vehiclesRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertTrue(actual!!)
    }
}