package com.reactnativenavigation.viewcontrollers.splash

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.reactnativenavigation.R
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.viewcontrollers.viewcontroller.NoOpYellowBoxDelegate
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewControllerOverlay

class SplashViewController(activity: Activity) : ViewController<ViewGroup>(
        activity,
        "SplashView",
        NoOpYellowBoxDelegate(activity),
        Options.EMPTY,
        ViewControllerOverlay(activity)
) {
    override fun getCurrentComponentName() = ""

    override fun createView() = findSplashView()

    fun findSplashView(): ViewGroup? = activity.findViewById(R.id.splash_view)

    override fun sendOnNavigationButtonPressed(buttonId: String) {
        // NOOP
    }

    override fun findView(id: String): View? {
        if (isDestroyed) return null
        return getView()?.let {
            ViewUtils.findChildrenByClassRecursive(it, View::class.java) { child: View ->
                id == child.getTag(R.id.nativeId)
            }.first()
        }
    }
}