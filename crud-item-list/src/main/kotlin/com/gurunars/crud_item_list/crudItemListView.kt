package com.gurunars.crud_item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.android.set
import com.gurunars.databinding.android.statefulComponent
import com.gurunars.databinding.android.viewSelector
import com.gurunars.databinding.branch
import com.gurunars.databinding.field
import com.gurunars.databinding.onChange
import com.gurunars.floatmenu.floatMenu
import com.gurunars.item_list.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.progressBar


/**
 * Widget to be used for manipulating a collection of items with a dedicated set of UI controls.
 *
 * @param ItemType type of the item to be shown in the list
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param sortable If false move up and move down buttons are hidden.
 * @param groupedItemTypeDescriptors a collection of item type descriptors
 * @param listActionColors Color of the icons meant to manipulate the collection of items in the
 * contextual menu.
 * @param confirmationActionColors Check mark icon color settings. The icon is shown when contextual
 * menu is opened. Clicking the icon closes contextual menu.
 * @param cancelActionColors Cross icon color settings. The icon is shown when creation menu is
 * opened. Clicking the icon closes the menu.
 * @param openIconColors Plus icon color settings. The icon is shown when the menu is closed.
 * Clicking the icon opens the creation menu
 * @param items A collection of items shown and manipulated by the view.
 * @param isOpen A flag specifying if the menu is open or closed. Be it a creation or contextual
 * one.
 */
fun <ItemType : Item> Context.crudItemListView(
    groupedItemTypeDescriptors: BindableField<List<List<ItemTypeDescriptor<ItemType>>>>,
    items: BindableField<List<ItemType>>,
    emptyViewBinder: BindableField<EmptyViewBinder> = this::defaultBindEmpty.field,
    sortable: BindableField<Boolean> = true.field,
    listActionColors: BindableField<IconColorBundle> = IconColorBundle().field,
    confirmationActionColors: BindableField<IconColorBundle> = IconColorBundle().field,
    cancelActionColors: BindableField<IconColorBundle> = IconColorBundle().field,
    openIconColors: BindableField<IconColorBundle> = IconColorBundle().field,
    isOpen: BindableField<Boolean> = false.field
): View = statefulComponent(R.id.crudItemListView, "CRUD ITEM LIST") {
    val selectedItems = BindableField<Set<ItemType>>(setOf())
    val itemInEdit: BindableField<ItemType?> = null.field
    val selectedView = ViewMode.CREATION.field
    val creationCloseIcon = cancelActionColors.branch { icon(R.drawable.ic_menu_close) }
    val contextualCloseIcon = confirmationActionColors.branch { icon(R.drawable.ic_check) }
    val openIcon = openIconColors.branch { icon(R.drawable.ic_plus) }
    val closeIcon = creationCloseIcon.get().field
    val hasOverlay = true.field

    val typeCache = groupedItemTypeDescriptors.branch {
        flatten().map {
            Pair(it.type, it)
        }.toMap()
    }

    val itemForm = context.frameLayout {
        fullSize()
        id = R.id.itemForm
    }

    fun onSave(item: ItemType) {
        items.set(processItemInEdit(item, items.get()))
        isOpen.set(false)
    }

    val stateMachine = StateMachine<ItemType>(
        selectedItems,
        isOpen,
        itemInEdit,
        selectedView,
        itemTypes = typeCache.branch { keys },
        loadItem = { typeCache.get()[it]!!.createNewItem() },
        bindForm = {
            itemForm(
                it,
                ::onSave,
                confirmationActionColors,
                typeCache.get()[it.type]!!
            ).set(R.id.formContent, itemForm)
        }
    )

    // TODO: move it to be external?
    // OR: add an extra section for the insides
    val contextualMenu = contextualMenu(
        sortable,
        listActionColors,
        items,
        selectedItems,
        // TODO: use double click?
        { itemInEdit.set(it) }
    )

    val loading = context.frameLayout {
        progressBar().lparams {
            width = dip(80)
            height = dip(80)
            gravity = Gravity.CENTER
        }
    }

    val creationMenu = creationMenu(
        groupedItemTypeDescriptors,
        { itemInEdit.set(it) }
    )

    val itemListView = selectableItemListView(
        items = items,
        selectedItems = selectedItems,
        itemViewBinders = groupedItemTypeDescriptors.branch {
            flatten().map {
                Pair(it.type,
                    { item: BindableField<SelectableItem<ItemType>> -> it.bindRow(item) }
                )
            }.toMap()
        },
        emptyViewBinder = emptyViewBinder
    )

    listOf(creationCloseIcon, contextualCloseIcon, selectedView).onChange {
        when (selectedView.get()) {
            ViewMode.CONTEXTUAL
                -> closeIcon.set(contextualCloseIcon.get())
            ViewMode.LOADING, ViewMode.CREATION, ViewMode.FORM
                -> closeIcon.set(creationCloseIcon.get())
            else
                -> { }
        }
    }

    selectedView.onChange {
        if (it.overlay != Overlay.SAME) {
            hasOverlay.set(it.overlay == Overlay.YES)
        }
    }

    val knobView = viewSelector(
        ViewMode.EMPTY to null,
        ViewMode.FORM to itemForm,
        ViewMode.LOADING to loading,
        ViewMode.CONTEXTUAL to contextualMenu,
        ViewMode.CREATION to creationMenu,
        selectedView = selectedView
    )

    retain(selectedView, itemInEdit, selectedItems, isOpen)

    groupedItemTypeDescriptors.onChange {
        stateMachine.clear()
        floatMenu(
            itemListView.field,
            knobView.field,
            closeIcon = closeIcon,
            openIcon = openIcon,
            hasOverlay = hasOverlay,
            isOpen = isOpen
        ).set(R.id.contentPane, this)
    }
}
