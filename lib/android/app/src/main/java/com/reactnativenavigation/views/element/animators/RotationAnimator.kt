package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.parse.SharedElementTransitionOptions

class RotationAnimator(from: View, to: View) : PropertyAnimatorCreator<ReactImageView>(from, to) {
    private val fromRotation = from.rotation
    private val toRotation = to.rotation

    override fun shouldAnimateProperty(fromChild: ReactImageView, toChild: ReactImageView): Boolean {
        return fromRotation != toRotation
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        to.rotation = fromRotation
        to.pivotX = 0f
        to.pivotY = 0f
        return ObjectAnimator.ofFloat(to, View.ROTATION, fromRotation, toRotation)
    }
}