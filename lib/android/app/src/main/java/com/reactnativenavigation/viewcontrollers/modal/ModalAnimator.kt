package com.reactnativenavigation.viewcontrollers.modal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import com.reactnativenavigation.options.FadeInAnimation
import com.reactnativenavigation.options.FadeOutAnimation
import com.reactnativenavigation.options.TransitionAnimationOptions
import com.reactnativenavigation.utils.ScreenAnimationListener
import com.reactnativenavigation.viewcontrollers.common.BaseAnimator
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

    open fun show(
            appearing: ViewController<*>,
            disappearing: ViewController<*>?,
            animationOptions: TransitionAnimationOptions,
            listener: ScreenAnimationListener
    ) {
        GlobalScope.launch(Dispatchers.Main.immediate) {
            val set = createShowModalAnimator(appearing, listener)
            if (animationOptions.hasElementTransitions() && disappearing != null) {
                setupShowModalWithSharedElementTransition(disappearing, appearing, animationOptions, set)
            } else {
                val appearingAnimation = if (animationOptions.enter.hasValue()) {
                    animationOptions.enter.getAnimation(appearing.view)
                } else getDefaultPushAnimation(appearing.view)
                val disappearingAnimation = if (disappearing != null && animationOptions.exit.hasValue()) {
                    animationOptions.exit.getAnimation(disappearing.view)
                } else null
                disappearingAnimation?.let {
                    set.playTogether(appearingAnimation, disappearingAnimation)
                } ?: set.playTogether(appearingAnimation)
            }
            set.start()
        }
    }

    open fun dismiss(appearing: ViewController<*>?, disappearing: ViewController<*>, animationOptions: TransitionAnimationOptions, listener: ScreenAnimationListener) {
        GlobalScope.launch(Dispatchers.Main.immediate) {
            if (runningAnimators.containsKey(disappearing)) {
                runningAnimators[disappearing]?.cancel()
                listener.onEnd()
            } else {
                val set = createDismissAnimator(disappearing, listener)
                if (animationOptions.hasElementTransitions() && appearing != null) {
                    setupDismissAnimationWithSharedElementTransition(disappearing, appearing, animationOptions, set)
                } else {
                    val appearingAnimation = if (appearing != null&&animationOptions.enter.hasValue()) {
                        animationOptions.enter.getAnimation(appearing.view)
                    } else null
                    val disappearingAnimation = if ( animationOptions.exit.hasValue()) {
                        animationOptions.exit.getAnimation(disappearing.view)
                    } else getDefaultPopAnimation(disappearing.view)
                    appearingAnimation?.let {
                        set.playTogether(appearingAnimation, disappearingAnimation)
                    } ?: set.playTogether(disappearingAnimation)
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
            show: TransitionAnimationOptions,
            set: AnimatorSet
    ) {
        val fade = if (show.enter.isFadeAnimation()) show.enter else FadeInAnimation().content.enter
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
            animationOptions: TransitionAnimationOptions,
            set: AnimatorSet
    ) {
        val fade = if (animationOptions.exit.isFadeAnimation()) animationOptions.exit else FadeOutAnimation().content.exit
        val transitionAnimators = transitionAnimatorCreator.create(animationOptions, fade, disappearing, appearing)
        set.playTogether(fade.getAnimation(disappearing.view), transitionAnimators)
        transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
        transitionAnimators.removeAllListeners()
    }
}
