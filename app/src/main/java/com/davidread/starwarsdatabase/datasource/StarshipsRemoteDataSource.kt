package com.davidread.starwarsdatabase.datasource

import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines HTTP operations that may be performed on the starships endpoint of SWAPI.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
interface StarshipsRemoteDataSource {

    /**
     * Fetches a [PageResponse] containing [ResourceResponse.Starship] results from SWAPI.
     *
     * @param page Which page of starships to fetch.
     */
    @GET(ENDPOINT_STARSHIPS)
    fun getStarships(@Query(PARAMETER_PAGE) @IntRange(from = 1) page: Int): Single<PageResponse<ResourceResponse.Starship>>

    /**
     * Fetches a single [ResourceResponse.Starship] from SWAPI.
     *
     * @param id Unique id of the starship to fetch.
     */
    @GET("${ENDPOINT_STARSHIPS}/{${PATH_ID}}")
    fun getStarship(@Path(PATH_ID) @IntRange(from = 1) id: Int): Single<ResourceResponse.Starship>

    companion object {
        private const val ENDPOINT_STARSHIPS = "starships"
        private const val PARAMETER_PAGE = "page"
        private const val PATH_ID = "id"
    }
}