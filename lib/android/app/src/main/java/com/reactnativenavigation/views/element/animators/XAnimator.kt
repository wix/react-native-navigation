package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.View.TRANSLATION_X
import androidx.core.animation.addListener
import androidx.core.animation.doOnStart
import com.facebook.react.views.text.ReactTextView
import com.reactnativenavigation.parse.SharedElementTransitionOptions
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.withInterpolator
import com.reactnativenavigation.utils.withStartDelay

class XAnimator(from: View, to: View) : PropertyAnimatorCreator<View>(from, to) {
    private val dx: Int

    init {
        val fromXy = ViewUtils.getLocationOnScreen(from)
        val toXy = ViewUtils.getLocationOnScreen(to)
        dx = fromXy.x - toXy.x
        to.pivotX = 0f
    }

    override fun excludedViews() = listOf(ReactTextView::class.java)

    override fun shouldAnimateProperty(fromChild: View, toChild: View) = dx != 0

    override fun create(options: SharedElementTransitionOptions): Animator {
        to.translationX = dx.toFloat()
        return ObjectAnimator
                .ofFloat(to, TRANSLATION_X, dx.toFloat(), 0f)
                .setDuration(options.getDuration())
                .withStartDelay(options.getStartDelay())
                .withInterpolator(options.interpolation.interpolator)
    }
}