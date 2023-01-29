package com.davidread.starwarsdatabase.util

import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import java.util.regex.Pattern

/**
 * Takes a [String] that matches the URL `https://swapi.dev/api/{endpoint}/{id}/` and returns the
 * `{id}` field as an [Int].
 *
 * @throws IllegalArgumentException Thrown when the [String] does not match the URL regular
 * expression.
 */
fun String.extractIDFromURL(): Int {
    val regex = "https://swapi\\.dev/api/[a-z]+/(\\d+)/"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    return if (matcher.matches()) {
        matcher.group(1)!!.toInt()
    } else {
        throw IllegalArgumentException("$this must match the regular expression $regex")
    }
}

/**
 * Takes a [List] of [String], where each matches the URL `https://swapi.dev/api/{endpoint}/{id}/`
 * and each `{id}` field as an [Int] in a [List].
 *
 * @throws IllegalArgumentException Thrown when at least one [String] does not match the URL regular
 * expression.
 */
fun List<String>.extractIDsFromURLs(): List<Int> {
    val regex = "https://swapi\\.dev/api/[a-z]+/(\\d+)/"
    val pattern = Pattern.compile(regex)
    val idList = mutableListOf<Int>()
    this.forEach { url ->
        val matcher = pattern.matcher(url)
        if (matcher.matches()) {
            idList.add(matcher.group(1)!!.toInt())
        } else {
            throw IllegalArgumentException("$url must match the regular expression $regex")
        }
    }
    return idList
}

/**
 * Takes a [String] that matches the URL `https://swapi.dev/api/{endpoint}/?page={pageInt}` and
 * returns the `{pageInt}` field as an [Int].
 *
 * @throws IllegalArgumentException Thrown when the [String] does not match the URL regular
 * expression.
 */
fun String.extractPageFromURL(): Int {
    val regex = "https://swapi\\.dev/api/[a-z]+/\\?page=(\\d+)"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    return if (matcher.matches()) {
        matcher.group(1)!!.toInt()
    } else {
        throw IllegalArgumentException("$this must match the regular expression $regex")
    }
}

/**
 * Takes a [List] of [ResourceResponse] and returns a [String] comma-delimited list of each object's
 * `name` or `title` field.
 */
fun List<ResourceResponse>.extractNames(): String {
    val nameList = mutableListOf<String>()
    this.forEach { resourceResponse ->
        val name = when (resourceResponse) {
            is ResourceResponse.Person -> resourceResponse.name
            is ResourceResponse.Film -> resourceResponse.title
            is ResourceResponse.Starship -> resourceResponse.name
            is ResourceResponse.Vehicle -> resourceResponse.name
            is ResourceResponse.Species -> resourceResponse.name
            is ResourceResponse.Planet -> resourceResponse.name
        }
        nameList.add(name)
    }
    return nameList.toString().removeSurrounding("[", "]")
}