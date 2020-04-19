package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.View.TRANSLATION_Y
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.core.animation.doOnStart
import com.facebook.react.views.text.ReactTextView
import com.reactnativenavigation.parse.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.withInterpolator
import com.reactnativenavigation.utils.withStartDelay

class YAnimator(from: View, to: View) : PropertyAnimatorCreator<View>(from, to) {
    private val dy: Int

    init {
        val fromXy = ViewUtils.getLocationOnScreen(from)
        val toY = (to.layoutParams as ViewGroup.MarginLayoutParams).topMargin
        dy = fromXy.y - toY
        to.pivotY = 0f
    }

    override fun shouldAnimateProperty(fromChild: View, toChild: View) = dy != 0

    override fun excludedViews() = listOf(ReactTextView::class.java)

    override fun create(options: SharedElementTransitionOptions): Animator {
        to.translationY = dy.toFloat()
        return ObjectAnimator
                .ofFloat(to, TRANSLATION_Y, dy.toFloat(), 0f)
                .setDuration(options.getDuration())
                .withStartDelay(options.getStartDelay())
                .withInterpolator(options.interpolation.interpolator)
    }
}