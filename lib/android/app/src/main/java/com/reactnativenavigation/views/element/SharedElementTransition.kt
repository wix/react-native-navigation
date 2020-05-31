package com.reactnativenavigation.views.element

import android.animation.AnimatorSet
import android.view.View
import com.reactnativenavigation.parse.SharedElementTransitionOptions
import com.reactnativenavigation.viewcontrollers.ViewController
import com.reactnativenavigation.views.element.animators.*

class SharedElementTransition(appearing: ViewController<*>, private val options: SharedElementTransitionOptions) : Transition() {
    val fromId: String = options.fromId.get()
    val toId: String = options.toId.get()
    lateinit var from: View
    lateinit var to: View
    override var viewController: ViewController<*> = appearing
    override val view: View
        get() = to
    override val topInset: Int
        get() = viewController.topInset

    fun isValid(): Boolean = this::from.isInitialized

    override fun createAnimators(): AnimatorSet {
        val animators = animators()
                .filter { it.shouldAnimateProperty() }
                .map { it.create(options).apply {
                    duration = options.getDuration()
                    startDelay = options.getStartDelay()
                    interpolator = options.getInterpolator()
                } }
        val set = AnimatorSet()
        set.playTogether(animators)
        return set
    }

    private fun animators(): List<PropertyAnimatorCreator<*>> {
        return listOf(
                MatrixAnimator(from, to),
                XAnimator(from, to),
                YAnimator(from, to),
                RotationAnimator(from, to),
                ScaleXAnimator(from, to),
                ScaleYAnimator(from, to),
                BackgroundColorAnimator(from, to),
                TextChangeAnimator(from, to)
        )
    }
}