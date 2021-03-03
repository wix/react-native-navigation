package com.reactnativenavigation.hierarchy.root

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.View
import com.reactnativenavigation.options.TransitionAnimationOptions
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import java.util.*

open class RootAnimator {
    private val runningAnimators: MutableMap<ViewController<*>, Animator?> = HashMap()

    open fun setRoot(appearing: ViewController<*>, disappearing: ViewController<*>?, setRoot: TransitionAnimationOptions, onAnimationEnd: Runnable) {
        if (!setRoot.hasValue() || (!setRoot.enter.hasAnimation() && !setRoot.exit.hasAnimation())) {
            appearing.view.visibility = View.VISIBLE
            onAnimationEnd.run()
            return
        }
        appearing.view.visibility = View.INVISIBLE

        val animationSet = createAnimator(appearing, Runnable {
            appearing.view.visibility = View.VISIBLE
            onAnimationEnd.run()
        })

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
    }

    private fun createAnimator(appearing: ViewController<*>, onAnimationEnd: Runnable): AnimatorSet {
        val set = AnimatorSet()
        set.addListener(object : AnimatorListenerAdapter() {
            private var isCancelled = false
            override fun onAnimationStart(animation: Animator) {
                runningAnimators[appearing] = animation
            }

            override fun onAnimationCancel(animation: Animator) {
                isCancelled = true
                onAnimationEnd.run()
            }

            override fun onAnimationEnd(animation: Animator) {
                runningAnimators.remove(appearing)
                if (!isCancelled) onAnimationEnd.run()
            }
        })
        return set
    }

}