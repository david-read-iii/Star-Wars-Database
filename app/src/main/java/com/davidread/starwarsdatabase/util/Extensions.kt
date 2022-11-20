package com.davidread.starwarsdatabase.util

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