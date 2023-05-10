package com.reactnativenavigation.views.stack.topbar.titlebar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.core.view.children
import com.facebook.react.ReactInstanceManager
import com.reactnativenavigation.react.ReactView

@SuppressLint("ViewConstructor")
class TitleBarReactView(context: Context?, reactInstanceManager: ReactInstanceManager?, componentId: String?,
                        componentName: String?) : ReactView(context, reactInstanceManager, componentId, componentName) {

    private var currentMaxWidth = 0
    private var currentHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(interceptReactRootViewMeasureSpec(widthMeasureSpec), heightMeasureSpec)
    }

    private fun interceptReactRootViewMeasureSpec(widthMeasureSpec: Int): Int {
        // This is a HACK.
        // ReactRootView has problematic behavior when setting width to WRAP_CONTENT,
        // It's causing infinite measurements, that hung up the UI.
        // Intercepting largest child by width, and use its width as (parent) ReactRootView width fixed that.
        // See for more details https://github.com/wix/react-native-navigation/pull/7096
        findMaxWidth(rootViewGroup)
        return if (currentMaxWidth > 0) MeasureSpec.makeMeasureSpec(currentMaxWidth, MeasureSpec.EXACTLY) else
            widthMeasureSpec
    }

    private fun findMaxWidth(viewGroup: ViewGroup) {
        for (child in viewGroup.children) {
            if (child.width > currentMaxWidth){
                currentMaxWidth = child.width
                currentHeight = child.height
                Log.d("TitleBarReactView", "currentMaxWidth = $currentMaxWidth")
            }
            if (child is ViewGroup) {
                findMaxWidth(child)
            }
        }
    }
}
