package com.reactnativenavigation.views.element.animators

import android.animation.TypeEvaluator
import android.graphics.Rect

class ClipBoundsEvaluator : TypeEvaluator<Rect> {
    private var fromWidth = 0
    private var fromHeight = 0
    private var toWidth = 0
    private var toHeight = 0
    private val result = Rect()

    override fun evaluate(ratio: Float, from: Rect, to: Rect): Rect {
        sync(from, to)
        if (toHeight == fromHeight) {
            result.bottom = toHeight
        } else {
            if (toHeight > fromHeight) {
                result.bottom = (toHeight - (toHeight - fromHeight) * (1 - ratio)).toInt()
            } else {
                result.bottom = (toHeight + (fromHeight - toHeight) * (1 - ratio)).toInt()
            }
        }
        if (toWidth == fromWidth) {
            result.right = toWidth
        } else {
            if (toWidth > fromWidth) {
                result.right = (toWidth - (toWidth - fromWidth) * (1 - ratio)).toInt()
            } else {
                result.right = (toWidth + (fromWidth - toWidth) * (1 - ratio)).toInt()
            }
        }
        return result
    }

    private fun sync(from: Rect, to: Rect) {
        fromWidth = from.right
        fromHeight = from.bottom
        toWidth = to.right
        toHeight = to.bottom
    }
}