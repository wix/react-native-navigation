package com.reactnativenavigation.views.animations

import android.graphics.drawable.ColorDrawable
import android.util.IntProperty
import android.util.Property
import android.view.View

val View.BkgColorProperty: Property<View, Int>
    get() = object: IntProperty<View>("bkgColor") {
        override fun setValue(view: View, value: Int) {
            (view.background as ColorDrawable).color = value
        }

        override fun get(view: View): Int {
            return (view.background as ColorDrawable).color
        }
    }
