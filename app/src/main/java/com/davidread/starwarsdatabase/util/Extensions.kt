package com.davidread.starwarsdatabase.util

import com.davidread.starwarsdatabase.model.datasource.ResourceResponse
import com.davidread.starwarsdatabase.model.view.ResourceDetailListItem
import java.util.regex.Pattern

/**
 * Takes a [String] that matches the URL `https://swapi.py4e.com/api/{endpoint}/{id}/` and returns the
 * `{id}` field as an [Int].
 *
 * @throws IllegalArgumentException Thrown when the [String] does not match the URL regular
 * expression.
 */
fun String.extractIDFromURL(): Int {
    val regex = "https://swapi\\.py4e\\.com/api/[a-z]+/(\\d+)/"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    return if (matcher.matches()) {
        matcher.group(1)!!.toInt()
    } else {
        throw IllegalArgumentException("$this must match the regular expression $regex")
    }
}

/**
 * Takes a [List] of [String], where each matches the URL `https://swapi.py4e.com/api/{endpoint}/{id}/`
 * and each `{id}` field as an [Int] in a [List].
 *
 * @throws IllegalArgumentException Thrown when at least one [String] does not match the URL regular
 * expression.
 */
fun List<String>.extractIDsFromURLs(): List<Int> {
    val regex = "https://swapi\\.py4e\\.com/api/[a-z]+/(\\d+)/"
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
 * Takes a [String] that matches the URL `https://swapi.py4e.com/api/{endpoint}/?page={pageInt}` and
 * returns the `{pageInt}` field as an [Int].
 *
 * @throws IllegalArgumentException Thrown when the [String] does not match the URL regular
 * expression.
 */
fun String.extractPageFromURL(): Int {
    val regex = "https://swapi\\.py4e\\.com/api/[a-z]+/\\?page=(\\d+)"
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

/**
 * Returns a [List] of [ResourceDetailListItem] given a [Map] containing the necessary data to build
 * the list. Each [Int] represents a string resource id pointing to a string to be used as a label
 * for a list item. Each [String] represents the value to be used in the list item. If a value is
 * empty, the list item is excluded from the return list.
 */
fun Map<Int, String>.toResourceDetailListItems(): List<ResourceDetailListItem> =
    this.map { (label, value) ->
        if (value.isNotEmpty()) {
            ResourceDetailListItem(label, value)
        } else {
            null
        }
    }.filterNotNull()