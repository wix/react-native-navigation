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
    var centered: Boolean = false
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var titleHeightMeasureSpec: Int
        var titleWidthMeasureSpec: Int
        if (centered) {
            titleHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            titleWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        } else {
            titleHeightMeasureSpec = heightMeasureSpec
            titleWidthMeasureSpec = widthMeasureSpec
        }
        super.onMeasure(titleWidthMeasureSpec, titleHeightMeasureSpec)
    }
}