package com.reactnativenavigation.utils

import com.facebook.react.views.image.ReactImageView

fun ReactImageView.getCornerRadius(): Float {
    return hierarchy.roundingParams!!.cornersRadii!!.first()
}