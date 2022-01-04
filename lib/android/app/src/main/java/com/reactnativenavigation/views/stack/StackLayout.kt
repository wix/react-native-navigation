package com.reactnativenavigation.views.stack

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController
import com.reactnativenavigation.views.component.Component
import com.reactnativenavigation.views.component.Renderable
import com.reactnativenavigation.views.stack.topbar.ScrollDIsabledBehavior
import com.reactnativenavigation.views.tooltips.AttachedOverlayContainer

@SuppressLint("ViewConstructor")
class StackLayout(
    context: Context,
    topBarController: TopBarController,
    stackId: String?
) : CoordinatorLayout(context), Component {
    val overlay: AttachedOverlayContainer = AttachedOverlayContainer(context, "Stack Layout: $stackId")

    init {
        createLayout(topBarController)
    }

    private fun createLayout(topBarController: TopBarController) {
        val topBar: View = topBarController.createView(context, this)
        val lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtils.getTopBarHeight(context))
        lp.behavior = ScrollDIsabledBehavior()
        addView(topBar, lp)
        addView(overlay, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    override fun isRendered(): Boolean {
        return childCount >= 3 &&
                getChildAt(2) is Renderable &&
                (getChildAt(2) as Renderable).isRendered
    }


}