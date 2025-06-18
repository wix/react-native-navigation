package com.reactnativenavigation.views.animations

import android.graphics.drawable.ColorDrawable
import android.util.Property
import android.view.View

val View.BkgColorProperty: Property<View, Int>
    // TODO Replace Property with IntProperty (Requires SDK≥24)
    get() = object: Property<View, Int>(Int::class.java, "bkgColor") {
        override fun set(view: View, value: Int) {
            (view.background as ColorDrawable).color = value
        }

        override fun get(view: View): Int {
            return (view.background as ColorDrawable).color
        }
    }
