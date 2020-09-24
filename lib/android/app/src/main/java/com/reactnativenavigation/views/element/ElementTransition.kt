package com.reactnativenavigation.views.element

import android.animation.AnimatorSet
import android.view.View
import com.reactnativenavigation.options.ElementTransitionOptions
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

class ElementTransition(private val transitionOptions: ElementTransitionOptions) : Transition() {
    val id: String
        get() = transitionOptions.id
    override lateinit var viewController: ViewController<*>
    override lateinit var view: View
    override val topInset: Int
        get() = viewController.topInset

    override fun createAnimators(): AnimatorSet {
        return transitionOptions.getAnimation(view).apply {
            applyInitialAnimationValues()
        }
    }

    fun isValid(): Boolean = ::view.isInitialized

    private fun applyInitialAnimationValues() {
        transitionOptions.viewPropertyAnimations.forEach {
            it.from.get(null)?.let { initialValue ->
                it.setCurrentViewProperty(view, initialValue)
            }
        }
    }
}