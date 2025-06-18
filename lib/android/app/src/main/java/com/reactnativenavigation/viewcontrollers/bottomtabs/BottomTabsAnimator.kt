package com.reactnativenavigation.viewcontrollers.bottomtabs

import com.reactnativenavigation.views.animations.BaseViewAppearanceAnimator
import com.reactnativenavigation.views.bottomtabs.BottomTabs

class BottomTabsAnimator(view: BottomTabs? = null) : BaseViewAppearanceAnimator<BottomTabs>(HideDirection.Down, view) {
    override fun onShowAnimationEnd() {
        view.restoreBottomNavigation(false)
    }

    override fun onHideAnimationEnd() {
        view.hideBottomNavigation(false)
    }
}