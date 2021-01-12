package com.reactnativenavigation.hierarchy.root

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

open class RootAnimator {
    open fun setRoot(root: ViewController<*>, setRoot: AnimationOptions, onAnimationEnd: Runnable) {
        root.view.visibility = View.INVISIBLE
        val set = setRoot.getAnimation(root.view)
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                root.view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                onAnimationEnd.run()
            }
        })
        set.start()
    }
}