package com.reactnativenavigation.views.stack

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController
import com.reactnativenavigation.views.component.Component
import com.reactnativenavigation.views.component.Renderable
import com.reactnativenavigation.views.stack.topbar.ScrollDIsabledBehavior
import com.reactnativenavigation.views.tooltips.TooltipsOverlay

@SuppressLint("ViewConstructor")
class StackLayout(
    context: Context,
    topBarController: TopBarController,
    val stackId: String
) : CoordinatorLayout(context), Component {
    val overlay: TooltipsOverlay = TooltipsOverlay(context, "Stack Layout")

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            this.overlay.setBackgroundColor(Color.argb(0.5f,0f,1f,0f));
        }
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
        return childCount >= 2 &&
                getChildAt(1) is Renderable &&
                (getChildAt(1) as Renderable).isRendered
    }


}