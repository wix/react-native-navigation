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
        val spec = interceptReactRootViewMeasureSpec(widthMeasureSpec)
        super.onMeasure(spec, heightMeasureSpec)
    }

    private fun interceptReactRootViewMeasureSpec(widthMeasureSpec: Int): Int {
        //This is a hack, ReactRootView manipulates the children by UIManageModule that is being done via
        // UIManageModule, Will work fine if the ReactRootView has fixed dimension or MATCH_PARENT and then the
        // measure is taking the result of the width spec, and continue safely whereas the WRAP_CONTENT will cause
        // the measurement to be always as the parent width or 0, instead, intercepting the measured child early and
        // set the measurement for the parent as the largest react view child.
        // see https://github.com/wix/react-native-navigation/pull/7096
        val measuredWidth = this.children.maxByOrNull { it.measuredWidth }?.measuredWidth ?: 0
        return if (measuredWidth > 0) MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY) else
            widthMeasureSpec
    }
}
