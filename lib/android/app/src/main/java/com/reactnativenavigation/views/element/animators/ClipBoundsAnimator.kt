package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.parse.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.withDuration
import com.reactnativenavigation.utils.withInterpolator
import com.reactnativenavigation.utils.withStartDelay

class ClipBoundsAnimator(from: View, to: View) : PropertyAnimatorCreator<ReactImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ReactImageView, toChild: ReactImageView): Boolean {
        return !ViewUtils.areDimensionsEqual(from, to)
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        val startDrawingRect = Rect(); from.getDrawingRect(startDrawingRect)
        val endDrawingRect = Rect(); to.getDrawingRect(endDrawingRect)
        return ObjectAnimator.ofObject(
                ClipBoundsEvaluator(),
                startDrawingRect,
                endDrawingRect
        )
                .setDuration(options.getDuration())
                .withStartDelay(options.getStartDelay())
                .withInterpolator(options.interpolation.interpolator)
    }

}