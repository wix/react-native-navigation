package com.reactnativenavigation.views.tooltips

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.reactnativenavigation.views.MaViewTooltip

class TooltipsOverlay(context: Context, id: String) : FrameLayout(context) {

    init {
//        addView(TextView(context).apply {
//            text = id
//            textSize = 18f
//        }, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
//            gravity = Gravity.CENTER
//        })
        z = Float.MAX_VALUE
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

    fun addTooltip(tooltipAnchorView: View, tooltipView: View, gravity: String): MaViewTooltip.TooltipView? {
        //In Order for ReactView to get measured properly it needs to be added to a layout
        //then the component will get started the JS thread will update his measurements
        //then we join it MessageQueue and wait after it finishes its job and move it to tooltip parent
        tooltipView.alpha = 0.0f
        this.addView(tooltipView)
        return when (gravity) {
            "top" -> {
                showTooltip(tooltipView, tooltipAnchorView, MaViewTooltip.Position.TOP)
            }
            "bottom" -> {
                showTooltip(tooltipView, tooltipAnchorView, MaViewTooltip.Position.BOTTOM)
            }
            "left" -> {
                showTooltip(tooltipView, tooltipAnchorView, MaViewTooltip.Position.LEFT)
            }
            "right" -> {
                showTooltip(tooltipView, tooltipAnchorView, MaViewTooltip.Position.RIGHT)
            }
            else -> {
                showTooltip(tooltipView, tooltipAnchorView, MaViewTooltip.Position.TOP)
            }
        }

    }


    private fun showTooltip(
        tooltipView: View,
        tooltipAnchorView: View,
        pos: MaViewTooltip.Position
    ): MaViewTooltip.TooltipView {
        val tooltipViewContainer = MaViewTooltip
            .on(context as Activity, this, tooltipAnchorView)
            .autoHide(false, 5000)
            .clickToHide(false)
            .align(MaViewTooltip.ALIGN.CENTER)
            .padding(0, 0, 0, 0)
            // .margin(0, 0, 0, 0)
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