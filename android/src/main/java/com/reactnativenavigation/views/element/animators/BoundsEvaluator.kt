package com.reactnativenavigation.views.element.animators

import android.animation.TypeEvaluator
import android.graphics.Rect

class BoundsEvaluator(private val onEvaluate: (Rect) -> Unit) : TypeEvaluator<Rect> {
    private var fromWidth = 0
    private var fromHeight = 0
    private var toWidth = 0
    private var toHeight = 0
    private val result = Rect()

    override fun evaluate(ratio: Float, from: Rect, to: Rect): Rect {
        sync(from, to)
        result.right = (fromWidth + (toWidth - fromWidth) * ratio).toInt()
        result.bottom = (fromHeight + (toHeight - fromHeight) * ratio).toInt()
        onEvaluate(result)
        return result
    }

    private fun sync(from: Rect, to: Rect) {
        fromWidth = from.right
        fromHeight = from.bottom
        toWidth = to.right
        toHeight = to.bottom
    }
}