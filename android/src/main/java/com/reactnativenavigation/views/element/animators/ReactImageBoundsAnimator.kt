package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.computeInheritedScale
import kotlin.math.roundToInt

class ReactImageBoundsAnimator(from: View, to: View) : PropertyAnimatorCreator<ReactImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ReactImageView, toChild: ReactImageView): Boolean {
        return !ViewUtils.areDimensionsEqual(from, to)
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        val startDrawingRect = Rect().apply { from.getDrawingRect(this) }
        val endDrawingRect = Rect().apply { to.getDrawingRect(this) }

        val (inheritedScaleX, inheritedScaleY) = computeInheritedScale(from)
        startDrawingRect.right = (startDrawingRect.right * inheritedScaleX).roundToInt()
        startDrawingRect.bottom = (startDrawingRect.bottom * inheritedScaleY).roundToInt()

        return ObjectAnimator.ofObject(
                BoundsEvaluator() {
                    with(to as ReactImageView) {
                        drawable.bounds = it
                        drawable.current.bounds = it
                        to.clipBounds = it
                        invalidate()
                    }
                },
                startDrawingRect,
                endDrawingRect
        )
    }
}
