package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.facebook.react.views.view.ReactViewGroup
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.borderRadius

class ReactViewBorderRadiusAnimator(from: View, to: View) : PropertyAnimatorCreator<ReactViewGroup>(from, to) {
    override fun shouldAnimateProperty(fromChild: ReactViewGroup, toChild: ReactViewGroup): Boolean {
        return fromChild.borderRadius != toChild.borderRadius
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        from as ReactViewGroup; to as ReactViewGroup
        return ObjectAnimator.ofObject(
                CornerRadiusEvaluator { to.setBorderRadius(it) },
                from.borderRadius,
                to.borderRadius
        )
    }
}