package com.davidread.starwarsdatabase.model.view

/**
 * Represents a list item that appears in the people list.
 */
sealed class PersonListItem {

    /**
     * Represents a list item for a person.
     */
    data class PersonItem(
        val name: String,
        val birthYear: String,
        val eyeColor: String,
        val gender: String,
        val hairColor: String,
        val height: String,
        val mass: String,
        val skinColor: String,
        val homeworld: String,
        val films: List<String>,
        val species: List<String>,
        val starships: List<String>,
        val vehicles: List<String>,
        val id: Int
    ) : PersonListItem()

    /**
     * Represents a list item for a loading view.
     */
    object LoadingItem : PersonListItem()

    /**
     * Represents a list item for an error view.
     */
    object ErrorItem : PersonListItem()
}