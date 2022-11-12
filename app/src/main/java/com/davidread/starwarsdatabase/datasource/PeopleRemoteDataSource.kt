package com.davidread.starwarsdatabase.datasource

import com.davidread.starwarsdatabase.model.datasource.PersonResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines SWAPI endpoints to hit in order to fetch people data.
 */
interface PeopleRemoteDataSource {

    /**
     * Fetches a [PersonResponse] from SWAPI.
     */
    @GET(ENDPOINT_PEOPLE)
    fun getPeople(@Query(ParameterFormat.NAME) format: String = ParameterFormat.VALUE_JSON): Single<PersonResponse>

    companion object {
        private const val ENDPOINT_PEOPLE = "people"
        private object ParameterFormat {
            const val NAME = "format"
            const val VALUE_JSON = "json"
        }
    }
}