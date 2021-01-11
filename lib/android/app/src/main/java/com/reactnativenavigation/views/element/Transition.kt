package com.reactnativenavigation.views.element

import android.animation.Animator
import android.view.View
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

abstract class Transition {
    abstract var viewController: ViewController<*>
    abstract val view: View
    abstract val topInset: Int

    abstract fun createAnimators(): Animator
}