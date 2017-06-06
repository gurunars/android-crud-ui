package com.gurunars.shortcuts

import android.support.v4.content.ContextCompat
import android.view.View

inline fun View.color(colorId: Int): Int = ContextCompat.getColor(context, colorId)