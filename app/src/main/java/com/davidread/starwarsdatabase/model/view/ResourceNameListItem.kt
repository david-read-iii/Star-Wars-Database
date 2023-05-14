package com.davidread.starwarsdatabase.model.view

import androidx.annotation.AttrRes

/**
 * Represents a list item that appears in any resource name list.
 */
sealed class ResourceNameListItem {

    /**
     * Creates a deep copy of this object.
     */
    abstract fun copySealedObject(): ResourceNameListItem

    /**
     * Represents a list item for a resource name.
     */
    data class ResourceName(
        val id: Int,
        val name: String,
        @AttrRes var backgroundAttrResId: Int
    ) : ResourceNameListItem() {
        override fun copySealedObject(): ResourceName = copy()
    }

    /**
     * Represents a list item for a loading view.
     */
    object Loading : ResourceNameListItem() {
        override fun copySealedObject(): Loading = Loading
    }

    /**
     * Represents a list item for an error view.
     */
    object Error : ResourceNameListItem() {
        override fun copySealedObject(): Error = Error
    }
}