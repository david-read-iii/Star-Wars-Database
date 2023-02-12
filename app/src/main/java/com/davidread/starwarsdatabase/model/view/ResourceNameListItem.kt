package com.davidread.starwarsdatabase.model.view

/**
 * Represents a list item that appears in any resource name list.
 */
sealed class ResourceNameListItem {

    /**
     * Represents a list item for a resource name.
     */
    data class ResourceName(val id: Int, val name: String) : ResourceNameListItem()

    /**
     * Represents a list item for a loading view.
     */
    object Loading : ResourceNameListItem()

    /**
     * Represents a list item for an error view.
     */
    object Error : ResourceNameListItem()
}