package com.davidread.starwarsdatabase.datasource

import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines HTTP operations that may be performed on the films endpoint of SWAPI.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
interface FilmsRemoteDataSource {

    /**
     * Fetches a [PageResponse] containing [ResourceResponse.Film] results from SWAPI.
     *
     * @param page Which page of films to fetch.
     */
    @GET(ENDPOINT_FILMS)
    fun getFilms(@Query(PARAMETER_PAGE) @IntRange(from = 1) page: Int): Single<PageResponse<ResourceResponse.Film>>

    /**
     * Fetches a single [ResourceResponse.Film] from SWAPI.
     *
     * @param id Unique id of the film to fetch.
     */
    @GET("${ENDPOINT_FILMS}/{${PATH_ID}}")
    fun getFilm(@Path(PATH_ID) @IntRange(from = 1) id: Int): Single<ResourceResponse.Film>

    companion object {
        private const val ENDPOINT_FILMS = "films"
        private const val PARAMETER_PAGE = "page"
        private const val PATH_ID = "id"
    }
}