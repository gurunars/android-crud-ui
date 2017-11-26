package com.gurunars.item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.gurunars.databinding.IBox
import com.gurunars.databinding.android.asRow
import com.gurunars.databinding.onChange
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

/**
 * @param box box representing item's payload
 * @return a view bound to a box holding the item
 */
typealias ItemViewBinder<ItemType> = (field: IBox<ItemType>) -> View

fun <ItemType : Item> Context.defaultBindView(field: IBox<ItemType>) = TextView(this).apply {
    backgroundColor = Color.YELLOW
    textColor = Color.RED
    field.onChange { value ->
        text = with(value.toString()) {
            substring(0, minOf(42, length))
        }
        asRow()
    }
}
