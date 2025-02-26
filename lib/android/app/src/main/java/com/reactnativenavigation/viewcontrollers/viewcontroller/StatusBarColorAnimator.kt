package com.reactnativenavigation.viewcontrollers.viewcontroller

import android.animation.ValueAnimator
import android.app.Activity
import com.reactnativenavigation.utils.ColorUtils
import com.reactnativenavigation.utils.SystemUiUtils
import com.reactnativenavigation.views.animations.ColorAnimator
import java.lang.ref.WeakReference

private const val STATUS_BAR_TRANSLUCENCY_ALPHA = (SystemUiUtils.STATUS_BAR_HEIGHT_TRANSLUCENCY * 255).toInt()

class StatusBarColorAnimator(val activity: Activity) {
    private val colorAnimator = ColorAnimator()
    private val window = WeakReference(activity.window)

    fun getAnimator(from: Int, to: Int, isTranslucent: Boolean): ValueAnimator =
        colorAnimator.getAnimation(
            from = from,
            to = embedTranslucency(to, isTranslucent)
        ).apply {
            addUpdateListener { animation ->
                SystemUiUtils.setStatusBarColor(window.get(), animation.animatedValue as Int)
            }
        }

    private fun embedTranslucency(color: Int, isTranslucent: Boolean): Int =
        if (!isTranslucent) color else ColorUtils.setAlpha(color, STATUS_BAR_TRANSLUCENCY_ALPHA)
}
