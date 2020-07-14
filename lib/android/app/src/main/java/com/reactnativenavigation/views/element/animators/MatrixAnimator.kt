package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.graphics.PointF
import android.graphics.Rect
import android.view.View
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.drawable.ScalingUtils.InterpolatingScaleType
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils
import kotlin.math.max

class MatrixAnimator(from: View, to: View) : PropertyAnimatorCreator<ReactImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ReactImageView, toChild: ReactImageView): Boolean {
        return !ViewUtils.areDimensionsEqual(from, to)
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        with(to as ReactImageView) {
            hierarchy.actualImageScaleType = InterpolatingScaleType(
                    getScaleType(from),
                    getScaleType(to),
                    calculateBounds(from),
                    calculateBounds(to),
                    PointF(from.width / 2f, from.height / 2f),
                    PointF(to.width / 2f, to.height / 2f)
            )

            to.layoutParams.width = max(from.width, to.width)
            to.layoutParams.height = max(from.height, to.height)
            return ObjectAnimator.ofObject(TypeEvaluator<Float> { fraction: Float, _: Any, _: Any ->
                hierarchy.actualImageScaleType?.let {
                    (hierarchy.actualImageScaleType as InterpolatingScaleType?)!!.value = fraction
                    to.invalidate()
                }
                null
            }, 0, 1)
        }
    }

    private fun getScaleType(child: View): ScalingUtils.ScaleType? {
        val scaleType = (child as ReactImageView).hierarchy.actualImageScaleType
        return if (scaleType is InterpolatingScaleType) scaleType.scaleTypeFrom else scaleType
    }

    private fun calculateBounds(view: View) = Rect(0, 0, view.width, view.height)
}
