package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils
import kotlin.math.roundToInt

class ClipBoundsAnimator(from: View, to: View) : PropertyAnimatorCreator<ImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ImageView, toChild: ImageView): Boolean {
        return !ViewUtils.areDimensionsEqual(from, to)
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        val startDrawingRect = Rect().apply { from.getDrawingRect(this) }
        val endDrawingRect = Rect().apply { to.getDrawingRect(this) }

        val parentScaleX = (from.parent as View).scaleX
        val parentScaleY = (from.parent as View).scaleY
        startDrawingRect.right = (startDrawingRect.right * parentScaleX).roundToInt()
        startDrawingRect.bottom = (startDrawingRect.bottom * parentScaleY).roundToInt()

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
