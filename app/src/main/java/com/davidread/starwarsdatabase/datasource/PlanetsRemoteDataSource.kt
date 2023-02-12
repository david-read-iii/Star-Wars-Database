package com.davidread.starwarsdatabase.datasource

import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines HTTP operations that may be performed on the planets endpoint of SWAPI.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
interface PlanetsRemoteDataSource {

    /**
     * Fetches a [PageResponse] containing [ResourceResponse.Planet] results from SWAPI.
     *
     * @param page Which page of planets to fetch.
     */
    @GET(RemoteDataSourceConstants.ENDPOINT_PLANETS)
    fun getPlanets(@Query(RemoteDataSourceConstants.PARAMETER_PAGE) @IntRange(from = 1) page: Int): Single<PageResponse<ResourceResponse.Planet>>

    /**
     * Fetches a single [ResourceResponse.Planet] from SWAPI.
     *
     * @param id Unique id of the planet to fetch.
     */
    @GET("${RemoteDataSourceConstants.ENDPOINT_PLANETS}/{${RemoteDataSourceConstants.PATH_ID}}")
    fun getPlanet(@Path(RemoteDataSourceConstants.PATH_ID) @IntRange(from = 1) id: Int): Single<ResourceResponse.Planet>
}