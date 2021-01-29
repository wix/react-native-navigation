package com.reactnativenavigation.options.params

import android.graphics.Color
import androidx.annotation.ColorInt
open class Colour(@ColorInt color: Int) : Param<Int>(color) {
    override fun toString(): String {
        return String.format("#%06X", 0xFFFFFF and get())
    }

    fun hasTransparency(): Boolean {
        return hasValue() && Color.alpha(value) < 1
    }
}


object NullColor : Colour(0) {
    override fun hasValue(): Boolean {
        return false
    }

    override fun toString(): String {
        return "Null Color"
    }
}