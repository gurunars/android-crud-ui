package com.gurunars.item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.gurunars.databinding.android.fullSize

/**
 * View binder for the case when there are no item in the list.
 */
typealias EmptyViewBinder = () -> View

fun Context.defaultBindEmpty() = TextView(this).apply {
    id = R.id.noItemsLabel
    fullSize()
    text = getString(R.string.empty)
    gravity = Gravity.CENTER
}
