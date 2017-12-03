package com.gurunars.floatmenu

import android.content.Context
import android.view.View
import com.gurunars.android_utils.Icon
import com.gurunars.databinding.Box
import com.gurunars.databinding.IBox
import com.gurunars.databinding.android.HorizontalAlignment
import com.gurunars.databinding.android.add
import com.gurunars.databinding.android.alignInParent
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.android.setAsOne
import com.gurunars.databinding.android.statefulView
import com.gurunars.databinding.branch
import com.gurunars.databinding.onChange
import org.jetbrains.anko.alignParentBottom
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.margin
import org.jetbrains.anko.relativeLayout

/**
 * An aggregation of view an an icon that controls its visibility in the menu.
 *
 * @property icon FAB icon to be shown when the view is visible
 */
interface ContentPane {
    /**
     * Returns a rendered view.
     */
    fun Context.render(): View

    val icon: Icon
}

/**
 * Content view to be show when the menu is open.
 *
 * @property hasOverlay if false - a view below will be clickable
 */
interface MenuPane : ContentPane {
    val hasOverlay: Boolean
}

/**
 * Floating menu available via a
 * [FAB](https://material.google.com/components/buttons-floating-action-button.html)
 *
 * @param contentPane View shown in the background layer of the widget. Semantically it
 * represents the data manipulated by the menu.
 * @param menuPane View shown in the foreground layer of the widget when the menu is open.
 * Is supposed to contain menu's controls.
 * @param animationDuration Time it takes to perform all the animated UI transitions.
 * @param isOpen flag indicating visibility of the menu pane on the screen
 */
fun Context.floatMenu(
    contentPane: IBox<ContentPane>,
    menuPane: IBox<MenuPane>,
    animationDuration: Int = 400,
    isOpen: IBox<Boolean> = Box(false)
) = statefulView(R.id.floatMenu, "FLOAT MENU") {
    retain(isOpen)

    relativeLayout {
        fullSize()
        frameLayout {
            id = R.id.contentPane
            contentPane.onChange { value ->
                value.apply {
                    render().setAsOne(this@frameLayout)
                }
            }
        }.fullSize()

        MenuHolder(context,
            menuPane.branch { hasOverlay },
            isOpen,
            animationDuration
        ).add(this) {
            id = R.id.menuPane
            fullSize()
            isClickable = true
            menuPane.onChange { value ->
                value.apply {
                    render().setAsOne(this@add)
                }
            }
        }
        fab(animationDuration,
            contentPane.branch { icon },
            menuPane.branch { icon },
            isOpen
        ).add(this) {
            id = R.id.openFab
        }.lparams {
            margin = dip(16)
            width = dip(60)
            height = dip(60)
            alignParentBottom()
            alignInParent(HorizontalAlignment.RIGHT)
        }
    }
}
