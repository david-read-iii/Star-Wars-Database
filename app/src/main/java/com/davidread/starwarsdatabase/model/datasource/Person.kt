package com.davidread.starwarsdatabase.model.datasource

import com.google.gson.annotations.SerializedName

/**
 * Represents a person from SWAPI.
 */
data class Person(@SerializedName("name") val name: String, @SerializedName("url") val url: String)