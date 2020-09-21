package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils

class ClipBoundsAnimator(from: View, to: View) : PropertyAnimatorCreator<ImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ImageView, toChild: ImageView): Boolean {
        return !ViewUtils.areDimensionsEqual(from, to)
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        val startDrawingRect = Rect().apply { from.getDrawingRect(this) }
        val endDrawingRect = Rect().apply { to.getDrawingRect(this) }
        return ObjectAnimator.ofObject(
                ClipBoundsEvaluator() {
                    to.clipBounds = it
                    to.invalidate()
                },
                startDrawingRect,
                endDrawingRect
        )
    }

}
