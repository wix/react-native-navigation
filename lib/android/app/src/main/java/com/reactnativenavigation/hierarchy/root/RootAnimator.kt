package com.reactnativenavigation.hierarchy.root

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.View
import com.reactnativenavigation.options.TransitionAnimationOptions
import com.reactnativenavigation.utils.ScreenAnimationListener
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import java.util.*

open class RootAnimator {
    private val runningAnimators: MutableMap<ViewController<*>, Animator?> = HashMap()

    open fun setRoot(appearing: ViewController<*>, disappearing: ViewController<*>?, setRoot: TransitionAnimationOptions, onAnimationEnd: Runnable) {
        appearing.view.visibility = View.INVISIBLE
        val set = setRoot.enter.getAnimation(appearing.view)
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                appearing.view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                onAnimationEnd.run()
            }
        })
        set.start()
    }
}