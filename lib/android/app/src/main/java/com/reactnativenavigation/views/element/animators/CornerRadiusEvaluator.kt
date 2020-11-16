package com.reactnativenavigation.views.element.animators

import android.animation.TypeEvaluator

class CornerRadiusEvaluator(private val onEvaluate: (Float) -> Unit) : TypeEvaluator<Float> {
    override fun evaluate(ratio: Float, from: Float, to: Float): Float {
        val radius = from + ratio * (to - from)
        onEvaluate(radius)
        return radius
    }
}