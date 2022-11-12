package com.davidread.starwarsdatabase.model.datasource

import com.google.gson.annotations.SerializedName

/**
 * Represents a person response from SWAPI.
 */
data class PersonResponse(@SerializedName("results") val results: List<Person>)