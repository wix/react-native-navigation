package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.PointF
import android.graphics.Rect
import android.view.View
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.drawable.ScalingUtils.InterpolatingScaleType
import com.facebook.react.views.image.ImageResizeMode
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils
import kotlin.math.max
import kotlin.math.roundToInt

class ReactImageMatrixAnimator(from: View, to: View) : PropertyAnimatorCreator<ReactImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ReactImageView, toChild: ReactImageView): Boolean {
        return !ViewUtils.areDimensionsEqual(from, to)
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        from as ReactImageView
        with(to as ReactImageView) {
            to.hierarchy.fadeDuration = 0
            val parentScaleX = (from.parent as View).scaleX
            val parentScalyY = (from.parent as View).scaleY

            val fromBounds = calculateBounds(from, parentScaleX, parentScalyY)
            hierarchy.actualImageScaleType = InterpolatingScaleType(
                    getScaleType(from),
                    getScaleType(to),
                    fromBounds,
                    calculateBounds(to),
                    PointF(from.width * parentScaleX / 2f, from.height * parentScalyY / 2f),
                    PointF(to.width / 2f, to.height / 2f)
            )

            to.layoutParams.width = max(from.width, to.width)
            to.layoutParams.height = max(from.height, to.height)
            return ObjectAnimator.ofObject({ fraction: Float, _: Any, _: Any ->
                hierarchy.actualImageScaleType?.let {
                    (hierarchy.actualImageScaleType as? InterpolatingScaleType)?.let {
                        it.value = fraction
                        to.invalidate()
                    }
                }
                null
            }, 0f, 1f)
        }
    }

    private fun getScaleType(child: View): ScalingUtils.ScaleType {
        return getScaleType(
                child as ReactImageView, child.hierarchy.actualImageScaleType ?: ImageResizeMode.defaultValue()
        )
    }

    private fun getScaleType(child: ReactImageView, scaleType: ScalingUtils.ScaleType): ScalingUtils.ScaleType {
        if (scaleType is InterpolatingScaleType) return getScaleType(child, scaleType.scaleTypeTo)
        return scaleType
    }

    private fun calculateBounds(view: View, parentScaleX: Float = 1f, parentScaleY: Float = 1f) =
            Rect(
                    0,
                    0,
                    (view.width * parentScaleX).roundToInt(),
                    (view.height * parentScaleY).roundToInt()
            )
}
