package com.davidread.starwarsdatabase.model.datasource

import com.google.gson.annotations.SerializedName

/**
 * Represents a person from SWAPI.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
data class Person(
    @SerializedName("name") val name: String,
    @SerializedName("birth_year") val birthYear: String,
    @SerializedName("eye_color") val eyeColor: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("hair_color") val hairColor: String,
    @SerializedName("height") val height: String,
    @SerializedName("mass") val mass: String,
    @SerializedName("skin_color") val skinColor: String,
    @SerializedName("homeworld") val homeworldURL: String,
    @SerializedName("films") val filmsURLs: List<String>,
    @SerializedName("species") val speciesURLs: List<String>,
    @SerializedName("starships") val starshipsURLs: List<String>,
    @SerializedName("vehicles") val vehiclesURLs: List<String>,
    @SerializedName("url") val url: String,
)