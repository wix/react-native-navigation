package com.reactnativenavigation.viewcontrollers.bottomtabs

import com.reactnativenavigation.views.animations.BaseViewAnimator
import com.reactnativenavigation.views.bottomtabs.BottomTabs
import com.reactnativenavigation.views.stack.topbar.TopBar

class BottomTabsAnimator(view: BottomTabs? = null) : BaseViewAnimator<BottomTabs>(HideDirection.Down, view) {
    override fun onShowAnimationEnd() {
        view.restoreBottomNavigation(false)
    }

    override fun onHideAnimationEnd() {
        view.hideBottomNavigation(false)
    }
}