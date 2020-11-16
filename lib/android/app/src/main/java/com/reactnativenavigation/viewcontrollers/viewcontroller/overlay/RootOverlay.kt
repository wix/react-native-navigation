package com.reactnativenavigation.viewcontrollers.viewcontroller.overlay

import android.content.Context
import android.view.View
import android.view.ViewGroup

class RootOverlay(context: Context, private val contentLayout: ViewGroup) : ViewControllerOverlay(context) {
    override fun add(parent: ViewGroup, view: View, layoutParams: ViewGroup.LayoutParams) {
        super.add(this.contentLayout, view, layoutParams)
    }
}