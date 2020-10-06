package com.reactnativenavigation.viewcontrollers.modal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.FadeAnimation
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.utils.ScreenAnimationListener
import com.reactnativenavigation.utils.awaitRender
import com.reactnativenavigation.viewcontrollers.common.BaseAnimator
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.TransitionAnimatorCreator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

open class ModalAnimator(context: Context?) : BaseAnimator(context) {
    private val runningAnimators: MutableMap<View, Animator?> = HashMap()
    private val transitionAnimatorCreator: TransitionAnimatorCreator = TransitionAnimatorCreator()

    open fun show(appearing: ViewController<*>, disappearing: ViewController<*>, options: Options, listener: ScreenAnimationListener) = GlobalScope.launch {
        val set = createShowAnimator(appearing, listener)
        appearing.setWaitForRender(Bool(true))
        appearing.view.alpha = 0f
        appearing.awaitRender()

        val fade = if (options.animations.push.content.isFadeAnimation()) options.animations.push.content else FadeAnimation().content
        val transitionAnimators = transitionAnimatorCreator.create(options.animations.push, fade, disappearing, appearing)
        set.playTogether(fade.getAnimation(appearing.view), transitionAnimators)
        transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
        transitionAnimators.removeAllListeners()
        set.start()
    }

    open fun dismiss(view: View?, dismiss: AnimationOptions, listener: ScreenAnimationListener) {
        if (runningAnimators.containsKey(view)) {
            runningAnimators[view]!!.cancel()
            listener.onEnd()
            return
        }
        val animator: Animator = dismiss.getAnimation(view!!, getDefaultPopAnimation(view))
        animator.addListener(object : AnimatorListenerAdapter() {
            private var isCancelled = false
            override fun onAnimationStart(animation: Animator) {
                listener.onStart()
            }

            override fun onAnimationCancel(animation: Animator) {
                isCancelled = true
                listener.onCancel()
            }

            override fun onAnimationEnd(animation: Animator) {
                runningAnimators.remove(view)
                if (!isCancelled) listener.onEnd()
            }
        })
        animator.start()
    }


    private fun createShowAnimator(appearing: ViewController<*>, listener: ScreenAnimationListener): AnimatorSet {
        val set = AnimatorSet()
        set.addListener(object : AnimatorListenerAdapter() {
            private var isCancelled = false
            override fun onAnimationStart(animation: Animator) {
                runningAnimators[appearing.view] = set
                listener.onStart()
            }

            override fun onAnimationCancel(animation: Animator) {
                isCancelled = true
                runningAnimators.remove(appearing.view)
                listener.onEnd()
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!isCancelled) {
                    runningAnimators.remove(appearing.view)
                    listener.onEnd()
                }
            }
        })
        return set
    }

    val isRunning: Boolean
        get() = runningAnimators.isNotEmpty()
}