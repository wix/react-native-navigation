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
        super.onMeasure(interceptReactRootViewMeasureSpec(widthMeasureSpec), heightMeasureSpec)
    }

    private fun interceptReactRootViewMeasureSpec(widthMeasureSpec: Int): Int {
        // This is a HACK.
        // ReactRootView has problematic behavior when setting width to WRAP_CONTENT,
        // It's causing infinite measurements, that hung up the UI.
        // Intercepting largest child by width, and use its width as (parent) ReactRootView width fixed that.
        // See for more details https://github.com/wix/react-native-navigation/pull/7096
        val view = getWidestView(rootViewGroup, 0)
        val measuredWidth = view?.measuredWidth ?: 0
        return if (measuredWidth > 0) MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY) else
            widthMeasureSpec
    }

    private fun getWidestView(viewGroup: ViewGroup, currentMaxWidth: Int): View? {
        if (viewGroup.children.count() == 0) {
            return null
        }

        var maxWidth = currentMaxWidth
        var result: View? = null

        for (child in viewGroup.children) {
            if (child is ViewGroup) {
                result = getWidestView(child, maxWidth)
                maxWidth = result?.measuredWidth ?: 0
            } else if (child.measuredWidth > maxWidth){
                maxWidth = child.measuredWidth
                result = child
            }
        }

        return result
    }
}
