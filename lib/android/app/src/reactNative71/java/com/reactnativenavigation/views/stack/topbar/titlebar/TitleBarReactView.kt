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

    private data class Coordinates(var left: Int = Int.MAX_VALUE,
                                   var top: Int = Int.MAX_VALUE,
                                   var right: Int = 0,
                                   var bottom: Int = 0) {
        fun width(): Int {
            return if (left < Int.MAX_VALUE) right - left else 0
        }

        fun height(): Int {
            return if (top < Int.MAX_VALUE) bottom - top else 0
        }

        fun set(left: Int, top: Int, right: Int, bottom: Int) {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }
    }

    private val titleCoordinates = Coordinates()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (titleCoordinates.right == 0) {
            setSize(titleCoordinates)
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
        val width = titleCoordinates.width()
        return if (width > 0) MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY) else
            widthMeasureSpec
    }

    private fun interceptReactRootViewMeasureSpecHeight(heightMeasureSpec: Int): Int {
        // This is a HACK.
        // ReactRootView has problematic behavior when setting width to WRAP_CONTENT,
        // It's causing infinite measurements, that hung up the UI.
        // Intercepting largest child by height, and use its height as (parent) ReactRootView width fixed that.
        // See for more details https://github.com/wix/react-native-navigation/pull/7096

        val height = titleCoordinates.height()
        return if (height > 0) MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY) else
            heightMeasureSpec
    }

    private fun setSize(coordinates: Coordinates) {
        if (!setSizeByFirstViewGroup(coordinates)) {
            setSizeByAllChildren(rootViewGroup, coordinates)
        }
    }

    private fun setSizeByFirstViewGroup(coordinates: Coordinates): Boolean {
        for (child in rootViewGroup.children) {
            if (child.isShown && child is ViewGroup) {
                val location = IntArray(2)
                child.getLocationOnScreen(location)
                val left = location[0]
                val top = location[1]
                val height = child.bottom - child.top
                val width = child.right - child.left
                if (width < context.resources.displayMetrics.widthPixels) {
                    coordinates.set(left, top, left + width, top + height)
                    return true
                }
            }
        }

        return false
    }

    private fun setSizeByAllChildren(viewGroup: ViewGroup, coordinates: Coordinates) {
        for (child in viewGroup.children) {
            if (child.isShown) {
                if (child is ViewGroup) {
                    val childCoordinates = Coordinates()
                    setSizeByAllChildren(child, childCoordinates)
                    coordinates.left = coordinates.left.coerceAtMost(childCoordinates.left)
                    coordinates.right = coordinates.right.coerceAtLeast(childCoordinates.right)
                    coordinates.top = coordinates.top.coerceAtMost(childCoordinates.top)
                    coordinates.bottom = coordinates.bottom.coerceAtLeast(childCoordinates.bottom)
                } else {
                    val location = IntArray(2)
                    child.getLocationOnScreen(location)
                    val childLeft = location[0]
                    val childRight = childLeft + child.measuredWidth
                    val childTop = location[1]
                    val childBottom = childTop + child.measuredHeight

                    coordinates.left = coordinates.left.coerceAtMost(childLeft)
                    coordinates.right = coordinates.right.coerceAtLeast(childRight)
                    coordinates.top = coordinates.top.coerceAtMost(childTop)
                    coordinates.bottom = coordinates.bottom.coerceAtLeast(childBottom)
                }
            }
        }

    }
}
