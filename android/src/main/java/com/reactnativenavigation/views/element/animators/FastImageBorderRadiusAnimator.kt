package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.BorderRadiusOutlineProvider

class FastImageBorderRadiusAnimator(from: View, to: View) : PropertyAnimatorCreator<ImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ImageView, toChild: ImageView): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                fromChild !is ReactImageView
                && toChild !is ReactImageView
                && (getInheritedBorderRadius(from) != 0f || getInheritedBorderRadius(to) != 0f)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun create(options: SharedElementTransitionOptions): Animator {
        from as ImageView; to as ImageView
        val fromRadius = getInheritedBorderRadius(from)
        val toRadius = getInheritedBorderRadius(to)
        val outlineProvider = BorderRadiusOutlineProvider(to, fromRadius)
        setInitialOutline(to, outlineProvider)

        return ObjectAnimator.ofObject(
                CornerRadiusEvaluator { outlineProvider.updateRadius(it) },
                fromRadius,
                toRadius
        ).apply {
            doOnEnd { to.outlineProvider = null }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setInitialOutline(to: ImageView, provider: BorderRadiusOutlineProvider) {
        to.outlineProvider = provider
        to.clipToOutline = true
        to.invalidateOutline()
    }
}
