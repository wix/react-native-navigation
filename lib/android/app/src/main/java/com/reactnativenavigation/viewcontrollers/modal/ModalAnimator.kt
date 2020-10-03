package com.reactnativenavigation.viewcontrollers.modal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.utils.ScreenAnimationListener
import com.reactnativenavigation.viewcontrollers.common.BaseAnimator
import java.util.*

open class ModalAnimator(context: Context?) : BaseAnimator(context) {
    private val runningAnimators: MutableMap<View, Animator?> = HashMap()
    open fun show(view: View, show: AnimationOptions, listener: ScreenAnimationListener) {
        val animator: Animator = show.getAnimation(view, getDefaultPushAnimation(view))
        animator.addListener(object : AnimatorListenerAdapter() {
            private var isCancelled = false
            override fun onAnimationStart(animation: Animator) {
                runningAnimators[view] = animator
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

    open fun dismiss(view: View?, dismiss: AnimationOptions, listener: ScreenAnimationListener) {
        if (runningAnimators.containsKey(view)) {
            Objects.requireNonNull(runningAnimators[view]).cancel()
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

    val isRunning: Boolean
        get() = !runningAnimators.isEmpty()
}