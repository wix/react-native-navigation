package com.reactnativenavigation.viewcontrollers.stack.topbar

import com.reactnativenavigation.views.animations.BaseViewAppearanceAnimator
import com.reactnativenavigation.views.stack.topbar.TopBar

class TopBarAppearanceAnimator @JvmOverloads constructor(view: TopBar? = null)
    : BaseViewAppearanceAnimator<TopBar>(HideDirection.Up, view) {

    @Suppress("UNUSED_PARAMETER")
    fun hideOnScroll(translationStart: Float, translationEndDy: Float) {
        // NOOP for now - this entire mechanism needs to be reimplemented as it relies on bridge events which are obsolete in TurboModules config
    }
}