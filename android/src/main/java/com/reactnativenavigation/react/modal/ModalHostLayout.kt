package com.reactnativenavigation.react.modal

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.view.View
import android.view.ViewGroup
import android.view.ViewStructure
import android.view.accessibility.AccessibilityEvent
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.uimanager.ThemedReactContext
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.utils.CompatUtils
import com.reactnativenavigation.viewcontrollers.viewcontroller.YellowBoxDelegate
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay
import java.util.*

@SuppressLint("ViewConstructor")
open class ModalHostLayout(reactContext: ThemedReactContext) : ViewGroup(reactContext), LifecycleEventListener {
    val viewController = ModalLayoutController(
        reactContext,
        reactContext.currentActivity, CompatUtils.generateViewId().toString(),
        YellowBoxDelegate(reactContext), Options().apply {
            hardwareBack.dismissModalOnPress = Bool(false)
        }, ViewControllerOverlay(reactContext),
        getHostId = { this.id }
    )
    private val mHostView = viewController.view.modalContentLayout

    init {
        (context as ReactContext).addLifecycleEventListener(this)
    }

    @TargetApi(23)
    override fun dispatchProvideStructure(structure: ViewStructure?) {
        mHostView.dispatchProvideStructure(structure)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

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

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean { return false }

    open fun onDropInstance() { (this.context as ReactContext).removeLifecycleEventListener(this) }

    override fun onHostResume() {}

    override fun onHostPause() {}

    override fun onHostDestroy() { onDropInstance() }
}