package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.res.Resources
import android.view.View
import com.reactnativenavigation.utils.UiUtils
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

const val DEFAULT_LEFT_MARGIN_DP = 16f
internal val DEFAULT_LEFT_MARGIN_PX = UiUtils.dpToPx(Resources.getSystem().displayMetrics, DEFAULT_LEFT_MARGIN_DP).toInt()

typealias TitleLeft = Int
typealias TitleRight = Int
typealias TitleTop = Int
typealias TitleBottom = Int

fun makeTitleAtMostWidthMeasureSpec(containerWidth: Int, rightBarWidth: Int, leftBarWidth: Int, isCenter: Boolean): Int {
    return if (isCenter) {
        View.MeasureSpec.makeMeasureSpec(containerWidth, View.MeasureSpec.AT_MOST)
    } else {
        View.MeasureSpec.makeMeasureSpec(containerWidth - rightBarWidth - leftBarWidth - 2 * DEFAULT_LEFT_MARGIN_PX, View.MeasureSpec.AT_MOST)
    }
}

fun resolveVerticalTitleBoundsLimit(
        parentHeight: Int,
        titleHeight: Int
): Pair<TitleTop, TitleBottom> {
    return (parentHeight / 2f - titleHeight / 2f).roundToInt() to (parentHeight / 2f + titleHeight / 2f).roundToInt()
}

fun resolveHorizontalTitleBoundsLimit(
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
        val availableSpace = parentWidth - resolvedLeftBarWidth - resolvedRightBarWidth
        if (titleWidth >= availableSpace) {
            return resolvedLeftBarWidth to rightLimit
        } else {
            suggestedLeft = max(parentWidth / 2 - titleWidth / 2, 0)
            suggestedRight = min(suggestedLeft + titleWidth, parentWidth)

            val leftOverlap = max(resolvedLeftBarWidth - suggestedLeft, 0)
            val rightOverlap = max(suggestedRight - rightLimit, 0)
            val overlap = max(leftOverlap, rightOverlap)

            if (overlap > 0) {
                suggestedLeft += overlap
                suggestedRight -= overlap
            }
        }

    } else {
        if (isRTL) {
            suggestedRight = max(resolvedLeftBarWidth + DEFAULT_LEFT_MARGIN_PX, rightLimit - DEFAULT_LEFT_MARGIN_PX)
            suggestedLeft = max(resolvedLeftBarWidth + DEFAULT_LEFT_MARGIN_PX, suggestedRight - titleWidth -
                    DEFAULT_LEFT_MARGIN_PX)
        } else {
            suggestedLeft = min(resolvedLeftBarWidth + DEFAULT_LEFT_MARGIN_PX, parentWidth - DEFAULT_LEFT_MARGIN_PX)
            suggestedRight = min(rightLimit - DEFAULT_LEFT_MARGIN_PX, suggestedLeft + titleWidth + DEFAULT_LEFT_MARGIN_PX)
        }
        suggestedLeft = max(0, suggestedLeft)
        suggestedRight = min(parentWidth, suggestedRight)
    }

    return suggestedLeft to suggestedRight
}

fun resolveLeftButtonsBounds(parentWidth: Int,
                             barWidth: Int,
                             isRTL: Boolean): Pair<Int, Int> {
    return if (isRTL) {
        max(0, parentWidth - barWidth) to parentWidth
    } else {
        0 to min(barWidth, parentWidth)
    }
}

fun resolveRightButtonsBounds(parentWidth: Int,
                              barWidth: Int,
                              isRTL: Boolean): Pair<Int, Int> {
    return resolveLeftButtonsBounds(parentWidth, barWidth, !isRTL)
}
