package com.reactnativenavigation.interfaces

import android.content.res.ColorStateList
import android.view.View
import com.reactnativenavigation.parse.params.Colour

interface FloatingButton {
    val fabId: Any
    fun setBackgroundColor(get: Int)
    fun disableCollapse()
    fun setCustomSize(get: Int)
    fun setOnClickListener(function: (View?) -> Unit)
    fun show()
    fun hide()
    fun setRippleColor(color : ColorStateList? )
    fun applyIcon(icon: String, color: Colour)
    fun enableCollapse(scrollEventListener: ScrollEventListener)
    fun setSize(size: Int)
    fun bringToFront()
    fun setText(string: String?)
    fun setExtended(status : Boolean)
}