package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.graphics.Rect
import android.view.View
import androidx.core.animation.addListener
import androidx.core.animation.doOnStart
import com.facebook.drawee.drawable.ScalingUtils.InterpolatingScaleType
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.parse.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.withStartDelay

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
                    calculateBounds(to)
            )

            return ObjectAnimator
                    .ofObject(TypeEvaluator<Float> { fraction: Float, _: Any, _: Any ->
                        hierarchy.actualImageScaleType?.let {
                            (hierarchy.actualImageScaleType as InterpolatingScaleType?)!!.value = fraction
                            to.invalidate()
                        }
                        null
                    }, 0, 1)
                    .setDuration(options.getDuration())
                    .withStartDelay(options.getStartDelay())
        }
    }

    private fun getScaleType(child: View) = (child as ReactImageView).hierarchy.actualImageScaleType

    private fun calculateBounds(view: View): Rect = Rect(0, 0, view.width, view.height)
}
