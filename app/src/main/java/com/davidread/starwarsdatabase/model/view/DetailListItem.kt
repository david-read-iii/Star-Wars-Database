package com.davidread.starwarsdatabase.model.view

import androidx.annotation.StringRes

/**
 * Represents a list item that appears in any detail list.
 */
data class DetailListItem(@StringRes val label: Int, val value: String)