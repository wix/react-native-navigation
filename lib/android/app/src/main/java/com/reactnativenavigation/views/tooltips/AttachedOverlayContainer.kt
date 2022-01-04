package com.reactnativenavigation.views.tooltips

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.reactnativenavigation.utils.ColorUtils
import com.reactnativenavigation.views.ViewTooltip

class AttachedOverlayContainer(context: Context, id: String, debug: Boolean = false) : FrameLayout(context) {

    init {
        if (debug) {
            addView(TextView(context).apply {
                text = id
                textSize = 18f
            }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            })
            setBackgroundColor(ColorUtils.randomColor(0.25f))
        }

        z = Float.MAX_VALUE
    }

    fun addOverlay(overlayView: View) {
        addView(overlayView)
    }

    fun addAnchoredView(anchorView: View, overlayView: View, gravity: String): ViewTooltip.TooltipView? {
        return when (gravity) {
            "top" -> {
                showTooltip(overlayView, anchorView, ViewTooltip.Position.TOP)
            }
            "bottom" -> {
                showTooltip(overlayView, anchorView, ViewTooltip.Position.BOTTOM)
            }
            "left" -> {
                showTooltip(overlayView, anchorView, ViewTooltip.Position.LEFT)
            }
            "right" -> {
                showTooltip(overlayView, anchorView, ViewTooltip.Position.RIGHT)
            }
            else -> {
                showTooltip(overlayView, anchorView, ViewTooltip.Position.TOP)
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
            .customView(tooltipView)
            .distanceWithView(0)
            .color(Color.WHITE)
            .bubble(false)
            .arrowHeight(0)
            .arrowWidth(0)
            .position(pos)

            .onDisplay {
            }
            .onHide {

            }
            .show()
        return tooltipViewContainer
    }

}