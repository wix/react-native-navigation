package com.reactnativenavigation.views.stack.topbar.titlebar

import android.annotation.SuppressLint
import android.content.Context
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
        var measuredWidth = 0;
        this.children.forEach {
            if (it.measuredWidth > measuredWidth) {
                measuredWidth = it.measuredWidth
            }
        }
        return if (measuredWidth > 0) MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY) else
            widthMeasureSpec
    }
}
