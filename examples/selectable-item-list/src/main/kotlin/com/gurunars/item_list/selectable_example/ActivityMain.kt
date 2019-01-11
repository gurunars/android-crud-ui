package com.gurunars.item_list.selectable_example

import android.app.Activity
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.gurunars.animal_item.AnimalItem
import com.gurunars.animal_item.Service
import com.gurunars.animal_item.Service.Companion.getRealService
import com.gurunars.animal_item.renderAnimal
import com.gurunars.box.*
import com.gurunars.box.ui.layoutAsOne
import com.gurunars.box.ui.statefulView
import com.gurunars.item_list.*

class ActivityMain : Activity() {

    lateinit var srv: Service
    lateinit var items: IBox<List<AnimalItem>>

    private val selectedItems = Box<Set<Long>>(setOf())
    private val explicitSelectionMode = false.box

    private fun add(type: AnimalItem.Type) {
        items.patch { this + AnimalItem(-this.size.toLong(), type, 0) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        srv = getRealService()
        items = srv.items

        statefulView(R.id.main) {
            retain(selectedItems, explicitSelectionMode)
            itemListView(
                items = items,
                renderer = Renderer(
                    renderWith<AnimalItem> { item ->
                        withSelector(selectedItems, item) { isSelected ->
                            renderAnimal(item).coloredRowSelectionDecorator(isSelected)
                        }
                    }
                )
            ).layoutAsOne(this)
        }.layoutAsOne(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_selection -> return showToast(clearSelection())
            R.id.create -> return showToast(create())
            R.id.delete_selected -> return showToast(deleteSelected())
            R.id.update_selected -> return showToast(updateSelected())
            R.id.reset -> return showToast(reset())
            R.id.enable_explicit_selection -> return showToast(enableExplicitSelection())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun enableExplicitSelection(): Int {
        explicitSelectionMode.set(true)
        return R.string.explicit_selection_enabled
    }

    private fun updateSelected(): Int {
        val selected = selectedItems.get()
        items.patch {
            map {
                if (selected.contains(it.id))
                    it.copy(version = it.version + 1)
                else
                    it
            }
        }
        return R.string.did_update_selected
    }

    private fun deleteSelected(): Int {
        items.patch {
            filter {
                !selectedItems.get().contains(it.id)
            }
        }
        return R.string.did_delete_selected
    }

    private fun showToast(@StringRes text: Int): Boolean {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        return true
    }

    @StringRes private fun clearSelection(): Int {
        selectedItems.set(setOf())
        return R.string.did_clear_selection
    }

    @StringRes private fun create(): Int {
        add(AnimalItem.Type.TIGER)
        add(AnimalItem.Type.WOLF)
        add(AnimalItem.Type.MONKEY)
        add(AnimalItem.Type.LION)
        return R.string.did_create
    }

    @StringRes private fun reset(): Int {
        srv.clear()
        explicitSelectionMode.set(false)
        create()
        return R.string.did_reset
    }
}