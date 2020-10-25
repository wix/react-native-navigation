package com.reactnativenavigation.utils

import android.view.View
import android.view.ViewParent
import com.reactnativenavigation.react.ReactView
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.OverlayLayout

fun areDimensionsWithInheritedScaleEqual(a: View, b: View): Boolean {
    val (aScaleX, aScaleY) = computeInheritedScale(a)
    val (bScaleX, bScaleY) = computeInheritedScale(b)
    return a.width * aScaleX == b.width * bScaleX &&
            a.height * aScaleY == b.height * bScaleY
}

fun computeInheritedScale(v: View): Scale {
    return computeInheritedScale(v.parent, Scale(x = v.scaleX, y = v.scaleY))
}

private fun computeInheritedScale(v: ViewParent, childrenScale: Scale): Scale {
    return if (v is ReactView || v is OverlayLayout || v.parent == null) {
        childrenScale
    } else {
        computeInheritedScale(v.parent, Scale(x = childrenScale.x * v.scaleX, y = childrenScale.y * v.scaleY))
    }
}

data class Scale(val x: Float, val y: Float)