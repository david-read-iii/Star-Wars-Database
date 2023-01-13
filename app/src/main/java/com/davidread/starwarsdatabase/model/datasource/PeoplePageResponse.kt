package com.davidread.starwarsdatabase.model.datasource

import com.google.gson.annotations.SerializedName

/**
 * Represents a people page response from SWAPI.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
data class PeoplePageResponse(
    @SerializedName("results") val results: List<PersonResponse>,
    @SerializedName("next") val next: String?
)