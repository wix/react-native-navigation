package com.reactnativenavigation.views.sidemenu

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.SideMenuOptions
import com.reactnativenavigation.utils.CoordinatorLayoutUtils
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.BehaviourAdapter
import com.reactnativenavigation.views.BehaviourDelegate
import com.reactnativenavigation.views.tooltips.TooltipsOverlay

class SideMenuRoot(context: Context) : CoordinatorLayout(context) {
    private var sideMenu: SideMenu? = null
    
    fun addSideMenu(sideMenu: SideMenu, behaviourAdapter: BehaviourAdapter?) {
        this.sideMenu = sideMenu
        enableDrawingBehindStatusBar()
        addView(sideMenu, CoordinatorLayoutUtils.matchParentWithBehaviour(BehaviourDelegate(behaviourAdapter)))
    }

    fun isDrawerOpen(gravity: Int): Boolean {
        return sideMenu?.isDrawerOpen(gravity)?:false
    }

    fun setCenter(center: ViewController<*>) {
        sideMenu?.addView(center.view)
    }

    @SuppressLint("RtlHardcoded")
    fun setLeft(left: ViewController<*>, options: Options) {
        sideMenu?.addView(left.view, createLayoutParams(options.sideMenuRootOptions.left, Gravity.LEFT))
    }

    @SuppressLint("RtlHardcoded")
    fun setRight(right: ViewController<*>, options: Options) {
        sideMenu?.addView(right.view, createLayoutParams(options.sideMenuRootOptions.right, Gravity.RIGHT))
    }

    fun isSideMenu(view: View): Boolean {
        return sideMenu === view
    }

    private fun createLayoutParams(options: SideMenuOptions, gravity: Int): DrawerLayout.LayoutParams {
        return DrawerLayout.LayoutParams(getWidth(options), getHeight(options), gravity)
    }

    private fun getWidth(sideMenuOptions: SideMenuOptions): Int {
        var width = ViewGroup.LayoutParams.MATCH_PARENT
        if (sideMenuOptions.width.hasValue()) {
            width = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                sideMenuOptions.width.get().toFloat(),
                Resources.getSystem().displayMetrics
            ).toInt()
        }
        return width
    }

    private fun getHeight(sideMenuOptions: SideMenuOptions): Int {
        var height = ViewGroup.LayoutParams.MATCH_PARENT
        if (sideMenuOptions.height.hasValue()) {
            height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                sideMenuOptions.height.get().toFloat(),
                Resources.getSystem().displayMetrics
            ).toInt()
        }
        return height
    }

    private fun enableDrawingBehindStatusBar() {
        sideMenu?.fitsSystemWindows = true
        sideMenu?.let { ViewCompat.setOnApplyWindowInsetsListener(it) { view: View?, insets: WindowInsetsCompat? -> insets } }
    }
}