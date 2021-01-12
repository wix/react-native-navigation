package com.reactnativenavigation.views.animations

import android.animation.Animator
import android.view.View

interface ViewAnimatorCreator {
    fun getShowAnimator(view: View, hideDirection: BaseViewAnimator.HideDirection, translationStart: Float): Animator
    fun getHideAnimator(view: View, hideDirection: BaseViewAnimator.HideDirection, additionalDy: Float): Animator
}