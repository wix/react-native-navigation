package com.reactnativenavigation.views.stack.topbar.titlebar

import android.annotation.SuppressLint
import android.content.Context
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
        var measuredWidth = 0

        if (rootViewGroup.children.count() > 0) {
            measuredWidth = (((rootViewGroup.children.first() as ViewGroup).children.first() as ViewGroup).children.first() as ViewGroup).width
        }

        return if (measuredWidth > 0) MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY) else
            widthMeasureSpec
    }

    private fun interceptReactRootViewMeasureSpecHeight(heightMeasureSpec: Int): Int {
        // This is a HACK.
        // ReactRootView has problematic behavior when setting width to WRAP_CONTENT,
        // It's causing infinite measurements, that hung up the UI.
        // Intercepting largest child by width, and use its width as (parent) ReactRootView width fixed that.
        // See for more details https://github.com/wix/react-native-navigation/pull/7096
        var measuredHeight = 0

        if (rootViewGroup.children.count() > 0) {
            measuredHeight = (((rootViewGroup.children.first() as ViewGroup).children.first() as ViewGroup).children.first() as ViewGroup).height
        }

        return if (measuredHeight > 0) MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY) else
            heightMeasureSpec
    }
}
