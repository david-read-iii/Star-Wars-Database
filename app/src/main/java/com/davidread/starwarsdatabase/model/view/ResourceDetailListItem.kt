package com.davidread.starwarsdatabase.model.view

import androidx.annotation.StringRes

/**
 * Represents a list item that appears in any resource detail list.
 */
data class ResourceDetailListItem(@StringRes val label: Int, val value: String)