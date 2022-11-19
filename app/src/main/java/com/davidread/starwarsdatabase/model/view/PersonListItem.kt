package com.davidread.starwarsdatabase.model.view

/**
 * Represents a list item that appears in the people list.
 */
sealed class PersonListItem {

    /**
     * Represents a list item for a person.
     *
     * @property id Unique identifier for the person from the API.
     * @property name Person's name.
     */
    data class PersonItem(val id: Int, val name: String) : PersonListItem()

    /**
     * Represents a list item for a loading view.
     */
    object LoadingItem : PersonListItem()

    /**
     * Represents a list item for an error view.
     */
    object ErrorItem : PersonListItem()
}