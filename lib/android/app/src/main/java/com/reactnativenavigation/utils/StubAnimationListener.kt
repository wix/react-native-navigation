package com.reactnativenavigation.utils

import android.animation.Animator

open class StubAnimationListener: Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator) {}
    override fun onAnimationEnd(animation: Animator) {}
    override fun onAnimationCancel(animation: Animator) {}
    override fun onAnimationRepeat(animation: Animator) {}

    companion object {
        @JvmStatic
        fun onAnimatorEnd(onEnd: (animation: Animator) -> Unit) = object: StubAnimationListener() {
            override fun onAnimationEnd(animation: Animator) {
                onEnd(animation)
            }
        }
    }
}
