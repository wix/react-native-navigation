package com.reactnativenavigation.views.element.animators

import android.animation.TypeEvaluator

class OutlineEvaluator(private val onEvaluate: (ViewOutline) -> Unit) : TypeEvaluator<ViewOutline> {
    override fun evaluate(ratio: Float, from: ViewOutline, to: ViewOutline): ViewOutline {
        return ViewOutline(
                from.width + ratio * (to.width - from.width),
                from.height + ratio * (to.height - from.height),
                from.radius + ratio * (to.radius - from.radius)
        ).apply { onEvaluate(this) }
    }
}