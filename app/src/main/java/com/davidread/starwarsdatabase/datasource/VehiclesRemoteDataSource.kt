package com.davidread.starwarsdatabase.datasource

import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines HTTP operations that may be performed on the vehicles endpoint of SWAPI.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
interface VehiclesRemoteDataSource {

    /**
     * Fetches a [PageResponse] containing [ResourceResponse.Vehicle] results from SWAPI.
     *
     * @param page Which page of vehicles to fetch.
     */
    @GET(RemoteDataSourceConstants.ENDPOINT_VEHICLES)
    fun getVehicles(@Query(RemoteDataSourceConstants.PARAMETER_PAGE) @IntRange(from = 1) page: Int): Single<PageResponse<ResourceResponse.Vehicle>>

    /**
     * Fetches a single [ResourceResponse.Vehicle] from SWAPI.
     *
     * @param id Unique id of the vehicle to fetch.
     */
    @GET("${RemoteDataSourceConstants.ENDPOINT_VEHICLES}/{${RemoteDataSourceConstants.PATH_ID}}")
    fun getVehicle(@Path(RemoteDataSourceConstants.PATH_ID) @IntRange(from = 1) id: Int): Single<ResourceResponse.Vehicle>
}