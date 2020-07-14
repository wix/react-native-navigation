package com.reactnativenavigation.views.touch

import android.view.MotionEvent
import androidx.annotation.VisibleForTesting
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.NullBool
import com.reactnativenavigation.react.ReactView
import com.reactnativenavigation.utils.coordinatesInsideView
import com.reactnativenavigation.views.component.ComponentLayout

open class OverlayTouchDelegate(private val component: ComponentLayout, private val reactView: ReactView) {
    var interceptTouchOutside: Bool = NullBool()

    fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return when (interceptTouchOutside.hasValue() && event.actionMasked == MotionEvent.ACTION_DOWN) {
            true -> handleDown(event)
            false -> component.superOnInterceptTouchEvent(event)
        }
    }

    @VisibleForTesting
    open fun handleDown(event: MotionEvent) = when (event.coordinatesInsideView(reactView.getChildAt(0))) {
        true -> component.superOnInterceptTouchEvent(event)
        false -> interceptTouchOutside.isFalse
    }
}