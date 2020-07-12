package com.reactnativenavigation.utils

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View

val hitRect = Rect()

fun MotionEvent.coordinatesInsideView(view: View?): Boolean {
    view ?: return false
    view.getHitRect(hitRect)
    return hitRect.contains(x.toInt(), y.toInt())
}