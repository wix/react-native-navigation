package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.graphics.Color
import androidx.constraintlayout.widget.Barrier
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.utils.CompatUtils
import com.reactnativenavigation.utils.UiUtils

const val DEFAULT_LEFT_MARGIN = 16

class MainToolBar(context: Context) : ConstraintLayout(context) {
    val leftBarTitleBarBarrierID = CompatUtils.generateViewId()
    val titleBarRightBarBarrierID = CompatUtils.generateViewId()
    val titleBar: TitleBar = TitleBar(context).apply {
        this.id = CompatUtils.generateViewId()
        setBackgroundColor(Color.BLUE)
    }
    val leftButtonsBar = LeftButtonsBar(context).apply {
        this.id = CompatUtils.generateViewId()
        setBackgroundColor(Color.RED)

    }
    val rightButtonsBar: RightButtonsBar = RightButtonsBar(context).apply {
        this.id = CompatUtils.generateViewId()
        setBackgroundColor(Color.GREEN)

    }

    init {
        this.setBackgroundColor(Color.YELLOW)

        this.addView(leftButtonsBar, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        this.addView(titleBar, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        this.addView(rightButtonsBar, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        val constraintSet = ConstraintSet().also { it.clone(this) }

        constraintSet.setHorizontalBias(leftButtonsBar.id, 0f)
        constraintSet.setHorizontalBias(rightButtonsBar.id, 0f)
        constraintSet.setHorizontalBias(titleBar.id, 0f)

        constraintSet.createBarrier(leftBarTitleBarBarrierID, Barrier.END, 0, leftButtonsBar.id)
        constraintSet.createBarrier(titleBarRightBarBarrierID, Barrier.START, 0, rightButtonsBar.id)

        constraintSet.connect(leftButtonsBar.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(leftButtonsBar.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(leftButtonsBar.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)

        constraintSet.connect(titleBar.id, ConstraintSet.START, leftBarTitleBarBarrierID, ConstraintSet.END)
        constraintSet.connect(titleBar.id, ConstraintSet.END, titleBarRightBarBarrierID, ConstraintSet.START)
        constraintSet.connect(titleBar.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(titleBar.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(rightButtonsBar.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(rightButtonsBar.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(rightButtonsBar.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.setMargin(titleBar.id, ConstraintSet.START, UiUtils.dpToPx(context, DEFAULT_LEFT_MARGIN))

        constraintSet.applyTo(this)
    }


    fun setTitleBarAlignment(alignment: Alignment) {
        val constraintSet = ConstraintSet().also { it.clone(this) }
        val bias = if (alignment == Alignment.Center) 0.5f else 0f;
        val margin = if (alignment == Alignment.Center) 0 else UiUtils.dpToPx(context, DEFAULT_LEFT_MARGIN)
        constraintSet.setHorizontalBias(titleBar.id, bias)
        constraintSet.setMargin(titleBar.id, ConstraintSet.START, margin)
        constraintSet.applyTo(this)
    }


}