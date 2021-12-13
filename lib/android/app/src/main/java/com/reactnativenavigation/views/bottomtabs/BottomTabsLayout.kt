package com.reactnativenavigation.views.bottomtabs

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.reactnativenavigation.views.tooltips.TooltipsOverlay

class BottomTabsLayout(context: Context) : CoordinatorLayout(context) {
    private var bottomTabsContainer: BottomTabsContainer? = null
    val tooltipsOverlay = TooltipsOverlay(context, " BottomTabs")

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tooltipsOverlay.setBackgroundColor(Color.argb(0.5f, 1f, 0f, 0f))
        }
        this.addView(tooltipsOverlay, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (bottomTabsContainer != null && child !== bottomTabsContainer) {
            super.addView(child, childCount - 1, params)
        } else {
            super.addView(child, 0, params)
        }
    }

    fun addBottomTabsContainer(bottomTabsContainer: BottomTabsContainer?) {
        val lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.BOTTOM
        addView(bottomTabsContainer, lp)
        this.bottomTabsContainer = bottomTabsContainer
    }
}