package com.reactnativenavigation.viewcontrollers.stack.topbar

import com.reactnativenavigation.views.animations.BaseViewAnimator
import com.reactnativenavigation.views.stack.topbar.TopBar

class TopBarAnimator @JvmOverloads constructor(view: TopBar? = null) : BaseViewAnimator<TopBar>(HideDirection.Up, view) {
    @Suppress("UNUSED_PARAMETER")
    fun hideOnScroll(translationStart: Float, translationEndDy: Float) {
        // NOOP for now - this entire mechanism needs to be reimplemented as it relies on bridge events which are obsolete in TurboModules config
    }
}