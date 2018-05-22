package com.gurunars.floatmenu

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.android_utils.Icon
import com.gurunars.box.*
import com.gurunars.box.ui.*
import org.jetbrains.anko.dip

/**
 * An aggregation with view an an icon that controls its visibility in the menu.
 *
 * @property icon FAB icon to be shown when the view is visible
 */
interface ContentPane {
    /** Returns a rendered view. */
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
 * @param contentPane View shown in the background layer with the widget. Semantically it
 * represents the data manipulated by the menu.
 * @param menuPane View shown in the foreground layer with the widget when the menu is open.
 * Is supposed to contain menu's controls.
 * @param animationDuration Time it takes to perform all the animated UI transitions.
 * @param isOpen flag indicating visibility with the menu pane on the screen
 */
fun Context.floatMenu(
    contentPane: IRoBox<ContentPane>,
    menuPane: IRoBox<MenuPane>,
    animationDuration: Int = 400,
    isOpen: IBox<Boolean> = Box(false)
) = statefulView(R.id.floatMenu) {
    retain(isOpen)

    with<RelativeLayout> {
        fullSize()
        fl@ with<FrameLayout> {
            id = R.id.contentPane
            contentPane.onChange { value ->
                value.apply {
                    render().layoutAsOne(this@fl)
                }
            }
        }.layout(this) {

        }

        MenuHolder(context,
            menuPane.oneWayBranch { hasOverlay },
            isOpen,
            animationDuration
        ).layout(this) {
            id = R.id.menuPane
            fullSize()
            isClickable = true
            menuPane.onChange { value ->
                value.apply {
                    render().layoutAsOne(this@layout)
                }
            }
        }
        fab(animationDuration,
            contentPane.oneWayBranch { icon },
            menuPane.oneWayBranch { icon },
            isOpen
        ).layout(this) {
            id = R.id.openFab
            margin = dip(16)
            width = dip(60)
            height = dip(60)
            alignParentBottom()
            alignInParent(HorizontalAlignment.RIGHT)
        }
    }
}
