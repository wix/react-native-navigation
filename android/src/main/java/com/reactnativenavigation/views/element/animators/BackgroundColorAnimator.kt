package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.graphics.drawable.ColorDrawable
import com.facebook.react.views.text.ReactTextView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.*

class BackgroundColorAnimator(from: View, to: View) : PropertyAnimatorCreator<ViewGroup>(from, to) {
    override fun shouldAnimateProperty(fromChild: ViewGroup, toChild: ViewGroup): Boolean {
        // Only animate if both backgrounds are ColorDrawable and colors are different
        val fromBg = fromChild.background
        val toBg = toChild.background
        return fromBg is ColorDrawable &&
                toBg is ColorDrawable &&
                fromBg.color != toBg.color
    }

    override fun excludedViews() = listOf(ReactTextView::class.java)

    override fun create(options: SharedElementTransitionOptions): Animator {
        val toBackground = to.background
        if (toBackground !is ColorDrawable) {
            // Fallback: return a no-op animator if background is not a ColorDrawable but this should happen.
            // Just for code safety
            return ObjectAnimator.ofFloat(to, "alpha", 1f, 1f).apply {
                duration = 0
            }
        }

        val backgroundColorEvaluator = BackgroundColorEvaluator(toBackground)
        val fromColor = try {
            ColorUtils.colorToLAB(ViewUtils.getBackgroundColor(from))
        } catch (e: Exception) {
            // Fallback to transparent if we can't get the color
            ColorUtils.colorToLAB(android.graphics.Color.TRANSPARENT)
        }
        val toColor = ColorUtils.colorToLAB(toBackground.color)

        backgroundColorEvaluator.evaluate(0f, fromColor, toColor)
        return ObjectAnimator.ofObject(backgroundColorEvaluator, fromColor, toColor)
    }
}
