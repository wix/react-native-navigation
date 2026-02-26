package com.reactnativenavigation.views.overlay

import android.content.Context
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.reactnativenavigation.views.component.ComponentLayout

/**
 * Custom CoordinatorLayout that checks child overlays' interceptTouchOutside
 * settings and passes touches through when appropriate.
 * 
 * Fixes issues #4953 and #7674 where overlays with interceptTouchOutside: false
 * were still blocking touches to underlying views.
 */
class OverlayContainer(context: Context) : CoordinatorLayout(context) {
    
    private var shouldPassThroughTouch = false
    
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Only check on ACTION_DOWN to avoid interfering with gesture sequences
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            shouldPassThroughTouch = true
            
            // Check if any child overlay wants to intercept this touch
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child is ComponentLayout) {
                    // Delegate to ComponentLayout's onInterceptTouchEvent
                    // which uses OverlayTouchDelegate to check interceptTouchOutside flag
                    if (child.onInterceptTouchEvent(ev)) {
                        // At least one overlay wants to handle this touch
                        shouldPassThroughTouch = false
                        return true
                    }
                }
            }
            // No overlay wants to intercept - don't block the touch
            return false
        }
        // For non-ACTION_DOWN events, use default behavior
        return super.onInterceptTouchEvent(ev)
    }
    
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // For ACTION_DOWN, check if we should pass through
        if (ev.actionMasked == MotionEvent.ACTION_DOWN && shouldPassThroughTouch) {
            // No overlay wants this touch - let it fall through to views below
            // Return false so the touch system doesn't consider it consumed
            return false
        }
        // Either it's not ACTION_DOWN or an overlay wants to handle it
        return super.dispatchTouchEvent(ev)
    }
}
