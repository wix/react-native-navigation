package com.reactnativenavigation.views.touch

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import androidx.annotation.VisibleForTesting
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.NullBool
import com.reactnativenavigation.viewcontrollers.viewcontroller.IReactView
import com.reactnativenavigation.views.component.ComponentLayout

open class OverlayTouchDelegate(private val component: ComponentLayout, private val reactView: IReactView) {
    var interceptTouchOutside: Bool = NullBool()
    private val hitRect = Rect()
    private val overlayView: View
        get() = if (reactView.asView().childCount > 0) reactView.asView().getChildAt(0) else reactView.asView()

    fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return when (interceptTouchOutside.hasValue() && event.actionMasked == MotionEvent.ACTION_DOWN) {
            true -> handleDown(event)
            false -> component.superOnInterceptTouchEvent(event)
        }
    }

    @VisibleForTesting
    open fun handleDown(event: MotionEvent) = if (isTouchInsideOverlay(event)) component.superOnInterceptTouchEvent(event) else interceptTouchOutside.isFalse

    private fun isTouchInsideOverlay(ev: MotionEvent): Boolean {
        overlayView.getHitRect(hitRect)
        return hitRect.contains(ev.rawX.toInt(), (ev.rawY - component.y).toInt())
    }
}