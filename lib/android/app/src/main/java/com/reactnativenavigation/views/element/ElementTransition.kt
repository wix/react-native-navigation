package com.reactnativenavigation.views.element

import android.animation.AnimatorSet
import android.util.Property
import android.view.View
import com.reactnativenavigation.parse.ElementTransitionOptions
import com.reactnativenavigation.viewcontrollers.ViewController

class ElementTransition(private val transitionOptions: ElementTransitionOptions) : Transition() {
    val id: String
        get() = transitionOptions.id
    override lateinit var viewController: ViewController<*>
    override lateinit var view: View
    override val topInset: Int
        get() = viewController.topInset

    override fun createAnimators(): AnimatorSet = transitionOptions.getAnimation(view)

    fun setValueDy(animation: Property<View?, Float?>?, fromDelta: Float, toDelta: Float) {
        transitionOptions.setValueDy(animation, fromDelta, toDelta)
    }

    fun isValid(): Boolean = ::view.isInitialized
}