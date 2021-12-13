package com.reactnativenavigation.views.tooltips

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.github.florent37.viewtooltip.ViewTooltip
import com.reactnativenavigation.utils.UiUtils

class TooltipsOverlay(context: Context, id: String) : FrameLayout(context) {

    var tooltipViewContainer:ViewTooltip.TooltipView? = null
    init {
        addView(TextView(context).apply {
            text = id
            textSize = 18f
        }, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        })
        z = Float.MAX_VALUE
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

    fun addTooltip(tooltipAnchorView: View, tooltipView: View, gravity: String) {
        //TODO this is used temp need to be managed
//        removeAllViews()
        tooltipViewContainer?.close()
        //TODO check for views that is not attached to a parent

//        addView(tooltipView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        //UiUtils.runOnPreDrawOnce(tooltipView) {
            when (gravity) {
                "top" -> {
                    anchorViewTop(tooltipAnchorView, tooltipView)
                }
                "bottom" -> {
                    anchorViewBottom(tooltipAnchorView, tooltipView)
                }
                "left" -> {
                    anchorViewLeft(tooltipAnchorView, tooltipView)
                }
                "right" -> {
                    anchorViewRight(tooltipAnchorView, tooltipView)
                }
            }
      //  }

//        tooltipView.x = centerX.toFloat()
//        tooltipView.y = centerY.toFloat()
    }

    private fun anchorViewRight(tooltipAnchorView: View, tooltipView: View) {
        tooltipViewContainer = ViewTooltip
            .on(context as Activity,this,tooltipAnchorView)
            .autoHide(false, 5000)
            .clickToHide(false)
//             .text("Hello bowbvowebofoewbof ewfo wef owe fo weof owe of oew foewfewfewfoew fwefoewfew\n " +
//                     "fwenfonweofowenfnwe\n fnweofnowebofowebofboewbfobew")
            .align(ViewTooltip.ALIGN.CENTER)
            .distanceWithView(0)
            .position(ViewTooltip.Position.RIGHT)
            .customView(tooltipView)
            .onDisplay({

            })
            .onHide({

            })
            .show()
        UiUtils.runOnMeasured(tooltipView){
            tooltipViewContainer?.setCustomView(tooltipView)
        }
    }

    private fun anchorViewLeft(tooltipAnchorView: View, tooltipView: View) {
        tooltipViewContainer = ViewTooltip
            .on(context as Activity,this,tooltipAnchorView)
            .autoHide(false, 5000)
            .clickToHide(false)
//             .text("Hello bowbvowebofoewbof ewfo wef owe fo weof owe of oew foewfewfewfoew fwefoewfew\n " +
//                     "fwenfonweofowenfnwe\n fnweofnowebofowebofboewbfobew")
            .align(ViewTooltip.ALIGN.CENTER)
            .distanceWithView(0)
            .position(ViewTooltip.Position.LEFT)
            .customView(tooltipView)
            .onDisplay({

            })
            .onHide({

            })
            .show()
        UiUtils.runOnMeasured(tooltipView){
            tooltipViewContainer?.setCustomView(tooltipView)
        }
    }

    private fun anchorViewBottom(tooltipAnchorView: View, tooltipView: View) {
        tooltipViewContainer = ViewTooltip
            .on(context as Activity,this,tooltipAnchorView)
            .autoHide(false, 5000)
            .clickToHide(false)
//             .text("Hello bowbvowebofoewbof ewfo wef owe fo weof owe of oew foewfewfewfoew fwefoewfew\n " +
//                     "fwenfonweofowenfnwe\n fnweofnowebofowebofboewbfobew")
            .align(ViewTooltip.ALIGN.CENTER)
            .distanceWithView(0)
            .position(ViewTooltip.Position.BOTTOM)
            .customView(tooltipView)
            .onDisplay({

            })
            .onHide({

            })
            .show()
        UiUtils.runOnMeasured(tooltipView){
            tooltipViewContainer?.setCustomView(tooltipView)
        }
    }

    private fun anchorViewTop(tooltipAnchorView: View, tooltipView: View) {


         tooltipViewContainer = ViewTooltip
             .on(context as Activity,this,tooltipAnchorView)
            .autoHide(false, 5000)
            .clickToHide(false)
//             .text("Hello bowbvowebofoewbof ewfo wef owe fo weof owe of oew foewfewfewfoew fwefoewfew\n " +
//                     "fwenfonweofowenfnwe\n fnweofnowebofowebofboewbfobew")
            .align(ViewTooltip.ALIGN.CENTER)
             .distanceWithView(100)
            .position(ViewTooltip.Position.TOP)
            .customView(FrameLayout(context).apply {
                this.addView(tooltipView)
            })
            .onDisplay({

            })
            .onHide({

            })
            .show()
        UiUtils.runOnMeasured(tooltipView){
            tooltipViewContainer?.setCustomView(tooltipView)
        }
    }
}