package com.reactnativenavigation.views.stack.topbar.titlebar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.facebook.react.ReactInstanceManager
import com.reactnativenavigation.react.ReactView

@SuppressLint("ViewConstructor")
class TitleBarReactView(context: Context?, reactInstanceManager: ReactInstanceManager?, componentId: String?,
                        componentName: String?) : ReactView(context, reactInstanceManager, componentId, componentName) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(interceptReactRootViewMeasureSpecWidth(widthMeasureSpec), interceptReactRootViewMeasureSpecHeight(heightMeasureSpec))
    }

    private fun interceptReactRootViewMeasureSpecWidth(widthMeasureSpec: Int): Int {
        // This is a HACK.
        // ReactRootView has problematic behavior when setting width to WRAP_CONTENT,
        // It's causing infinite measurements, that hung up the UI.
        // Intercepting largest child by width, and use its width as (parent) ReactRootView width fixed that.
        // See for more details https://github.com/wix/react-native-navigation/pull/7096
        val measuredWidth = this.getLastRootViewChild()?.width

        return if (measuredWidth != null) MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY) else
            widthMeasureSpec
    }

    private fun interceptReactRootViewMeasureSpecHeight(heightMeasureSpec: Int): Int {
        // This is a HACK.
        // ReactRootView has problematic behavior when setting width to WRAP_CONTENT,
        // It's causing infinite measurements, that hung up the UI.
        // Intercepting largest child by height, and use its height as (parent) ReactRootView width fixed that.
        // See for more details https://github.com/wix/react-native-navigation/pull/7096
        val measuredHeight = this.getLastRootViewChild()?.height

        return if (measuredHeight != null) MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY) else
            heightMeasureSpec
    }

    private fun getLastRootViewChild(): View? {
        if (rootViewGroup.children.count() == 0) {
            return null
        }
        var rootViewGroupLastChild: View = rootViewGroup
        var next = rootViewGroup as Any
        while(next is ViewGroup && next.childCount > 0) try {
            rootViewGroupLastChild = next
            next.children.first().also { next = it }

        } catch (e: Exception) {
            Log.i("TitleBarReactView", "getRootViewFirstChild: ${e.message}")
        }
        @Suppress("UNREACHABLE_CODE")
        return rootViewGroupLastChild
    }
}