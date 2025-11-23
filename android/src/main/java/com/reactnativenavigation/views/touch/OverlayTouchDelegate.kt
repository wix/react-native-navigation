package com.reactnativenavigation.views.touch

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.NullBool
import com.reactnativenavigation.react.ReactView
import com.reactnativenavigation.utils.coordinatesInsideView
import com.reactnativenavigation.views.component.ComponentLayout
import androidx.core.view.isVisible

open class OverlayTouchDelegate(
    private val component: ComponentLayout,
    private val reactView: ReactView
) {
    var interceptTouchOutside: Bool = NullBool()

    fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return when (interceptTouchOutside.hasValue() && event.actionMasked == MotionEvent.ACTION_DOWN) {
            true -> handleDown(event)
            false -> component.superOnInterceptTouchEvent(event)
        }
    }

    @VisibleForTesting
    open fun handleDown(event: MotionEvent) = when (isInsideView(event)) {
        true -> component.superOnInterceptTouchEvent(event)
        false -> interceptTouchOutside.isFalse
    }

    /**
     * In new architecture, ReactView could have a DebugOverlay as a child that covers the entire screen.
     * We need to check if the touch event is inside the actual React content. So we go over all children
     * of the ReactView and check if the event is inside any of them except the DebugOverlay.
     *
     * Example of ReactView hierarchy:
     * ```
     * ReactView
     * └── ReactSurfaceView
     *     ├── ReactViewGroup
     *     │   └── DebuggingOverlay (covers entire screen)
     *     └── ReactViewGroup (the content we care about)
     * ```
     */
    private fun isInsideView(event: MotionEvent): Boolean {
        val reactViewSurface = this.reactView.getChildAt(0) as ViewGroup
        for (i in 0 until reactViewSurface.childCount) {
            val childItem = reactViewSurface.getChildAt(i)

            if (!debuggingOverlay(childItem) && childItem.isVisible && event.coordinatesInsideView(childItem)) {
                return true
            }
        }
        return false
    }

    private fun debuggingOverlay(childItem: View?): Boolean {
        if (childItem !is ViewGroup) return false
        val firstChild = childItem.getChildAt(0) ?: return false
        return isDebuggingOverlay(firstChild)
    }

    private fun isDebuggingOverlay(view: View): Boolean {
        return try {
            val className = view.javaClass.name
            className == "com.facebook.react.views.debuggingoverlay.DebuggingOverlay"
        } catch (e: Exception) {
            false
        }
    }

}