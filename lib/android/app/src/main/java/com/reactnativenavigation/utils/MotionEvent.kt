package com.reactnativenavigation.utils

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.reactnativenavigation.BuildConfig

val hitRect = Rect()

fun MotionEvent.coordinatesInsideView(view: View?): Boolean {
    if (BuildConfig.REACT_NATVE_VERSION_MINOR < 71) {
        view ?: return false
        view.getHitRect(hitRect)
        return hitRect.contains(x.toInt(), y.toInt())
    } else {
        val viewGroup = (view as? ViewGroup)?.getChildAt(0) as? ViewGroup ?: view

        viewGroup?.getHitRect(hitRect)
        return hitRect.contains(x.toInt(), y.toInt())
    }
}