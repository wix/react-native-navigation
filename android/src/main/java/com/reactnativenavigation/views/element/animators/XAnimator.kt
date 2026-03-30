package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.View.TRANSLATION_X
import android.view.ViewGroup
import com.facebook.react.views.text.ReactTextView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils

class XAnimator(from: View, to: View) : PropertyAnimatorCreator<View>(from, to) {
    private val dx: Int

    init {
        val fromXy = ViewUtils.getLocationOnScreen(from)
        val toX = (to.layoutParams as ViewGroup.MarginLayoutParams).leftMargin
        dx = fromXy.x - toX
    }

    override fun excludedViews() = listOf(ReactTextView::class.java)

    override fun shouldAnimateProperty(fromChild: View, toChild: View) = dx != 0

    override fun create(options: SharedElementTransitionOptions): Animator {
        to.translationX = dx.toFloat()
        return ObjectAnimator.ofFloat(to, TRANSLATION_X, dx.toFloat(), 0f)
    }
}