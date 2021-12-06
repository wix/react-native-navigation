package com.reactnativenavigation.views.tooltips

import android.content.Context
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

class TooltipsOverlay(context: Context, id:String) : FrameLayout(context) {

    init {
        addView(TextView(context).apply {
            text = id
            textSize = 18f
        }, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            gravity=Gravity.CENTER
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }
}