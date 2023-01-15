package com.davidread.starwarsdatabase.datasource

import androidx.annotation.IntRange
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines HTTP operations that may be performed on the species endpoint of SWAPI.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
interface SpeciesRemoteDataSource {

    /**
     * Fetches a [PageResponse] containing [ResourceResponse.Species] results from SWAPI.
     *
     * @param page Which page of species to fetch.
     */
    @GET(ENDPOINT_SPECIES)
    fun getSpecies(@Query(PARAMETER_PAGE) @IntRange(from = 1) page: Int): Single<PageResponse<ResourceResponse.Species>>

    /**
     * Fetches a single [ResourceResponse.Species] from SWAPI.
     *
     * @param id Unique id of the species to fetch.
     */
    @GET("${ENDPOINT_SPECIES}/{${PATH_ID}}")
    fun getSingleSpecies(@Path(PATH_ID) @IntRange(from = 1) id: Int): Single<ResourceResponse.Species>

    companion object {
        private const val ENDPOINT_SPECIES = "species"
        private const val PARAMETER_PAGE = "page"
        private const val PATH_ID = "id"
    }
}