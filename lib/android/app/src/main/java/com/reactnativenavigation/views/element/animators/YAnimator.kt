package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.View.TRANSLATION_Y
import android.view.ViewGroup
import com.facebook.react.views.text.ReactTextView
import com.reactnativenavigation.parse.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils

class YAnimator(from: View, to: View) : PropertyAnimatorCreator<View>(from, to) {
    private val dy: Int

    init {
        val fromXy = ViewUtils.getLocationOnScreen(from)
        val toY = (to.layoutParams as ViewGroup.MarginLayoutParams).topMargin
        dy = fromXy.y - toY
    }

    override fun shouldAnimateProperty(fromChild: View, toChild: View) = dy != 0

    override fun excludedViews() = listOf(ReactTextView::class.java)

    override fun create(options: SharedElementTransitionOptions): Animator {
        to.translationY = dy.toFloat()
        to.pivotY = 0f
        return ObjectAnimator.ofFloat(to, TRANSLATION_Y, dy.toFloat(), 0f)
    }
}