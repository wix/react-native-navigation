package com.reactnativenavigation.views.tooltips

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.reactnativenavigation.views.ViewTooltip

class TooltipsOverlay(context: Context, id: String, debug:Boolean = false) : FrameLayout(context) {

    init {
        if(debug){
            addView(TextView(context).apply {
                text = id
                textSize = 18f
            }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            })
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBackgroundColor(Color.argb(0.25f,0.5f,0f,0f))
            }
        }

        z = Float.MAX_VALUE
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

    fun addTooltip(tooltipAnchorView: View, tooltipView: View, gravity: String): ViewTooltip.TooltipView? {
        //In Order for ReactView to get measured properly it needs to be added to a layout
        //then the component will get started the JS thread will update his measurements
        //then we join it MessageQueue and wait after it finishes its job and move it to tooltip parent
        tooltipView.alpha = 0.0f
        this.addView(tooltipView)
        return when (gravity) {
            "top" -> {
                showTooltip(tooltipView, tooltipAnchorView, ViewTooltip.Position.TOP)
            }
            "bottom" -> {
                showTooltip(tooltipView, tooltipAnchorView, ViewTooltip.Position.BOTTOM)
            }
            "left" -> {
                showTooltip(tooltipView, tooltipAnchorView, ViewTooltip.Position.LEFT)
            }
            "right" -> {
                showTooltip(tooltipView, tooltipAnchorView, ViewTooltip.Position.RIGHT)
            }
            else -> {
                showTooltip(tooltipView, tooltipAnchorView, ViewTooltip.Position.TOP)
            }
        }

    }


    private fun showTooltip(
        tooltipView: View,
        tooltipAnchorView: View,
        pos: ViewTooltip.Position
    ): ViewTooltip.TooltipView {
        val tooltipViewContainer = ViewTooltip
            .on(context as Activity, this, tooltipAnchorView)
            .autoHide(false, 5000)
            .clickToHide(false)
            .align(ViewTooltip.ALIGN.CENTER)
            .padding(0, 0, 0, 0)
            // .margin(0, 0, 0, 0)
//            .customView(tooltipView)
            .distanceWithView(-25)
            .color(Color.WHITE)
            .arrowHeight(25)
            .arrowWidth(25)
            .position(pos)

            .onDisplay {
            }
            .onHide {

            }
            .show()
        this.post {
            removeView(tooltipView)
            tooltipView.alpha = 1.0f
            tooltipViewContainer.setCustomView(tooltipView)
        }
        return tooltipViewContainer
    }

}