package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import com.reactnativenavigation.utils.UiUtils
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

internal class TitleComponentMeasurer(context: Context) {

    //TODO: add this to the calculation whenever no right/left bar
    private val defaultMargin = UiUtils.dpToPx(context, DEFAULT_LEFT_MARGIN)
    private var parentWidth: Int = 0
    private var titleWidth: Int = 0
    var leftBarWidth: Int = 0
        private set
    var rightBarWidth: Int = 0
        private set
    private var isCenter: Boolean = false

    fun setRawMeasurements(parentWidth: Int, titleWidth: Int, leftBarWidth: Int, rightBarWidth: Int, isCenter: Boolean) {
        this.parentWidth = parentWidth
        this.titleWidth = titleWidth
        this.leftBarWidth = min(leftBarWidth, spaceLimitPerComponent)
        this.rightBarWidth = min(rightBarWidth, spaceLimitPerComponent)
        this.isCenter = isCenter
    }

    private val spaceLimitPerComponent: Int
        get() = (parentWidth / 3f).roundToInt()

    fun resolveTitleLeft(): Int {
        return if (isCenter) max(parentWidth / 2 - titleWidth / 2, leftBarWidth) else leftBarWidth
    }

    fun resolveTitleRight(): Int {
        return min(parentWidth - rightBarWidth, resolveTitleLeft() + titleWidth)
    }


}