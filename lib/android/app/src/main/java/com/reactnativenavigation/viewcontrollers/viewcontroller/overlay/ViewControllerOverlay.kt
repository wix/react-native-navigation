package com.reactnativenavigation.viewcontrollers.viewcontroller.overlay

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.reactnativenavigation.utils.removeFromParent

open class ViewControllerOverlay(context: Context) {
    private val overlay = OverlayLayout(context)

    open fun add(parent: ViewGroup, view: View, layoutParams: ViewGroup.LayoutParams) {
        attachOverlayToParent(parent)
        overlay.addView(view, layoutParams)
    }

    fun remove(view: View) {
        overlay.removeView(view)
        if (overlay.childCount == 0) overlay.removeFromParent()
    }

    private fun attachOverlayToParent(parent: ViewGroup) {
        if (overlay.parent == null) {
            parent.addView(overlay, MATCH_PARENT, MATCH_PARENT)
        }
    }
}