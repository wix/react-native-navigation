package com.reactnativenavigation.react

import android.annotation.TargetApi
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewStructure
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.uimanager.ThemedReactContext
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.utils.CompatUtils
import com.reactnativenavigation.viewcontrollers.viewcontroller.YellowBoxDelegate
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay
import java.util.*

open class DeclaredLayoutHost(reactContext: ThemedReactContext) : ViewGroup(reactContext), LifecycleEventListener {
    val viewController = DeclaredLayoutController(
        reactContext.currentActivity, CompatUtils.generateViewId().toString(),
        YellowBoxDelegate(reactContext), Options.EMPTY, ViewControllerOverlay(reactContext)
    )
    private val mHostView = viewController.view

    init {
        (context as ReactContext).addLifecycleEventListener(this)
    }

    @TargetApi(23)
    override fun dispatchProvideStructure(structure: ViewStructure?) {
        mHostView.dispatchProvideStructure(structure)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        this.dismiss()
    }

    override fun addView(child: View?, index: Int) {
        UiThreadUtil.assertOnUiThread()
        mHostView.addView(child, index)
    }

    override fun getChildCount(): Int {
        return mHostView.childCount
    }

    override fun getChildAt(index: Int): View? {
        return mHostView.getChildAt(index)
    }

    override fun removeView(child: View?) {
        UiThreadUtil.assertOnUiThread()
        mHostView.removeView(child)
    }

    override fun removeViewAt(index: Int) {
        UiThreadUtil.assertOnUiThread()
        val child = getChildAt(index)
        mHostView.removeView(child)
    }

    override fun addChildrenForAccessibility(outChildren: ArrayList<View?>?) {}

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
        return false
    }

    open fun onDropInstance() {
        (this.context as ReactContext).removeLifecycleEventListener(this)
        this.dismiss()
    }

    open fun dismiss() {
        UiThreadUtil.assertOnUiThread()
//        val parent = mHostView.parent as? ViewGroup
//        parent?.removeViewAt(0)
    }

    override fun onHostResume() {
        this.showOrUpdate()
    }

    override fun onHostPause() {}

    override fun onHostDestroy() {
        onDropInstance()
    }

    private fun getCurrentActivity(): Activity? {
        return (this.context as ReactContext).currentActivity
    }

    open fun showOrUpdate() {
        UiThreadUtil.assertOnUiThread()
        val currentActivity = getCurrentActivity()
        val context = currentActivity ?: this.context

    }

    open fun getContentView(): View? {
        val frameLayout = FrameLayout(this.context)
        frameLayout.addView(mHostView)
//        if (this.mStatusBarTranslucent) {
//            frameLayout.systemUiVisibility = 1024
//        } else {
//            frameLayout.fitsSystemWindows = true
//        }
        return frameLayout
    }

}