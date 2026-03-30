package com.reactnativenavigation.views.element.animators

import android.animation.TypeEvaluator
import androidx.core.graphics.ColorUtils
import android.graphics.drawable.Drawable
import android.graphics.drawable.ColorDrawable
import com.reactnativenavigation.utils.ColorUtils.*

class BackgroundColorEvaluator(private val background: Drawable) : TypeEvaluator<DoubleArray> {
    private val color = DoubleArray(3)

    override fun evaluate(ratio: Float, from: DoubleArray, to: DoubleArray): DoubleArray {
        ColorUtils.blendLAB(from, to, ratio.toDouble(), color)
        val colorInt = labToColor(color)
        // Try to set color on ColorDrawable if the background is a ColorDrawable
        if (background is ColorDrawable) {
            background.color = colorInt
        }
        return color
    }
}