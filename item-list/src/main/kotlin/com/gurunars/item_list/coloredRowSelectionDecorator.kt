package com.gurunars.item_list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.support.annotation.ColorInt
import android.view.View
import com.gurunars.box.Box
import com.gurunars.box.IRoBox
import com.gurunars.box.ui.asRow

/**
 * A decorator to add row coloring behavior to the list view items.
 *
 * @param field selectable box to be bound with renderer
 * @param render original view binder unaware of selection flag
 * @param selectionColor color integer applied when the row is selected
 */
fun <ItemType : Item> coloredRowSelectionDecorator(
    field: IRoBox<SelectableItem<ItemType>>,
    @ColorInt selectionColor: Int = Color.RED,
    render: (field: IRoBox<ItemType>) -> View
): View {
    val newField = Box(field.get().item)
    return render(newField).apply {
        asRow()
        val originalBackground: Drawable
        if (background is LayerDrawable) {
            val current = background as LayerDrawable
            originalBackground = LayerDrawable(
                (0 until current.numberOfLayers).map { current.getDrawable(it) }.toTypedArray()
            )
        } else {
            originalBackground = background.mutate()
        }
        field.onChange { item ->
            setTag(R.id.isSelected, item.isSelected)
            background = if (item.isSelected) LayerDrawable(
                listOf(ColorDrawable(selectionColor), background).toTypedArray()) else originalBackground
            newField.set(item.item)
        }
    }
}