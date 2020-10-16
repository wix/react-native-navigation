package com.reactnativenavigation.options.interpolators

import android.animation.TimeInterpolator
import kotlin.math.cos

class DecelerateAccelerateInterpolator : TimeInterpolator {
    override fun getInterpolation(input: Float): Float {
        return (cos((input + 1) * Math.PI) / 2.0f).toFloat() + 0.5f
    }
}