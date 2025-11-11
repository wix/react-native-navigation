package com.reactnativenavigation.views.animations

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View

class ColorAnimator {
    fun getAnimation(from: Int, to: Int): ValueAnimator = createObjectAnimator(null, from, to)
    fun getAnimation(view: View, from: Int, to: Int): ValueAnimator = createObjectAnimator(view, from, to)

    private fun createObjectAnimator(view: View?, from: Int, to: Int) =
        if (view == null) {
            ObjectAnimator.ofArgb(from, to)
        } else {
            ObjectAnimator.ofArgb(
                view,
                view.BkgColorProperty,
                from,
                to,
            )
        }
}
