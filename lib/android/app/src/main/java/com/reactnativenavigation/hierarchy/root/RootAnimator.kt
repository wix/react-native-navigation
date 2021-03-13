package com.reactnativenavigation.hierarchy.root

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.View
import com.reactnativenavigation.options.TransitionAnimationOptions
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

open class RootAnimator {

    open fun setRoot(appearing: ViewController<*>, disappearing: ViewController<*>?, setRoot: TransitionAnimationOptions, onAnimationEnd: ()->Unit) {
        appearing.view.visibility = View.VISIBLE

        if (!setRoot.hasValue() || (!setRoot.enter.hasAnimation() && !setRoot.exit.hasAnimation())) {
            onAnimationEnd()
            return
        }

        val animationSet = createAnimator(onAnimationEnd)

        val appearingAnimation = if (setRoot.enter.hasAnimation()) {
            setRoot.enter.getAnimation(appearing.view)
        } else null
        val disappearingAnimation = if (disappearing != null && setRoot.exit.hasAnimation()) {
            setRoot.exit.getAnimation(disappearing.view)
        } else null

        when {
            appearingAnimation != null && disappearingAnimation != null -> animationSet.playTogether(appearingAnimation, disappearingAnimation)
            appearingAnimation != null -> animationSet.play(appearingAnimation)
            disappearingAnimation != null -> animationSet.play(disappearingAnimation)
        }
        animationSet.start()
    }

    private fun createAnimator(onAnimationEnd: () -> Unit): AnimatorSet {
        val set = AnimatorSet()
        set.addListener(object : AnimatorListenerAdapter() {
            private var isCancelled = false
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationCancel(animation: Animator) {
                isCancelled = true
                onAnimationEnd()
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!isCancelled) onAnimationEnd()
            }
        })
        return set
    }

}