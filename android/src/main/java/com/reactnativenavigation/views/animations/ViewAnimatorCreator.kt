package com.reactnativenavigation.views.animations

import android.animation.Animator
import android.view.View

interface ViewAnimatorCreator {
    fun getShowAnimator(view: View, hideDirection: BaseViewAppearanceAnimator.HideDirection, translationStart: Float): Animator
    fun getHideAnimator(view: View, hideDirection: BaseViewAppearanceAnimator.HideDirection, additionalDy: Float): Animator
}