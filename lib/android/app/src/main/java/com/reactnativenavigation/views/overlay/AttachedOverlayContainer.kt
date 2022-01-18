package com.reactnativenavigation.views.overlay

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout

class AttachedOverlayContainer(context: Context) : FrameLayout(context) {

    init {
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