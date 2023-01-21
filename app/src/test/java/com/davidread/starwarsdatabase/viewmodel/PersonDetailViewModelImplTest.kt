package com.davidread.starwarsdatabase.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.RxImmediateSchedulerRule
import com.davidread.starwarsdatabase.datasource.*
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import com.davidread.starwarsdatabase.model.view.DetailListItem
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
    fun `given that all datasources return success response, when viewmodel calls getPerson(), then viewmodel emits the expected detail list items in the UI list`() {
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.just(getSuccessfulPersonResponse())
        }
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.just(getSuccessfulPlanetResponse())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(1) } returns Single.just(getSuccessfulFilmResponse(1))
            every { getFilm(2) } returns Single.just(getSuccessfulFilmResponse(2))
            every { getFilm(3) } returns Single.just(getSuccessfulFilmResponse(3))
            every { getFilm(6) } returns Single.just(getSuccessfulFilmResponse(6))
        }
        val speciesRemoteDataSource = mockk<SpeciesRemoteDataSource> {
            every { getSingleSpecies(any()) } returns Single.just(getSuccessfulSpeciesResponse())
        }
        val vehiclesRemoteDataSource = mockk<VehiclesRemoteDataSource> {
            every { getVehicle(any()) } returns Single.just(getSuccessfulVehicleResponse())
        }
        val starshipsRemoteDataSource = mockk<StarshipsRemoteDataSource> {
            every { getStarship(10) } returns Single.just(getSuccessfulStarshipResponse(10))
            every { getStarship(22) } returns Single.just(getSuccessfulStarshipResponse(22))
        }
        val viewModel = PersonDetailViewModelImpl(
            peopleRemoteDataSource,
            planetsRemoteDataSource,
            speciesRemoteDataSource,
            filmsRemoteDataSource,
            starshipsRemoteDataSource,
            vehiclesRemoteDataSource
        )
        viewModel.getPerson(1)

        val expectedList = listOf(
            DetailListItem(R.string.name_detail_label, "Chewbacca"),
            DetailListItem(R.string.homeworld_detail_label, "Kashyyyk"),
            DetailListItem(R.string.birth_year_detail_label, "200BBY"),
            DetailListItem(R.string.species_detail_label, "Wookie"),
            DetailListItem(R.string.gender_detail_label, "male"),
            DetailListItem(R.string.height_detail_label, "228"),
            DetailListItem(R.string.mass_detail_label, "112"),
            DetailListItem(R.string.hair_color_detail_label, "brown"),
            DetailListItem(R.string.eye_color_detail_label, "blue"),
            DetailListItem(R.string.skin_color_detail_label, "unknown"),
            DetailListItem(
                R.string.films_detail_label,
                "A New Hope, The Empire Strikes Back, Return of the Jedi, Revenge of the Sith"
            ),
            DetailListItem(R.string.starships_detail_label, "Millennium Falcon, Imperial shuttle"),
            DetailListItem(R.string.vehicles_detail_label, "AT-ST")
        )
        val actualList = viewModel.personDetailListItemsLiveData.value
        Assert.assertEquals(expectedList, actualList)
    }

    @Test
    fun `given that all datasources return success response, when viewmodel calls getPerson(), then viewmodel does not emit an error status to the UI`() {
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.just(getSuccessfulPersonResponse())
        }
        val planetsRemoteDataSource = mockk<PlanetsRemoteDataSource> {
            every { getPlanet(any()) } returns Single.just(getSuccessfulPlanetResponse())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.just(getSuccessfulFilmResponse())
        }
        val speciesRemoteDataSource = mockk<SpeciesRemoteDataSource> {
            every { getSingleSpecies(any()) } returns Single.just(getSuccessfulSpeciesResponse())
        }
        val vehiclesRemoteDataSource = mockk<VehiclesRemoteDataSource> {
            every { getVehicle(any()) } returns Single.just(getSuccessfulVehicleResponse())
        }
        val starshipsRemoteDataSource = mockk<StarshipsRemoteDataSource> {
            every { getStarship(any()) } returns Single.just(getSuccessfulStarshipResponse())
        }
        val viewModel = PersonDetailViewModelImpl(
            peopleRemoteDataSource,
            planetsRemoteDataSource,
            speciesRemoteDataSource,
            filmsRemoteDataSource,
            starshipsRemoteDataSource,
            vehiclesRemoteDataSource
        )
        viewModel.getPerson(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertFalse(actual!!)
    }

    @Test
    fun `given all datasources return error response, when viewmodel calls getPerson(), then viewmodel emits an error status to the UI`() {
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
        val viewModel = PersonDetailViewModelImpl(
            peopleRemoteDataSource,
            planetsRemoteDataSource,
            speciesRemoteDataSource,
            filmsRemoteDataSource,
            starshipsRemoteDataSource,
            vehiclesRemoteDataSource
        )
        viewModel.getPerson(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertTrue(actual!!)
    }

    private fun getSuccessfulPersonResponse() = ResourceResponse.Person(
        "Chewbacca",
        "228",
        "112",
        "brown",
        "unknown",
        "blue",
        "200BBY",
        "male",
        "https://swapi.dev/api/planets/14/",
        listOf(
            "https://swapi.dev/api/films/1/",
            "https://swapi.dev/api/films/2/",
            "https://swapi.dev/api/films/3/",
            "https://swapi.dev/api/films/6/"
        ),
        listOf("https://swapi.dev/api/species/3/"),
        listOf("https://swapi.dev/api/vehicles/19/"),
        listOf(
            "https://swapi.dev/api/starships/10/",
            "https://swapi.dev/api/starships/22/"
        ),
        "https://swapi.dev/api/people/13/"
    )

    private fun getSuccessfulPlanetResponse() = mockk<ResourceResponse.Planet> {
        every { name } returns "Kashyyyk"
    }

    private fun getSuccessfulFilmResponse(id: Int? = null): ResourceResponse.Film {
        val titleString = when (id) {
            1 -> "A New Hope"
            2 -> "The Empire Strikes Back"
            3 -> "Return of the Jedi"
            6 -> "Revenge of the Sith"
            else -> "Some Film"
        }
        return mockk {
            every { title } returns titleString
        }
    }

    private fun getSuccessfulSpeciesResponse() = mockk<ResourceResponse.Species> {
        every { name } returns "Wookie"
    }

    private fun getSuccessfulVehicleResponse() = mockk<ResourceResponse.Vehicle> {
        every { name } returns "AT-ST"
    }

    private fun getSuccessfulStarshipResponse(id: Int? = null): ResourceResponse.Starship {
        val nameString = when (id) {
            10 -> "Millennium Falcon"
            22 -> "Imperial shuttle"
            else -> "Some Starship"
        }
        return mockk {
            every { name } returns nameString
        }
    }
}