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

    private var currentLeft = Int.MAX_VALUE
    private var currentRight = 0
    private var currentTop = Int.MAX_VALUE
    private var currentBottom = 0
    private var garbageViews = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (currentRight == 0) {
            findSize(rootViewGroup)
        }
        super.onMeasure(interceptReactRootViewMeasureSpecWidth(widthMeasureSpec),
                interceptReactRootViewMeasureSpecHeight(heightMeasureSpec))
    }

    private fun interceptReactRootViewMeasureSpecWidth(widthMeasureSpec: Int): Int {
        // This is a HACK.
        // ReactRootView has problematic behavior when setting width to WRAP_CONTENT,
        // It's causing infinite measurements, that hung up the UI.
        // Intercepting largest child by width, and use its width as (parent) ReactRootView width fixed that.
        // See for more details https://github.com/wix/react-native-navigation/pull/7096
        val width = currentRight - currentLeft
        return if (width > 0) MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY) else
            widthMeasureSpec
    }

    private fun interceptReactRootViewMeasureSpecHeight(heightMeasureSpec: Int): Int {
        // This is a HACK.
        // ReactRootView has problematic behavior when setting width to WRAP_CONTENT,
        // It's causing infinite measurements, that hung up the UI.
        // Intercepting largest child by height, and use its height as (parent) ReactRootView width fixed that.
        // See for more details https://github.com/wix/react-native-navigation/pull/7096

        val height = currentBottom - currentTop
        return if (height > 0) MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY) else
            heightMeasureSpec
    }

    private fun findSize(viewGroup: ViewGroup) {
        if (garbageViews) {
            return
        }

        for (child in viewGroup.children) {
            if (child.isShown) {
                if (child is ViewGroup) {
                    findSize(child)
                } else {
                    val location = IntArray(2)
                    child.getLocationOnScreen(location)
                    val childLeft = location[0]
                    val childRight = childLeft + child.measuredWidth
                    val childTop = location[1]
                    val childBottom = childTop + child.measuredHeight

                    if (childTop >= 50) { // To filter garbage views, such as debug warning messages
                        currentLeft = currentLeft.coerceAtMost(childLeft)
                        currentRight = currentRight.coerceAtLeast(childRight)
                        currentTop = currentTop.coerceAtMost(childTop)
                        currentBottom = currentBottom.coerceAtLeast(childBottom)

                        Log.d("TitleBarReactView", "current ViewGroup = $viewGroup")
                        Log.d("TitleBarReactView", "child.measuredWidth = ${child.measuredWidth}")
                        Log.d("TitleBarReactView", "child.measuredHeight = ${child.measuredHeight}")
                        Log.d("TitleBarReactView", "currentLeft = $currentLeft")
                        Log.d("TitleBarReactView", "currentRight = $currentRight")
                        Log.d("TitleBarReactView", "currentWidth = ${currentRight - currentLeft}")
                        Log.d("TitleBarReactView", "currentTop = $currentTop")
                        Log.d("TitleBarReactView", "currentBottom = $currentBottom")
                        Log.d("TitleBarReactView", "currentHeight = ${currentBottom - currentTop}")
                        Log.d("TitleBarReactView", "---------------------------------------------")
                    } else {

                        Log.d("TitleBarReactView", "current ViewGroup (garbage) = $viewGroup")
                        Log.d("TitleBarReactView", "child (garbage) = ${child.toString()}")
                        Log.d("TitleBarReactView", "child.type (garbage) = ${child.javaClass.name}")
                        Log.d("TitleBarReactView", "child.id (garbage) = ${child.id}")

                        garbageViews = true
                        return
                    }
                }
            }
        }
    }
}
