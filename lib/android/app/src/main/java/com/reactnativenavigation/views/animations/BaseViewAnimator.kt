package com.reactnativenavigation.views.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.VisibleForTesting
import androidx.core.animation.doOnEnd
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.animations.ViewAnimationOptions
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.utils.resetViewProperties

open class BaseViewAnimator<T : View>(
        private val hideDirection: HideDirection,
        view: T? = null,
        private val defaultAnimatorCreator: ViewAnimatorCreator = DefaultViewAnimatorCreator()
) {
    enum class HideDirection { Up, Down }
    private enum class AnimationState { Idle, AnimatingEnter, AnimatingExit }

    protected lateinit var view: T

    @VisibleForTesting
    var showAnimator: Animator = AnimatorSet()
        private set(value) {
            field = value
            field.addListener(showAnimatorListener)
            field.doOnEnd { onShowAnimationEnd() }
        }
    @VisibleForTesting
    var hideAnimator: Animator = AnimatorSet()
        set(value) {
            field = value
            field.addListener(hideAnimatorListener)
            field.doOnEnd { onHideAnimationEnd() }
        }

    private val showAnimatorListener = AnimatorListener(AnimationState.AnimatingEnter, View.VISIBLE)
    private val hideAnimatorListener = AnimatorListener(AnimationState.AnimatingExit, View.GONE)

    private inner class AnimatorListener(private val startState: AnimationState, private val endVisibility: Int) : AnimatorListenerAdapter() {
        var isCancelled = false

        override fun onAnimationStart(animation: Animator?) {
            view.resetViewProperties()
            view.visibility = View.VISIBLE
            animationState = startState
        }

        override fun onAnimationCancel(animation: Animator?) {
            isCancelled = true
        }

        override fun onAnimationEnd(animation: Animator?) {
            if (!isCancelled) {
                animationState = AnimationState.Idle
                view.visibility = endVisibility
            }
        }
    }

    private var animationState = AnimationState.Idle

    private val isOrWillBeVisible: Boolean
        get() = isFullyVisible || animationState == AnimationState.AnimatingEnter
    private val isFullyVisible: Boolean
        get() = view.visibility == View.VISIBLE && animationState == AnimationState.Idle

    val isOrWillBeHidden: Boolean
        get() = isFullyHidden || animationState == AnimationState.AnimatingExit
    private val isFullyHidden: Boolean
        get() = view.visibility == View.GONE && animationState == AnimationState.Idle

    init {
        view?.let { this.view = it }
    }

    @CallSuper
    open fun bindView(view: T) {
        this.view = view
    }

    open fun onShowAnimationEnd() = Unit

    open fun onHideAnimationEnd() = Unit

    fun isAnimatingHide() = hideAnimator.isRunning

    fun isAnimatingShow() = showAnimator.isRunning

    fun getPushAnimation(animation: ViewAnimationOptions, visible: Bool, additionalDy: Float = 0f): Animator? {
        if (isOrWillBeVisible && visible.isFalse) {
            showAnimator.cancel()
            hideAnimator = animation.exit.getAnimation(view, defaultAnimatorCreator.getHideAnimator(view, hideDirection, additionalDy))
            return hideAnimator
        }

        if (isOrWillBeHidden && visible.isTrueOrUndefined) {
            hideAnimator.cancel()
            showAnimator = animation.enter.getAnimation(view, defaultAnimatorCreator.getShowAnimator(view, hideDirection, additionalDy))
            return showAnimator
        }

        return null
    }

    fun getPopAnimation(animation: ViewAnimationOptions, visible: Bool, additionalDy: Float = 0f): Animator? {
        if (isOrWillBeVisible && visible.isFalse) {
            showAnimator.cancel()
            hideAnimator = animation.exit.getAnimation(view, defaultAnimatorCreator.getHideAnimator(view, hideDirection, additionalDy))
            return hideAnimator
        }

        if (isOrWillBeHidden && visible.isTrueOrUndefined) {
            hideAnimator.cancel()
            showAnimator = animation.enter.getAnimation(view, defaultAnimatorCreator.getShowAnimator(view, hideDirection, additionalDy))
            return showAnimator
        }

        return null
    }

    fun getSetStackRootAnimation(animation: ViewAnimationOptions, visible: Bool, additionalDy: Float = 0f): Animator? {
        if (isOrWillBeVisible && visible.isFalse) {
            showAnimator.cancel()
            hideAnimator = animation.exit.getAnimation(view, defaultAnimatorCreator.getHideAnimator(view, hideDirection, additionalDy))
            return hideAnimator
        }

        if (isOrWillBeHidden && visible.isTrueOrUndefined) {
            hideAnimator.cancel()
            showAnimator = animation.enter.getAnimation(view, defaultAnimatorCreator.getShowAnimator(view, hideDirection, additionalDy))
            return showAnimator
        }

        return null
    }

    fun show(options: AnimationOptions = AnimationOptions(), translationStartDy: Float = 0f) {
        if (isOrWillBeVisible) return
        showAnimator = if (options.hasValue()) {
            options.setValueDy(
                    View.TRANSLATION_Y,
                    -translationStartDy,
                    0f
            )
            options.getAnimation(view)
        } else {
            defaultAnimatorCreator.getShowAnimator(view, hideDirection, translationStartDy)
        }
        hideAnimator.cancel()
        showAnimator.start()
    }

    open fun hide(
            options: AnimationOptions = AnimationOptions(),
            additionalDy: Float = 0f,
            onAnimationEnd: Runnable? = null
    ) {
        if (isOrWillBeHidden) return
        hideAnimator = if (options.hasValue()) {
            options.setValueDy(View.TRANSLATION_Y, 0f, -additionalDy)
            options.getAnimation(view)
        } else {
            defaultAnimatorCreator.getHideAnimator(view, hideDirection, additionalDy)
        }
        showAnimator.cancel()
        hideAnimator.apply {
            doOnEnd { onAnimationEnd?.run() }
            start()
        }
    }
}