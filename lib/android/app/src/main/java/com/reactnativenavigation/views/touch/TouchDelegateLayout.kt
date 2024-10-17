package com.reactnativenavigation.views.touch

import android.view.MotionEvent

interface TouchDelegateLayout {
    fun superOnInterceptTouchEvent(event: MotionEvent): Boolean
}
