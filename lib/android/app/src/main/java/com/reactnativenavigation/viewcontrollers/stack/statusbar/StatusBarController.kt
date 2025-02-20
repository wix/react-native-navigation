package com.reactnativenavigation.viewcontrollers.stack.statusbar

import android.animation.Animator
import com.reactnativenavigation.options.Options

interface StatusBarController {
    fun getStatusBarPushAnimation(appearingOptions: Options): Animator?
    fun getStatusBarPopAnimation(appearingOptions: Options, disappearingOptions: Options): Animator?
}
