package com.davidread.starwarsdatabase.datasource

import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.model.datasource.PersonResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines SWAPI endpoints to hit in order to fetch people data.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
interface PeopleRemoteDataSource {

    /**
     * Fetches a [PersonResponse] from SWAPI.
     *
     * @param page Which page of people to fetch.
     */
    @GET(ENDPOINT_PEOPLE)
    fun getPeople(@Query(PARAMETER_NAME_PAGE) @IntRange(from = 1) page: Int): Single<PersonResponse>

    companion object {
        private const val ENDPOINT_PEOPLE = "people"
        private const val PARAMETER_NAME_PAGE = "page"
    }
}