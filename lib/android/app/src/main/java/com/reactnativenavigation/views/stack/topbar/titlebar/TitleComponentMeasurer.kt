package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.res.Resources
import com.reactnativenavigation.utils.UiUtils
import kotlin.math.max
import kotlin.math.min

const val DEFAULT_LEFT_MARGIN_DP = 16f
internal val DEFAULT_LEFT_MARGIN_PX = UiUtils.dpToPx(Resources.getSystem().displayMetrics, DEFAULT_LEFT_MARGIN_DP).toInt()

typealias TitleLeft = Int
typealias TitleRight = Int

fun resolveTitleBoundsLimit(
        parentWidth: Int,
        titleWidth: Int,
        leftBarWidth: Int,
        rightBarWidth: Int,
        isCenter: Boolean,
        isRTL: Boolean
): Pair<TitleLeft, TitleRight> {
    val resolvedLeftBarWidth = if (isRTL) rightBarWidth else leftBarWidth
    val resolvedRightBarWidth = if (isRTL) leftBarWidth else rightBarWidth
    var suggestedLeft: TitleLeft
    var suggestedRight: TitleRight

    val rightLimit = parentWidth - resolvedRightBarWidth
    if (isCenter) {
        suggestedLeft = parentWidth / 2 - titleWidth / 2
        suggestedRight = suggestedLeft + titleWidth

        val leftOverlap = max(resolvedLeftBarWidth - suggestedLeft, 0)
        val rightOverlap = max(suggestedRight - rightLimit, 0)
        val overlap = max(leftOverlap, rightOverlap)

        if (overlap > 0) {
            suggestedLeft += overlap
            suggestedRight -= overlap
        }
    } else {
        if (isRTL) {
            suggestedRight = rightLimit - DEFAULT_LEFT_MARGIN_PX
            suggestedLeft = max(0,suggestedRight - titleWidth - DEFAULT_LEFT_MARGIN_PX)
        } else {
            suggestedLeft = resolvedLeftBarWidth + DEFAULT_LEFT_MARGIN_PX
            suggestedRight = min(rightLimit - DEFAULT_LEFT_MARGIN_PX, suggestedLeft + titleWidth + DEFAULT_LEFT_MARGIN_PX)
        }
        suggestedLeft = max(0, suggestedLeft)
        suggestedRight = min(parentWidth, suggestedRight)
    }
    return suggestedLeft to suggestedRight
}

fun resolveLeftToolbarBounds(parentWidth: Int,
                             barWidth: Int,
                             isRTL: Boolean): Pair<Int, Int> {
    return if (isRTL) {
        parentWidth - barWidth to parentWidth
    } else {
        0 to barWidth
    }
}

fun resolveRightToolbarBounds(parentWidth: Int,
                              barWidth: Int,
                              isRTL: Boolean): Pair<Int, Int> {
    return resolveLeftToolbarBounds(parentWidth, barWidth, !isRTL)
}
