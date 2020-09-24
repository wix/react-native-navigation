package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.graphics.PointF
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils
import java.lang.RuntimeException
import kotlin.math.max

class SplashImageAnimator(from: View, to: View) : PropertyAnimatorCreator<ImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ImageView, toChild: ImageView): Boolean {
        return !ViewUtils.areDimensionsEqual(from, to) && from !is ReactImageView
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        with(to as ReactImageView) {
            hierarchy.actualImageScaleType = ScalingUtils.InterpolatingScaleType(
                    getScaleType(from as ImageView),
                    getScaleType(this),
                    calculateBounds(from),
                    calculateBounds(to),
                    PointF(from.width / 2f, from.height / 2f),
                    PointF(to.width / 2f, to.height / 2f)
            )

            to.layoutParams.width = max(from.width, to.width)
            to.layoutParams.height = max(from.height, to.height)
            return ObjectAnimator.ofObject(TypeEvaluator<Float> { fraction: Float, _: Any, _: Any ->
                hierarchy.actualImageScaleType?.let {
                    (hierarchy.actualImageScaleType as ScalingUtils.InterpolatingScaleType?)!!.value = fraction
                    to.invalidate()
                }
                null
            }, 0, 1)
        }
    }

    private fun getScaleType(child: ImageView): ScalingUtils.ScaleType {
        return when (child.scaleType) {
            ImageView.ScaleType.CENTER -> ScalingUtils.ScaleType.CENTER
            ImageView.ScaleType.CENTER_CROP -> ScalingUtils.ScaleType.CENTER_CROP
            ImageView.ScaleType.CENTER_INSIDE -> ScalingUtils.ScaleType.CENTER_INSIDE
            ImageView.ScaleType.FIT_CENTER -> ScalingUtils.ScaleType.FIT_CENTER
            ImageView.ScaleType.FIT_END -> ScalingUtils.ScaleType.FIT_END
            ImageView.ScaleType.FIT_START -> ScalingUtils.ScaleType.FIT_START
            ImageView.ScaleType.FIT_XY -> ScalingUtils.ScaleType.FIT_XY
            else -> throw RuntimeException("Unsupported ScaleType ${child.scaleType}")
        }
    }

    private fun getScaleType(child: ReactImageView): ScalingUtils.ScaleType {
        return getScaleType(child, child.hierarchy.actualImageScaleType!!)
    }

    private fun getScaleType(child: ReactImageView, scaleType: ScalingUtils.ScaleType): ScalingUtils.ScaleType {
        if (scaleType is ScalingUtils.InterpolatingScaleType) return getScaleType(child, scaleType.scaleTypeTo )
        return scaleType
    }

    private fun calculateBounds(view: View) = Rect(0, 0, view.width, view.height)
}