package com.reactnativenavigation.views.element

import android.animation.AnimatorSet
import android.view.View
import com.reactnativenavigation.viewcontrollers.ViewController

abstract class Transition {
    abstract var viewController: ViewController<*>
    abstract val view: View
    val topInset: Int
        get() = viewController.topInset

    abstract fun createAnimators(): AnimatorSet
}