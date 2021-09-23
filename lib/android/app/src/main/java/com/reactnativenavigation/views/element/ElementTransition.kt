package com.reactnativenavigation.views.element

import android.animation.Animator
import android.util.Property
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

    override fun createAnimators(): Animator = transitionOptions.getAnimation(view)

    fun isInvalid(): Boolean = !isValid()

    fun setValueDy(animation: Property<View?, Float?>?, fromDelta: Float, toDelta: Float) {
        transitionOptions.setValueDy(animation, fromDelta, toDelta)
    }

    fun isValid(): Boolean = ::view.isInitialized

}