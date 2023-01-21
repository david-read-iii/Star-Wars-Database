package com.davidread.starwarsdatabase.datasource

import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines HTTP operations that may be performed on the people endpoint of SWAPI.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
interface PeopleRemoteDataSource {

    /**
     * Fetches a [PageResponse] containing [ResourceResponse.Person] results from SWAPI.
     *
     * @param page Which page of people to fetch.
     */
    @GET(ENDPOINT_PEOPLE)
    fun getPeople(@Query(PARAMETER_PAGE) @IntRange(from = 1) page: Int): Single<PageResponse<ResourceResponse.Person>>

    /**
     * Fetches a single [ResourceResponse.Person] from SWAPI.
     *
     * @param id Unique id of the person to fetch.
     */
    @GET("$ENDPOINT_PEOPLE/{$PATH_ID}")
    fun getPerson(@Path(PATH_ID) @IntRange(from = 1) id: Int): Single<ResourceResponse.Person>

    companion object {
        private const val ENDPOINT_PEOPLE = "people"
        private const val PARAMETER_PAGE = "page"
        private const val PATH_ID = "id"
    }
}