package com.reactnativenavigation.views.stack.topbar.titlebar

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.facebook.react.ReactInstanceManager
import com.reactnativenavigation.react.ReactView

@SuppressLint("ViewConstructor")
class TitleBarReactView(context: Context?, reactInstanceManager: ReactInstanceManager?, componentId: String?, componentName: String?) : ReactView(context, reactInstanceManager, componentId, componentName) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
                getWidthMeasureSpec(widthMeasureSpec),
                heightMeasureSpec
        )
    }

    private fun getWidthMeasureSpec(currentSpec: Int): Int {
        return if (isCenter && childCount > 0 && getChildAt(0).width > 0) MeasureSpec.makeMeasureSpec(getChildAt(0).width, MeasureSpec.EXACTLY) else currentSpec
    }

    private val isCenter: Boolean
        get() = layoutParams is FrameLayout.LayoutParams
}

