package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import com.facebook.react.views.text.ReactTextView
import com.facebook.react.views.view.ReactViewBackgroundDrawable
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.*

class BackgroundColorAnimator(from: View, to: View) : PropertyAnimatorCreator<ViewGroup>(from, to) {
    override fun shouldAnimateProperty(fromChild: ViewGroup, toChild: ViewGroup): Boolean {
        return fromChild.background is ReactViewBackgroundDrawable &&
                toChild.background is ReactViewBackgroundDrawable && (fromChild.background as ReactViewBackgroundDrawable).color != (toChild.background as ReactViewBackgroundDrawable).color
    }

    override fun excludedViews() = listOf(ReactTextView::class.java)

    override fun create(options: SharedElementTransitionOptions): Animator {
        val backgroundColorEvaluator = BackgroundColorEvaluator(to.background as ReactViewBackgroundDrawable)
        val fromColor = ColorUtils.colorToLAB(ViewUtils.getBackgroundColor(from))
        val toColor = ColorUtils.colorToLAB(ViewUtils.getBackgroundColor(to))

        backgroundColorEvaluator.evaluate(0f, fromColor, toColor)
        return ObjectAnimator.ofObject(backgroundColorEvaluator, fromColor, toColor)
    }
}