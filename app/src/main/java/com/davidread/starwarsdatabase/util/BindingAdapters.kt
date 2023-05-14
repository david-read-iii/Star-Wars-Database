package com.davidread.starwarsdatabase.util

import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

/**
 * Binds an attribute resource onto the background property of a [View].
 *
 * @param view [View] being bound onto.
 * @param backgroundAttrResId Attribute resource id being applied onto the [View].
 */
@BindingAdapter("backgroundAttr")
fun setBackgroundAttr(view: View, @AttrRes backgroundAttrResId: Int) {
    val typedValue = TypedValue()
    view.context.theme.resolveAttribute(backgroundAttrResId, typedValue, true)
    view.background = ContextCompat.getDrawable(view.context, typedValue.resourceId)
}