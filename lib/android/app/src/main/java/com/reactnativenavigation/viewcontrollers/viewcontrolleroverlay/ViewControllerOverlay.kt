package com.reactnativenavigation.viewcontrollers.viewcontrolleroverlay

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import com.reactnativenavigation.utils.ViewUtils.removeFromParent

class ViewControllerOverlay(context: Context) {
    private val overlay: FrameLayout = FrameLayout(context)

    fun add(parent: ViewGroup, view: View) {
        attachOverlayToParent(parent)
        overlay.addView(view)
    }

    fun remove(view: View) {
        overlay.removeView(view)
        if (overlay.childCount == 0) removeFromParent(overlay)
    }

    private fun attachOverlayToParent(parent: ViewGroup) {
        if (overlay.parent == null) {
            parent.addView(overlay, MATCH_PARENT, MATCH_PARENT)
        }
    }
}