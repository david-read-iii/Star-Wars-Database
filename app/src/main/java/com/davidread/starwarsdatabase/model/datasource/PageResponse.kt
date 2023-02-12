package com.davidread.starwarsdatabase.model.datasource

import com.google.gson.annotations.SerializedName

/**
 * Represents a page response of some resource from SWAPI.
 *
 * @param T The element type of the list objects of [results].
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
data class PageResponse<T : ResourceResponse>(
    @SerializedName("results") val results: List<T>,
    @SerializedName("next") val next: String?
)