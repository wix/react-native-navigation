package com.reactnativenavigation.interfaces

import android.content.res.ColorStateList
import android.view.View
import com.reactnativenavigation.parse.params.Colour

interface FloatingButton {

    val fabId: Any

    fun setBackgroundColor(get: Int) {
        TODO("Not yet implemented")
    }

    fun disableCollapse() {
        TODO("Not yet implemented")
    }

    fun setCustomSize(get: Int?) {
        TODO("Not yet implemented")
    }

    fun setOnClickListener(function: (View?) -> Unit) {
        TODO("Not yet implemented")
    }

    fun show() {
        TODO("Not yet implemented")
    }

    fun hide() {
        TODO("Not yet implemented")
    }

    fun setRippleColor(color : ColorStateList? ) {
        TODO("Not yet implemented")
    }

    fun applyIcon(icon: String, color: Colour) {
        TODO("Not yet implemented")
    }

    fun enableCollapse(scrollEventListener: ScrollEventListener) {
        TODO("Not yet implemented")
    }

    fun setSize(size: Int) {
        TODO("Not yet implemented")
    }

    fun bringToFront() {
        TODO("Not yet implemented")
    }

    fun setText(string: String?) {
        TODO("Not yet implemented")
    }

    fun setExtended(status : Boolean) {
        TODO("Not yet implemented")
    }
}