package com.reactnativenavigation.viewcontrollers.modal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import com.reactnativenavigation.options.FadeAnimation
import com.reactnativenavigation.options.NestedAnimationsOptions
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

open class ModalAnimator(context: Context?) : BaseAnimator(context) {
    private val runningAnimators: MutableMap<View, Animator?> = HashMap()
    private val transitionAnimatorCreator: TransitionAnimatorCreator = TransitionAnimatorCreator()

    open fun show(appearing: ViewController<*>, disappearing: ViewController<*>, showModal: NestedAnimationsOptions, listener: ScreenAnimationListener) = GlobalScope.launch(Dispatchers.Main.immediate) {
        val set = createShowAnimator(appearing, listener)
        appearing.setWaitForRender(Bool(true))
        appearing.view.alpha = 0f
        appearing.awaitRender()

        val fade = if (showModal.content.isFadeAnimation()) showModal else FadeAnimation()
        val transitionAnimators = transitionAnimatorCreator.create(showModal, fade.content, disappearing, appearing)
        set.playTogether(fade.content.getAnimation(appearing.view), transitionAnimators)
        transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
        transitionAnimators.removeAllListeners()

        set.start()
    }

    open fun dismiss(appearing: ViewController<*>, disappearing: ViewController<*>, dismissModal: NestedAnimationsOptions, listener: ScreenAnimationListener) = GlobalScope.launch(Dispatchers.Main.immediate) {
        val set = createShowAnimator(appearing, listener)
        appearing.setWaitForRender(Bool(true))
        appearing.view.alpha = 0f
        appearing.awaitRender()

        val fade = if (dismissModal.content.isFadeAnimation()) dismissModal else FadeAnimation()
        val transitionAnimators = transitionAnimatorCreator.create(dismissModal, fade.content, disappearing, appearing)
        set.playTogether(fade.content.getAnimation(disappearing.view), transitionAnimators)
        transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
        transitionAnimators.removeAllListeners()

        set.start()
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