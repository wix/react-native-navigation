package com.reactnativenavigation.viewcontrollers.modal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import com.reactnativenavigation.viewcontrollers.common.BaseAnimator
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.FadeAnimation
import com.reactnativenavigation.utils.ScreenAnimationListener
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.TransitionAnimatorCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

open class ModalAnimator @JvmOverloads constructor(
        context: Context,
        private val transitionAnimatorCreator: TransitionAnimatorCreator = TransitionAnimatorCreator()
) : BaseAnimator(context) {
    val isRunning: Boolean
        get() = runningAnimators.isNotEmpty()

    private val runningAnimators: MutableMap<ViewController<*>, Animator?> = HashMap()

    open fun show(appearing: ViewController<*>, disappearing: ViewController<*>?, show: AnimationOptions, listener: ScreenAnimationListener) {
        GlobalScope.launch(Dispatchers.Main.immediate) {
            val set = createShowModalAnimator(appearing, listener)
            if (show.hasElementTransitions() && disappearing != null) {
                setupShowModalWithSharedElementTransition(disappearing, appearing, show, set)
            } else {
                set.playTogether(show.getAnimation(appearing.view, getDefaultPushAnimation(appearing.view)))
            }
            set.start()
        }
    }

    open fun dismiss(appearing: ViewController<*>?, disappearing: ViewController<*>, dismiss: AnimationOptions, listener: ScreenAnimationListener) {
        GlobalScope.launch(Dispatchers.Main.immediate) {
            if (runningAnimators.containsKey(disappearing)) {
                runningAnimators[disappearing]?.cancel()
                listener.onEnd()
            } else {
                val set = createDismissAnimator(disappearing, listener)
                if (dismiss.hasElementTransitions() && appearing != null) {
                    setupDismissAnimationWithSharedElementTransition(disappearing, appearing, dismiss, set)
                } else {
                    set.play(dismiss.getAnimation(disappearing.view, getDefaultPopAnimation(disappearing.view)))
                }
                set.start()
            }
        }
    }

    private fun createShowModalAnimator(appearing: ViewController<*>, listener: ScreenAnimationListener): AnimatorSet {
        val set = AnimatorSet()
        set.addListener(object : AnimatorListenerAdapter() {
            private var isCancelled = false
            override fun onAnimationStart(animation: Animator) {
                runningAnimators[appearing] = animation
                listener.onStart()
            }

            override fun onAnimationCancel(animation: Animator) {
                isCancelled = true
                listener.onCancel()
            }

            override fun onAnimationEnd(animation: Animator) {
                runningAnimators.remove(appearing)
                if (!isCancelled) listener.onEnd()
            }
        })
        return set
    }

    private suspend fun setupShowModalWithSharedElementTransition(
            disappearing: ViewController<*>,
            appearing: ViewController<*>,
            show: AnimationOptions,
            set: AnimatorSet
    ) {
        val fade = if (show.isFadeAnimation()) show else FadeAnimation().content
        val transitionAnimators = transitionAnimatorCreator.create(show, fade, disappearing, appearing)
        set.playTogether(fade.getAnimation(appearing.view), transitionAnimators)
        transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
        transitionAnimators.removeAllListeners()
    }

    private fun createDismissAnimator(disappearing: ViewController<*>, listener: ScreenAnimationListener): AnimatorSet {
        val set = AnimatorSet()
        set.addListener(object : AnimatorListenerAdapter() {
            private var isCancelled = false
            override fun onAnimationStart(animation: Animator) {
                listener.onStart()
            }

            override fun onAnimationCancel(animation: Animator) {
                isCancelled = true
                listener.onCancel()
            }

            override fun onAnimationEnd(animation: Animator) {
                runningAnimators.remove(disappearing)
                if (!isCancelled) listener.onEnd()
            }
        })
        return set
    }

    private suspend fun setupDismissAnimationWithSharedElementTransition(
            disappearing: ViewController<*>,
            appearing: ViewController<*>,
            dismiss: AnimationOptions,
            set: AnimatorSet
    ) {
        val fade = if (dismiss.isFadeAnimation()) dismiss else FadeAnimation(true).content
        val transitionAnimators = transitionAnimatorCreator.create(dismiss, fade, disappearing, appearing)
        set.playTogether(fade.getAnimation(disappearing.view), transitionAnimators)
        transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
        transitionAnimators.removeAllListeners()
    }
}
