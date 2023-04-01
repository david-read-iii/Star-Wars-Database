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
 * Unit tests that verify the correctness of [VehicleDetailsViewModelImpl].
 */
class VehicleDetailsViewModelImplTest : BaseViewModelImplTest() {

    @Test
    fun `given that all datasources return success response, when viewmodel calls getResourceDetails(), then viewmodel emits the expected detail list items in the UI list`() {
        val vehiclesRemoteDataSource = mockk<VehiclesRemoteDataSource> {
            every { getVehicle(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulVehicleResponse())
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
        val viewModel = VehicleDetailsViewModelImpl(
            vehiclesRemoteDataSource,
            peopleRemoteDataSource,
            filmsRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val expectedList = listOf(
            ResourceDetailListItem(R.string.name_detail_label, "Vehicle 1"),
            ResourceDetailListItem(R.string.model_detail_label, "Digger Crawler"),
            ResourceDetailListItem(
                R.string.manufacturer_detail_label,
                "Corellia Mining Corporation"
            ),
            ResourceDetailListItem(R.string.cost_detail_label, "150000"),
            ResourceDetailListItem(R.string.length_detail_label, "36.8"),
            ResourceDetailListItem(R.string.max_atmosphering_speed_detail_label, "30"),
            ResourceDetailListItem(R.string.crew_detail_label, "46"),
            ResourceDetailListItem(R.string.passengers_detail_label, "30"),
            ResourceDetailListItem(R.string.cargo_capacity_detail_label, "50000"),
            ResourceDetailListItem(R.string.consumables_detail_label, "2 months"),
            ResourceDetailListItem(R.string.vehicle_class_detail_label, "wheeled"),
            ResourceDetailListItem(R.string.pilots_detail_label, "Person 1, Person 2, Person 3"),
            ResourceDetailListItem(R.string.films_detail_label, "Film 1, Film 2, Film 3")
        )
        val actualList = viewModel.resourceDetailsLiveData.value
        Assert.assertEquals(expectedList, actualList)
    }

    @Test
    fun `given that all datasources return success response, when viewmodel calls getResourceDetails(), then viewmodel does not emit an error status to the UI`() {
        val vehiclesRemoteDataSource = mockk<VehiclesRemoteDataSource> {
            every { getVehicle(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulVehicleResponse())
        }
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulPersonResponse())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.just(BaseViewModelImplTestConstants.getSuccessfulFilmResponse())
        }
        val viewModel = VehicleDetailsViewModelImpl(
            vehiclesRemoteDataSource,
            peopleRemoteDataSource,
            filmsRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertFalse(actual!!)
    }

    @Test
    fun `given all datasources return error response, when viewmodel calls getResourceDetails(), then viewmodel emits an error status to the UI`() {
        val vehiclesRemoteDataSource = mockk<VehiclesRemoteDataSource> {
            every { getVehicle(any()) } returns Single.error(Throwable())
        }
        val peopleRemoteDataSource = mockk<PeopleRemoteDataSource> {
            every { getPerson(any()) } returns Single.error(Throwable())
        }
        val filmsRemoteDataSource = mockk<FilmsRemoteDataSource> {
            every { getFilm(any()) } returns Single.error(Throwable())
        }
        val viewModel = VehicleDetailsViewModelImpl(
            vehiclesRemoteDataSource,
            peopleRemoteDataSource,
            filmsRemoteDataSource
        )
        viewModel.getResourceDetails(1)

        val actual = viewModel.showErrorLiveData.value
        Assert.assertTrue(actual!!)
    }
}