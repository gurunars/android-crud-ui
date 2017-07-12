package com.gurunars.item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.gurunars.shortcuts.fullSize

typealias EmptyViewBinder = (context: Context) -> View

internal fun defaultEmptyViewBinder(context: Context): View {
    return TextView(context).apply {
        fullSize()
        setText(R.string.empty)
        gravity = Gravity.CENTER
    }
}