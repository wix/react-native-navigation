package com.reactnativenavigation.viewcontrollers.navigator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import androidx.core.animation.doOnStart
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.viewcontrollers.common.BaseAnimator
import com.reactnativenavigation.viewcontrollers.splash.SplashViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.TransitionAnimatorCreator
import kotlinx.coroutines.*
import java.lang.Runnable

open class RootAnimator @JvmOverloads constructor(
        context: Context,
        private val transitionAnimatorCreator: TransitionAnimatorCreator = TransitionAnimatorCreator()
) : BaseAnimator(context) {
    open fun setRoot(root: ViewController<*>, options: Options, onAnimationEnd: Runnable) {
        val set = createAnimatorSet(options, root.view, onAnimationEnd)
        if (options.animations.setRoot.hasElementTransitions()) {
            setRootWithElementTransition(root, options, set)
        } else {
            setRootWithoutElementTransitions(root, options, set)
        }
    }

    private fun setRootWithElementTransition(root: ViewController<*>, options: Options, set: AnimatorSet) = GlobalScope.launch(Dispatchers.Main.immediate) {
        val splash = SplashViewController(root.activity)
        if (splash.findSplashView() == null) {
            setRootWithoutElementTransitions(root, options, set)
            return@launch
        }
        root.setWaitForRender(Bool(true))
        root.view.alpha = 0f
        val transitionAnimators = transitionAnimatorCreator.createSetRootAnimator(options.animations.setRoot, splash, root)
        set.playTogether(transitionAnimators)
        set.doOnStart {
            root.view.alpha = 1f
        }
        transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
        transitionAnimators.removeAllListeners()

        root.view.post { set.start() }
    }

    private fun setRootWithoutElementTransitions(root: ViewController<*>, options: Options, set: AnimatorSet) {
        if (options.animations.setRoot.waitForRender.isTrue) {
            root.view.alpha = 0f
            root.addOnAppearedListener {
                root.view.alpha = 1f
                set.playTogether(options.animations.setRoot.getAnimation(root.view))
                set.start()
            }
        } else {
            set.playTogether(options.animations.setRoot.getAnimation(root.view))
            set.start()
        }
    }

    private fun createAnimatorSet(options: Options, root: View, onAnimationEnd: Runnable): AnimatorSet {
        val set = options.animations.setRoot.getAnimation(root)
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onAnimationEnd.run()
            }
        })
        return set
    }
}