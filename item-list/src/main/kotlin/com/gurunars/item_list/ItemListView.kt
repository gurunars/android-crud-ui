package com.gurunars.item_list

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.Widget
import com.gurunars.shortcuts.fullSize
import com.gurunars.shortcuts.setAsOne
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dip
import org.objenesis.strategy.StdInstantiatorStrategy

/**
 * @param ItemType type of the item to be shown in the list
 * @param context Android context
 * @param itemViewBinders a mapping between item types and view binders
 * meant  to render the respective items
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param stableIds - if false, forces the whole list to be updated whenever the changes arrive
 *
 * @property items A collection of items shown in the list
 */
@SuppressLint("ViewConstructor")
class ItemListView<ItemType : Item>(
    context: Context,
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = context::defaultBindEmpty,
    stableIds: Boolean = false
) : Widget(context), ItemContainer<ItemType> {

    private val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    override val items = BindableField(
        listOf<ItemType>(),
        { item -> kryo.copy(ArrayList(item)) }
    )

    init {
        RecyclerView(context).apply {
            id = R.id.recyclerView
            fullSize()
            clipToPadding = false
            bottomPadding = dip(60)
            isSaveEnabled = false
            adapter = ItemAdapter(items, emptyViewBinder, itemViewBinders).apply {
                setHasStableIds(stableIds)
            }
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }.setAsOne(this)

    }

}
