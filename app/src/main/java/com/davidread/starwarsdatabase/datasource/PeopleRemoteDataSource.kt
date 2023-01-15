package com.davidread.starwarsdatabase.datasource

import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines SWAPI endpoints to hit in order to fetch people data.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
interface PeopleRemoteDataSource {

    /**
     * Fetches a [PageResponse] of [ResourceResponse.Person]s from SWAPI.
     *
     * @param page Which page of people to fetch.
     */
    @GET(ENDPOINT_PEOPLE)
    fun getPeople(@Query(PARAMETER_NAME_PAGE) @IntRange(from = 1) page: Int): Single<PageResponse<ResourceResponse.Person>>

    /**
     * Fetches a [ResourceResponse.Person] from SWAPI.
     *
     * @param id Unique id of the person to fetch.
     */
    @GET("$ENDPOINT_PEOPLE/{$PATH_NAME_ID}")
    fun getPerson(@Path(PATH_NAME_ID) @IntRange(from = 1) id: Int): Single<ResourceResponse.Person>

    companion object {
        private const val ENDPOINT_PEOPLE = "people"
        private const val PARAMETER_NAME_PAGE = "page"
        private const val PATH_NAME_ID = "id"
    }
}