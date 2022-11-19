package com.davidread.starwarsdatabase.model.datasource

import com.google.gson.annotations.SerializedName

/**
 * Represents a person response from SWAPI.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
data class PersonResponse(
    @SerializedName("results") val results: List<Person>,
    @SerializedName("next") val next: String?
)