package com.reactnativenavigation.views.stack.topbar.titlebar

import android.annotation.SuppressLint
import android.content.Context
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
        val measuredWidth = this.getLastRootViewChildMaxWidth()

        return MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
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

    private fun getLastRootViewChildMaxWidth(): Int {
        if (rootViewGroup.children.count() == 0) {
            return 0
        }
        var maxWidth = rootViewGroup.width
        var next = rootViewGroup as Any
        while(next is ViewGroup) { //try {
            if (next.width > maxWidth) {
                maxWidth = next.width
            }
            if (next.children.count() == 0) break
            next.children.first().also { next = it }
        }
        return maxWidth
    }

    private fun getLastRootViewChild(): View? {
        if (rootViewGroup.children.count() == 0) {
            return null
        }
        var rootViewGroupLastChild: View = rootViewGroup
        var next = rootViewGroup as Any
        while(next is ViewGroup) { //try {
            rootViewGroupLastChild = next
            if (next.children.count() == 0) break
            next.children.first().also { next = it }
        }
        @Suppress("UNREACHABLE_CODE")
        return rootViewGroupLastChild
    }
}