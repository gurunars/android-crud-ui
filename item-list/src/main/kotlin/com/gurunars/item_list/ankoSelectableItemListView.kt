package com.gurunars.item_list

import android.app.Activity
import android.content.Context
import android.view.ViewManager


import kotlin.Unit
import org.jetbrains.anko.custom.ankoView

fun <ItemType : Item> ViewManager.selectableItemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<SelectableItem<ItemType>>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(),
    init: SelectableItemListView<ItemType>.() -> Unit
): SelectableItemListView<ItemType> = ankoView({ SelectableItemListView(it, itemViewBinders, emptyViewBinder) }, 0, init)

fun <ItemType : Item> Activity.selectableItemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<SelectableItem<ItemType>>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(),
    init: SelectableItemListView<ItemType>.() -> Unit
): SelectableItemListView<ItemType> = ankoView({ SelectableItemListView(it, itemViewBinders, emptyViewBinder) }, 0, init)

fun <ItemType : Item> Context.selectableItemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<SelectableItem<ItemType>>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(),
    init: SelectableItemListView<ItemType>.() -> Unit
): SelectableItemListView<ItemType> = ankoView({ SelectableItemListView(it, itemViewBinders, emptyViewBinder) }, 0, init)