package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.RoundingParams
import com.reactnativenavigation.utils.getCornerRadius
import kotlin.math.min

class ReactImageCornerRadiusAnimator2(from: View, to: View) : PropertyAnimatorCreator<ReactImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ReactImageView, toChild: ReactImageView): Boolean {
        return fromChild.getCornerRadius() != toChild.getCornerRadius()
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        to as ReactImageView; from as ReactImageView

        return ObjectAnimator.ofObject(
                CornerRadiusEvaluator {
                    to.setBorderRadius(it)
                    to.maybeUpdateView()
                },
                from.getCornerRadius(),
                to.getCornerRadius()
        ).apply {
            setInitialBorderRadius(to, from)
            doOnEnd {

            }
        }
    }

    private fun setInitialBorderRadius(to: ReactImageView, from: ReactImageView) {
        to.setFadeDuration(0)
        to.setBorderRadius(from.getCornerRadius())
        to.maybeUpdateView()
    }
}
