package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.res.Resources
import com.reactnativenavigation.utils.UiUtils
import kotlin.math.max

const val DEFAULT_LEFT_MARGIN_DP = 16f
private val defaultMarginPx = UiUtils.dpToPx(Resources.getSystem().displayMetrics, DEFAULT_LEFT_MARGIN_DP).toInt()

typealias TitleLeft = Int
typealias TitleRight = Int

fun resolveTitleBoundsLimit(
        parentWidth: Int,
        titleWidth: Int,
        leftBarWidth: Int,
        rightBarWidth: Int,
        isCenter: Boolean
): Pair<TitleLeft, TitleRight> {

    val rightLimit = parentWidth - rightBarWidth
    if (isCenter) {
        var suggestedLeft: Int = parentWidth / 2 - titleWidth / 2
        var suggestedRight: Int = suggestedLeft + titleWidth

        val leftOverlap = max(leftBarWidth - suggestedLeft, 0)
        val rightOverlap = max(suggestedRight - rightLimit, 0)
        val overlap = max(leftOverlap, rightOverlap)

        if (overlap > 0) {
            suggestedLeft += overlap
            suggestedRight -= overlap
        }

        return suggestedLeft to suggestedRight
    } else {
        return leftBarWidth + defaultMarginPx to rightLimit
    }
}

