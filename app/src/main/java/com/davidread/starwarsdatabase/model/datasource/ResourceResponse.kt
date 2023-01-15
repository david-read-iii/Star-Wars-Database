package com.davidread.starwarsdatabase.model.datasource

import com.google.gson.annotations.SerializedName

/**
 * Represents a resource response from SWAPI.
 *
 * @see <a href="https://swapi.dev/documentation">SWAPI documentation</a>
 */
sealed class ResourceResponse {

    /**
     * Represents a person resource response from SWAPI.
     */
    data class Person(
        @SerializedName("name") val name: String,
        @SerializedName("height") val height: String,
        @SerializedName("mass") val mass: String,
        @SerializedName("hair_color") val hairColor: String,
        @SerializedName("skin_color") val skinColor: String,
        @SerializedName("eye_color") val eyeColor: String,
        @SerializedName("birth_year") val birthYear: String,
        @SerializedName("gender") val gender: String,
        @SerializedName("homeworld") val homeworldURL: String,
        @SerializedName("films") val filmsURLs: List<String>,
        @SerializedName("species") val speciesURLs: List<String>,
        @SerializedName("vehicles") val vehiclesURLs: List<String>,
        @SerializedName("starships") val starshipsURLs: List<String>,
        @SerializedName("url") val url: String
    ) : ResourceResponse()

    /**
     * Represents a film resource response from SWAPI.
     */
    data class Film(
        @SerializedName("title") val title: String,
        @SerializedName("episode_id") val episodeID: String,
        @SerializedName("opening_crawl") val openingCrawl: String,
        @SerializedName("director") val director: String,
        @SerializedName("producer") val producer: String,
        @SerializedName("release_date") val releaseDate: String,
        @SerializedName("characters") val characterURLs: List<String>,
        @SerializedName("planets") val planetURLs: List<String>,
        @SerializedName("starships") val starshipURLs: List<String>,
        @SerializedName("vehicles") val vehicleURLs: List<String>,
        @SerializedName("species") val speciesURLs: List<String>,
        @SerializedName("url") val url: String
    ) : ResourceResponse()

    /**
     * Represents a starship resource response from SWAPI.
     */
    data class Starship(
        @SerializedName("name") val name: String,
        @SerializedName("model") val model: String,
        @SerializedName("manufacturer") val manufacturer: String,
        @SerializedName("cost_in_credits") val costInCredits: String,
        @SerializedName("length") val length: String,
        @SerializedName("max_atmosphering_speed") val maxAtmospheringSpeed: String,
        @SerializedName("crew") val crew: String,
        @SerializedName("passengers") val passengers: String,
        @SerializedName("cargo_capacity") val cargoCapacity: String,
        @SerializedName("consumables") val consumables: String,
        @SerializedName("hyperdrive_rating") val hyperdriveRating: String,
        @SerializedName("MGLT") val mglt: String,
        @SerializedName("starship_class") val starshipClass: String,
        @SerializedName("pilots") val pilotURLs: List<String>,
        @SerializedName("films") val filmURLs: List<String>,
        @SerializedName("url") val url: String
    ) : ResourceResponse()

    /**
     * Represents a vehicle resource response from SWAPI.
     */
    data class Vehicle(
        @SerializedName("name") val name: String,
        @SerializedName("model") val model: String,
        @SerializedName("manufacturer") val manufacturer: String,
        @SerializedName("cost_in_credits") val costInCredits: String,
        @SerializedName("length") val length: String,
        @SerializedName("max_atmosphering_speed") val maxAtmospheringSpeed: String,
        @SerializedName("crew") val crew: String,
        @SerializedName("passengers") val passengers: String,
        @SerializedName("cargo_capacity") val cargoCapacity: String,
        @SerializedName("consumables") val consumables: String,
        @SerializedName("vehicle_class") val vehicleClass: String,
        @SerializedName("pilots") val pilotURLs: List<String>,
        @SerializedName("films") val filmURLs: List<String>,
        @SerializedName("url") val url: String
    ) : ResourceResponse()

    /**
     * Represents a species resource response from SWAPI.
     */
    data class Species(
        @SerializedName("name") val name: String,
        @SerializedName("classification") val classification: String,
        @SerializedName("designation") val designation: String,
        @SerializedName("average_height") val averageHeight: String,
        @SerializedName("skin_colors") val skinColors: String,
        @SerializedName("hair_colors") val hairColors: String,
        @SerializedName("eye_colors") val eyeColors: String,
        @SerializedName("average_lifespan") val averageLifespan: String,
        @SerializedName("homeworld") val homeworldURL: String,
        @SerializedName("language") val language: String,
        @SerializedName("people") val peopleURLs: List<String>,
        @SerializedName("films") val filmURLs: List<String>,
        @SerializedName("url") val url: String
    ) : ResourceResponse()

    /**
     * Represents a planet resource response from SWAPI.
     */
    data class Planet(
        @SerializedName("name") val name: String,
        @SerializedName("rotation_period") val rotationPeriod: String,
        @SerializedName("orbital_period") val orbitalPeriod: String,
        @SerializedName("diameter") val diameter: String,
        @SerializedName("climate") val climate: String,
        @SerializedName("gravity") val gravity: String,
        @SerializedName("terrain") val terrain: String,
        @SerializedName("surface_water") val surfaceWater: String,
        @SerializedName("population") val population: String,
        @SerializedName("residents") val residentURLs: List<String>,
        @SerializedName("films") val filmURLs: List<String>,
        @SerializedName("url") val url: String
    ) : ResourceResponse()
}