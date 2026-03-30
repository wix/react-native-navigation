package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.facebook.react.views.view.ReactViewGroup
import com.reactnativenavigation.options.SharedElementTransitionOptions

class ReactViewRotationAnimator(from: View, to: View) : PropertyAnimatorCreator<ReactViewGroup>(from, to) {
    private val fromRotation = from.rotation
    private val toRotation = to.rotation

    override fun shouldAnimateProperty(fromChild: ReactViewGroup, toChild: ReactViewGroup): Boolean {
        return fromRotation != toRotation &&
                fromChild.childCount == 0 &&
                toChild.childCount == 0
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        to.rotation = fromRotation
        to.pivotX = 0f
        to.pivotY = 0f
        return ObjectAnimator.ofFloat(to, View.ROTATION, fromRotation, toRotation)
    }
}