package com.reactnativenavigation.views.stack.topbar.titlebar

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.reactnativenavigation.react.ReactView

@SuppressLint("ViewConstructor")
class TitleBarReactView(context: Context?, componentId: String?,
                        componentName: String?) : ReactView(context, componentId, componentName) {
    var centered: Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (centered) {
            val availableWidth = MeasureSpec.getSize(widthMeasureSpec)
            super.onMeasure(
                MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.EXACTLY),
                heightMeasureSpec
            )
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}