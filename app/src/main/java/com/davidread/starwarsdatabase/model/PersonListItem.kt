package com.davidread.starwarsdatabase.model

/**
 * Represents a list item that appears in the people list.
 */
sealed class PersonListItem {

    /**
     * Represents a list item for a person.
     *
     * @param id Unique identifier for the person from the API.
     * @param name Person's name.
     */
    class PersonItem(val id: Int, val name: String) : PersonListItem()

    /**
     * Represents a list item for a loading view.
     */
    object LoadingItem : PersonListItem()

    /**
     * Represents a list item for an error view.
     */
    object ErrorItem : PersonListItem()
}