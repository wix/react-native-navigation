package com.reactnativenavigation.viewcontrollers.modal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.reactnativenavigation.options.FadeAnimation
import com.reactnativenavigation.options.StackAnimationOptions
import com.reactnativenavigation.options.TransitionAnimationOptions
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.utils.ScreenAnimationListener
import com.reactnativenavigation.utils.awaitRender
import com.reactnativenavigation.viewcontrollers.common.BaseAnimator
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.TransitionAnimatorCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

open class ModalAnimator @JvmOverloads constructor(
        context: Context,
        private val transitionAnimatorCreator: TransitionAnimatorCreator = TransitionAnimatorCreator(),
        private val defaultAnimation: StackAnimationOptions = FadeAnimation
) : BaseAnimator(context) {
    val isRunning: Boolean
        get() = runningAnimators.isNotEmpty()

    @VisibleForTesting
    internal val runningAnimators: MutableMap<ViewController<*>, AnimatorSet?> = HashMap()


    open fun show(
            appearing: ViewController<*>,
            disappearing: ViewController<*>?,
            animationOptions: TransitionAnimationOptions,
            listener: ScreenAnimationListener
    ) {
        val set = createShowModalAnimator(appearing, listener)
        runningAnimators[appearing] = set
        if (animationOptions.hasElementTransitions() && disappearing != null) {
            showModalWithElementTransition(appearing, disappearing, animationOptions, set)
        } else {
            showModalWithoutElementTransition(appearing, disappearing, animationOptions, set)
        }

    }

    private fun showModalWithElementTransition(appearing: ViewController<*>, disappearing: ViewController<*>, animationOptions: TransitionAnimationOptions, set: AnimatorSet) {
        GlobalScope.launch(Dispatchers.Main.immediate) {
            appearing.setWaitForRender(Bool(true))
            appearing.view.alpha = 0f
            appearing.awaitRender()
            val appearingFade = if (animationOptions.enter.isFadeAnimation()) animationOptions.enter else defaultAnimation.content.enter
            val transitionAnimators = transitionAnimatorCreator.create(animationOptions, appearingFade, disappearing, appearing)
            set.playTogether(appearingFade.getAnimation(appearing.view), transitionAnimators)
            transitionAnimators.listeners.forEach { animatorListener: Animator.AnimatorListener -> set.addListener(animatorListener) }
            transitionAnimators.removeAllListeners()
            set.start()
        }
    }

    private fun showModalWithoutElementTransition(appearing: ViewController<*>, disappearing: ViewController<*>?, animationOptions: TransitionAnimationOptions, set: AnimatorSet) {
        GlobalScope.launch(Dispatchers.Main.immediate) {
            val appearingAnimation = if (animationOptions.enter.hasValue()) {
                animationOptions.enter.getAnimation(appearing.view)
            } else getDefaultPushAnimation(appearing.view)
            val disappearingAnimation = if (disappearing != null && animationOptions.exit.hasValue()) {
                animationOptions.exit.getAnimation(disappearing.view)
            } else null
            disappearingAnimation?.let {
                set.playTogether(appearingAnimation, disappearingAnimation)
            } ?: set.playTogether(appearingAnimation)
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
                    val appearingAnimation = if (appearing != null && animationOptions.enter.hasValue()) {
                        animationOptions.enter.getAnimation(appearing.view)
                    } else null
                    val disappearingAnimation = if (animationOptions.exit.hasValue()) {
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
                listener.onStart()
            }

            override fun onAnimationCancel(animation: Animator) {
                isCancelled = true
                runningAnimators.remove(appearing)
                listener.onCancel()
            }

            override fun onAnimationEnd(animation: Animator) {
                runningAnimators.remove(appearing)
                if (!isCancelled) listener.onEnd()
            }
        })
        return set
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
                runningAnimators.remove(disappearing)
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
        val disappearFade = if (animationOptions.exit.isFadeAnimation()) animationOptions.exit else defaultAnimation.content.exit
        val transitionAnimators = transitionAnimatorCreator.create(animationOptions, disappearFade, disappearing, appearing)
        set.playTogether(disappearFade.getAnimation(disappearing.view), transitionAnimators)
        transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
        transitionAnimators.removeAllListeners()
    }
}
