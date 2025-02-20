package com.reactnativenavigation.viewcontrollers.viewcontroller

import android.animation.ValueAnimator
import android.app.Activity
import com.reactnativenavigation.utils.SystemUiUtils.setStatusBarColor
import com.reactnativenavigation.views.animations.ColorAnimator
import java.lang.ref.WeakReference

class StatusBarColorAnimator(val activity: Activity) {
    private val colorAnimator = ColorAnimator()
    private val window = WeakReference(activity.window)

    fun getAnimator(from: Int, to: Int, isTranslucent: Boolean): ValueAnimator =
        colorAnimator.getAnimation(from, to).apply {
            addUpdateListener { animation ->
                setStatusBarColor(window.get(), animation.animatedValue as Int, isTranslucent)
            }
        }
}
