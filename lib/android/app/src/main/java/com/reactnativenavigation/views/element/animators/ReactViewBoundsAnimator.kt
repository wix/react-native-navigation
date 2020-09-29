package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import androidx.core.animation.doOnEnd
import com.facebook.react.views.view.ReactViewGroup
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.areDimensionsWithInheritedScaleEqual
import com.reactnativenavigation.utils.computeInheritedScale
import kotlin.math.roundToInt

class ReactViewBoundsAnimator(from: View, to: View) : PropertyAnimatorCreator<ReactViewGroup>(from, to) {
    override fun shouldAnimateProperty(fromChild: ReactViewGroup, toChild: ReactViewGroup): Boolean {
        return !areDimensionsWithInheritedScaleEqual(from, to) &&
                fromChild.childCount == 0 &&
                toChild.childCount == 0
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        val originalToWidth = to.width
        val originalToHeight = to.height
        val (inheritedScaleX, inheritedScaleY) = computeInheritedScale(from)

        to.layoutParams.width = (from.width * inheritedScaleX).roundToInt()
        to.layoutParams.height = (from.height * inheritedScaleY).roundToInt()

        val startDrawingRect = Rect(from.background.bounds)
        val endDrawingRect = Rect(to.background.bounds)
        startDrawingRect.right = (startDrawingRect.right * inheritedScaleX).roundToInt()
        startDrawingRect.bottom = (startDrawingRect.bottom * inheritedScaleY).roundToInt()

        return ObjectAnimator.ofObject(
                BoundsEvaluator() { to.background.bounds = it },
                startDrawingRect,
                endDrawingRect
        ).apply {
            doOnEnd {
                to.layoutParams.width = originalToWidth
                to.layoutParams.height = originalToHeight
            }
        }
    }
}