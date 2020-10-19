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
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.utils.awaitPost
import com.reactnativenavigation.utils.awaitRender
import com.reactnativenavigation.viewcontrollers.common.BaseAnimator
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.TransitionAnimatorCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

open class StackAnimator @JvmOverloads constructor(
        context: Context,
        private val transitionAnimatorCreator: TransitionAnimatorCreator = TransitionAnimatorCreator()
) : BaseAnimator(context) {
    private val runningPushAnimations: MutableMap<View, Animator> = HashMap()
    private val runningPIPAnimations: MutableMap<View, Animator> = HashMap()

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

    fun push(appearing: ViewController<*>, disappearing: ViewController<*>, options: Options, onAnimationEnd: Runnable) {
        val set = createPushAnimator(appearing, onAnimationEnd)
        runningPushAnimations[appearing.view] = set
        if (options.animations.push.sharedElements.hasValue()) {
            pushWithElementTransition(appearing, disappearing, options, set)
        } else {
            pushWithoutElementTransitions(appearing, options, set)
        }
    }

    open fun pop(appearing: ViewController<*>, disappearing: ViewController<*>, pop: NestedAnimationsOptions, onAnimationEnd: Runnable) {
        if (runningPushAnimations.containsKey(disappearing.view)) {
            runningPushAnimations[disappearing.view]!!.cancel()
            onAnimationEnd.run()
        } else {
            animatePop(appearing, disappearing, pop, onAnimationEnd)
        }
    }

    open suspend fun pipIn(pipContainer: View, pip: ViewController<*>, options: Options, onAnimationEnd: Runnable?) {
        val set = createPIPAnimator(pip, onAnimationEnd!!)
        runningPIPAnimations[pipContainer] = set
        if (options.animations.pipIn.elementTransitions.hasValue()) {
            pipInElementTransition(pipContainer, pip, options, set, object : TransitionAnimatorCreator.CreatorResultCallback() {
                override fun onError() {
                    pipInWithoutElementTransitions(pipContainer, pip, options, set)
                }
            })
        } else {
            pipInWithoutElementTransitions(pipContainer, pip, options, set)
        }
    }


    private fun animatePop(appearing: ViewController<*>, disappearing: ViewController<*>, pop: NestedAnimationsOptions, onAnimationEnd: Runnable) {
        GlobalScope.launch(Dispatchers.Main.immediate) {
            val set = createPopAnimator(onAnimationEnd)
            if (pop.sharedElements.hasValue()) {
                popWithElementTransitions(appearing, disappearing, pop, set)
            } else {
                popWithoutElementTransitions(pop, set, disappearing)
            }
        }
    }

    private suspend fun popWithElementTransitions(appearing: ViewController<*>, disappearing: ViewController<*>, pop: NestedAnimationsOptions, set: AnimatorSet) {
        val fade = if (pop.content.isFadeAnimation()) pop else FadeAnimation()
        val transitionAnimators = transitionAnimatorCreator.create(pop, fade.content, disappearing, appearing)
        set.playTogether(fade.content.getAnimation(disappearing.view), transitionAnimators)
        transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
        transitionAnimators.removeAllListeners()
        set.start()
    }

    private fun popWithoutElementTransitions(pop: NestedAnimationsOptions, set: AnimatorSet, disappearing: ViewController<*>) {
        set.playTogether(pop.content.getAnimation(disappearing.view, getDefaultPopAnimation(disappearing.view)))
        set.start()
    }

    private fun createPopAnimator(onAnimationEnd: Runnable): AnimatorSet {
        val set = AnimatorSet()
        set.addListener(object : AnimatorListenerAdapter() {
            private var cancelled = false
            override fun onAnimationCancel(animation: Animator) {
                cancelled = true
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!cancelled) onAnimationEnd.run()
            }
        })
        return set
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

    private fun createPIPAnimator(appearing: ViewController<*>, onAnimationEnd: Runnable): AnimatorSet {
        val set = AnimatorSet()
        set.addListener(object : AnimatorListenerAdapter() {
            private var isCancelled = false
            override fun onAnimationCancel(animation: Animator) {
                isCancelled = true
                runningPIPAnimations.remove(appearing.view)
                onAnimationEnd.run()
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!isCancelled) {
                    runningPIPAnimations.remove(appearing.view)
                    onAnimationEnd.run()
                }
            }
        })
        return set
    }

    private fun pushWithElementTransition(appearing: ViewController<*>, disappearing: ViewController<*>, options: Options, set: AnimatorSet) = GlobalScope.launch(Dispatchers.Main.immediate) {
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

        private suspend fun pipInElementTransition(pipContainer: View, pipIn: ViewController<*>, options: Options, set: AnimatorSet, callback: TransitionAnimatorCreator.CreatorResultCallback) {
            val fade = if (options.animations.pipIn.content.isFadeAnimation()) options.animations.pipIn.content else FadeAnimation().content
            transitionAnimatorCreator.createPIPTransitions(
                    options.animations.pipIn,
                    fade,
                    pipContainer,
                    pipIn,
                    object : TransitionAnimatorCreator.CreatorResultCallback(callback) {
                        override fun onSuccess(transitionAnimators: AnimatorSet) {
                            set.playTogether(options.animations.pipIn.content.getAnimation(pipContainer, getDefaultPopAnimation(pipContainer)), transitionAnimators)
                            transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
                            transitionAnimators.removeAllListeners()
                            set.start()
                        }
                    }
            )
        }

        private fun pipInWithoutElementTransitions(pipContainer: View, appearing: ViewController<*>, options: Options, set: AnimatorSet) {
            if (options.animations.pipIn.waitForRender.isTrue) {
                appearing.view.alpha = 0f
                appearing.addOnAppearedListener {
                    appearing.view.alpha = 1f
                    set.playTogether(options.animations.pipIn.content.getAnimation(pipContainer, getDefaultPopAnimation(pipContainer)))
                    set.start()
                }
            } else {
                set.playTogether(options.animations.pipIn.content.getAnimation(pipContainer, getDefaultPopAnimation(pipContainer)))
                set.start()
            }
        }

        open suspend fun pipOut(pipContainer: View, pip: ViewController<*>, options: Options, onAnimationEnd: Runnable?) {
            val set = createPIPAnimator(pip, onAnimationEnd!!)
            runningPIPAnimations[pipContainer] = set
            if (options.animations.pipOut.elementTransitions.hasValue()) {
                pipOutElementTransition(pipContainer, pip, options, set, object : TransitionAnimatorCreator.CreatorResultCallback() {
                    override fun onError() {
                        pipOutWithoutElementTransitions(pipContainer, pip, options, set)
                    }
                })
            } else {
                pipOutWithoutElementTransitions(pipContainer, pip, options, set)
            }
        }

        private fun pipOutWithoutElementTransitions(pipContainer: View, appearing: ViewController<*>, options: Options, set: AnimatorSet) {
            if (options.animations.pipOut.waitForRender.isTrue) {
                appearing.view.alpha = 0f
                appearing.addOnAppearedListener {
                    appearing.view.alpha = 1f
                    set.playTogether(options.animations.pipOut.content.getAnimation(pipContainer, getDefaultPushAnimation(pipContainer)))
                    set.start()
                }
            } else {
                set.playTogether(options.animations.pipOut.content.getAnimation(pipContainer, getDefaultPushAnimation(pipContainer)))
                set.start()
            }
        }

        private suspend fun pipOutElementTransition(pipContainer: View, pipOut: ViewController<*>, options: Options, set: AnimatorSet, callback: TransitionAnimatorCreator.CreatorResultCallback) {
            val fade = if (options.animations.pipOut.content.isFadeAnimation()) options.animations.pipOut.content else FadeAnimation().content
            transitionAnimatorCreator.createPIPOutTransitions(
                    options.animations.pipOut,
                    fade,
                    pipContainer,
                    pipOut,
                    object : TransitionAnimatorCreator.CreatorResultCallback(callback) {
                        override fun onSuccess(transitionAnimators: AnimatorSet) {
                            set.playTogether(options.animations.pipOut.content.getAnimation(pipContainer, getDefaultPopAnimation(pipContainer)), transitionAnimators)
                            transitionAnimators.listeners.forEach { listener: Animator.AnimatorListener -> set.addListener(listener) }
                            transitionAnimators.removeAllListeners()
                            set.start()
                        }
                    }
            )
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
