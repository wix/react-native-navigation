package com.reactnativenavigation.utils

import com.facebook.drawee.generic.RoundingParams

class RoundingParams {
    companion object {
        fun fromCornersRadii(radius: Float): RoundingParams =
                RoundingParams.fromCornersRadii(FloatArray(8) { radius })
    }
}