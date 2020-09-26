package com.reactnativenavigation.utils

import android.view.View

fun areDimensionsWithInheritedScaleEqual(a: View, b: View): Boolean {
    val (aScaleX, aScaleY) = computeInheritedScale(a)
    val (bScaleX, bScaleY) = computeInheritedScale(b)
    return a.width * aScaleX == b.width * bScaleX &&
            a.height * aScaleY == b.height * bScaleY
}

fun computeInheritedScale(v: View): Scale {
    return Scale(
            x = v.scaleX * v.parent.scaleX * v.grandparent.scaleX,
            y = v.scaleY * v.parent.scaleY * v.grandparent.scaleY
    )
}

data class Scale(val x: Float, val y: Float)