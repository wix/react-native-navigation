package com.reactnativenavigation.utils

import android.view.View
import android.view.ViewManager

fun View?.removeFromParent() = (this?.parent as? ViewManager)?.removeView(this)

fun View.isRTL() = layoutDirection == View.LAYOUT_DIRECTION_RTL

fun View.resetViewProperties() {
    x = 0f
    y = 0f
    translationX = 0f
    translationY = 0f
    alpha = 1f
    scaleX = 1f
    scaleY = 1f
    rotationX = 0f
    rotationY = 0f
    rotation = 0f
}