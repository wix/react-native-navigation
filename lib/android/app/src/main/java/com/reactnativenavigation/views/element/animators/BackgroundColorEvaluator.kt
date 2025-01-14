@file:OptIn(UnstableReactNativeAPI::class)

package com.reactnativenavigation.views.element.animators

import android.animation.TypeEvaluator
import androidx.core.graphics.ColorUtils
import com.facebook.react.common.annotations.UnstableReactNativeAPI
import com.facebook.react.uimanager.drawable.CSSBackgroundDrawable

class BackgroundColorEvaluator(private val background: CSSBackgroundDrawable) : TypeEvaluator<DoubleArray> {
    private val color = DoubleArray(3)

    override fun evaluate(ratio: Float, from: DoubleArray, to: DoubleArray): DoubleArray {
        ColorUtils.blendLAB(from, to, ratio.toDouble(), color)
        background.color = com.reactnativenavigation.utils.ColorUtils.labToColor(color)
        return color
    }
}