package com.reactnativenavigation.views.stack.topbar.titlebar

import kotlin.math.max

fun resolveTitleBoundsLimit(
        defaultStartMargin: Int,
        parentWidth: Int,
        titleWidth: Int,
        leftBarWidth: Int,
        rightBarWidth: Int,
        isCenter: Boolean
): Pair<Int, Int> {

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
        return leftBarWidth + defaultStartMargin to rightLimit
    }
}
