package com.reactnativenavigation.viewcontrollers.statusbar

import android.animation.Animator
import com.reactnativenavigation.options.Options

interface StatusBarController {
    fun getStatusBarPushAnimation(appearingOptions: Options): Animator?
    fun getStatusBarPopAnimation(appearingOptions: Options, disappearingOptions: Options): Animator?
}
