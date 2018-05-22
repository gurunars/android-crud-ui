package com.gurunars.box.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.RelativeLayout

/** Replaces view marked by a specific id with a given new view */
fun <T : View> T.set(parent: ViewGroup, id: Int, init: T.() -> Unit = {}): T {
    val view = parent.findViewById<View>(id)
    if (view != null) parent.removeView(view)
    return layout(parent, init).apply {
        this.id = id
    }
}

/**
 * width = MATCH_PARENT
 * height = MATCH_PARENT
 */
fun ViewGroup.LayoutParams.fullSize() {
    width = MATCH_PARENT
    height = MATCH_PARENT
}

/**
 * width = MATCH_PARENT
 * height = WRAP_CONTENT
 */
fun ViewGroup.LayoutParams.asRow() {
    width = MATCH_PARENT
    height = WRAP_CONTENT
}

/**
 * width = 0
 * height = 0
 */
fun ViewGroup.LayoutParams.asEmpty() {
    width = 0
    height = 0
}

/** Base function to layout a view within a view group */
fun <T: View, Layout: ViewGroup, LayoutParams: ViewGroup.LayoutParams> T.layout(
    container: Layout,
    config: LayoutParams.() -> Unit,
    defaultConfig: LayoutParams
): T = with(container.context) {
    container.addView(this@layout.apply {
        layoutParams = defaultConfig.apply {
            config()
        }
    })
    this@layout
}

/** For FrameLayout */
fun View.layout(
    frameLayout: FrameLayout,
    config: FrameLayout.LayoutParams.() -> Unit
) = layout(frameLayout, config, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

/** For LinearLayout */
fun View.layout(
    linearLayout: LinearLayout,
    config: LinearLayout.LayoutParams.() -> Unit
) = layout(linearLayout, config, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))

/** For RelativeLayout */
fun View.layout(
    relativeLayout: RelativeLayout,
    config: RelativeLayout.LayoutParams.() -> Unit
) = layout(relativeLayout, config, RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))

/** Removes all the views and adds this view as the only full screen child */
fun <T : View> T.layoutAsOne(parent: FrameLayout): T {
    layout(parent) { fullSize() }
    parent.removeAllViews()
    parent.addView(this)
    return this
}

/** Removes all the views and adds this view as the only full screen child */
fun <T : View> T.layoutAsOne(parent: Activity): T {
    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    parent.setContentView(this)
    return this
}

/** Add a view to parent */
fun <T : View> T.layout(parent: ViewGroup, init: T.() -> Unit = {}): T {
    parent.addView(this)
    this.init()
    return this
}

/** Initializes a View within a given context */
inline fun <reified T: View> Context.with(init: T.() -> Unit = {}): T =
    T::class.java.getConstructor(Context::class.java).newInstance(this).apply {
        init()
    }