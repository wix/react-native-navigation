package com.reactnativenavigation.viewcontrollers.viewcontroller.overlay

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout

class ModalOverlay(context: Context) : ViewControllerOverlay(context) {
    lateinit var modalsLayout: CoordinatorLayout

    override fun add(parent: ViewGroup, view: View, layoutParams: ViewGroup.LayoutParams) {
        super.add(modalsLayout, view, layoutParams)
    }
}