package com.reactnativenavigation.viewcontrollers.stack

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import androidx.annotation.RestrictTo
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.FadeAnimation
import com.reactnativenavigation.options.NestedAnimationsOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.utils.CollectionUtils
import com.reactnativenavigation.viewcontrollers.common.BaseAnimator
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.ElementTransitionManager
import java.util.*

open class StackAnimator(context: Context, private val transitionManager: ElementTransitionManager) : BaseAnimator(context) {
    private val runningPushAnimations: MutableMap<View, Animator> = HashMap()

    fun push(appearing: ViewController<*>, disappearing: ViewController<*>, options: Options, onAnimationEnd: Runnable) {
        val set = createPushAnimator(appearing, onAnimationEnd)
        runningPushAnimations[appearing.view] = set
        if (options.animations.push.sharedElements.hasValue()) {
            pushWithElementTransition(appearing, disappearing, options, set)
        } else {
            pushWithoutElementTransitions(appearing, options, set)
        }
    }

    private fun createPushAnimator(appearing: ViewController<*>, onAnimationEnd: Runnable): AnimatorSet {
        val set = AnimatorSet()
        set.addListener(object : AnimatorListenerAdapter() {
            private var isCancelled = false
            override fun onAnimationCancel(animation: Animator) {
                isCancelled = true
                runningPushAnimations.remove(appearing.view)
                onAnimationEnd.run()
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!isCancelled) {
                    runningPushAnimations.remove(appearing.view)
                    onAnimationEnd.run()
                }
            }
        })
        return set
    }

    private fun pushWithElementTransition(appearing: ViewController<*>, disappearing: ViewController<*>, options: Options, set: AnimatorSet) {
        appearing.view.alpha = 0f
        transitionManager.createTransitions(
                options.animations.push,
                disappearing,
                appearing) { transitionSet ->
                    if (transitionSet.isEmpty) {
                        set.playTogether(options.animations.push.content.getAnimation(appearing.view, getDefaultPushAnimation(appearing.view)))
                    } else {
                        val fade = if (options.animations.push.content.isFadeAnimation()) options.animations.push.content else FadeAnimation().content
                        val transitions = transitionManager.createAnimators(fade, transitionSet)
                        val listeners = transitions.listeners
                        set.playTogether(fade.getAnimation(appearing.view), transitions)
                        CollectionUtils.forEach(listeners) { listener: Animator.AnimatorListener? -> set.addListener(listener) }
                        transitions.removeAllListeners()
                    }
                    set.start()
                }
    }

    private fun pushWithoutElementTransitions(appearing: ViewController<*>, options: Options, set: AnimatorSet) {
        if (options.animations.push.waitForRender.isTrue) {
            appearing.view.alpha = 0f
            appearing.addOnAppearedListener {
                appearing.view.alpha = 1f
                set.playTogether(options.animations.push.content.getAnimation(appearing.view, getDefaultPushAnimation(appearing.view)))
                set.start()
            }
        } else {
            set.playTogether(options.animations.push.content.getAnimation(appearing.view, getDefaultPushAnimation(appearing.view)))
            set.start()
        }
    }

    open fun pop(view: View, pop: NestedAnimationsOptions, onAnimationEnd: Runnable) {
        if (runningPushAnimations.containsKey(view)) {
            runningPushAnimations[view]!!.cancel()
            onAnimationEnd.run()
            return
        }
        val set = pop.content.getAnimation(view, getDefaultPopAnimation(view))
        set.addListener(object : AnimatorListenerAdapter() {
            private var cancelled = false
            override fun onAnimationCancel(animation: Animator) {
                cancelled = true
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!cancelled) onAnimationEnd.run()
            }
        })
        set.start()
    }

    open fun setRoot(root: View, setRoot: AnimationOptions, onAnimationEnd: Runnable) {
        root.visibility = View.INVISIBLE
        val set = setRoot.getAnimation(root)
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                root.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                onAnimationEnd.run()
            }
        })
        set.start()
    }

    fun cancelPushAnimations() {
        for (view in runningPushAnimations.keys) {
            runningPushAnimations[view]!!.cancel()
            runningPushAnimations.remove(view)
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun endPushAnimation(view: View?) {
        if (runningPushAnimations.containsKey(view)) {
            runningPushAnimations[view]!!.end()
        }
    }
}
