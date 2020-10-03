package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.RectF
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import com.facebook.react.views.view.ReactViewGroup
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.OutlineProvider
import com.reactnativenavigation.utils.areDimensionsWithInheritedScaleEqual
import com.reactnativenavigation.utils.borderRadius
import com.reactnativenavigation.utils.computeInheritedScale
import kotlin.math.max
import kotlin.math.roundToInt

class ReactViewOutlineAnimator(from: View, to: View) : PropertyAnimatorCreator<ReactViewGroup>(from, to) {
    override fun shouldAnimateProperty(fromChild: ReactViewGroup, toChild: ReactViewGroup): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                !areDimensionsWithInheritedScaleEqual(from, to) &&
                fromChild.childCount == 0 &&
                toChild.childCount == 0
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun create(options: SharedElementTransitionOptions): Animator {
        from as ReactViewGroup; to as ReactViewGroup
        val (inheritedScaleX, inheritedScaleY) = computeInheritedScale(from)

        to.layoutParams.width = max((from.width * inheritedScaleX).roundToInt(), to.width)
        to.layoutParams.height = max((from.height * inheritedScaleY).roundToInt(), to.height)

        val startDrawingRect = RectF(from.background.bounds)
        val endDrawingRect = RectF(to.background.bounds)
        startDrawingRect.right = startDrawingRect.right * inheritedScaleX
        startDrawingRect.bottom = startDrawingRect.bottom * inheritedScaleY
        val fromOutline = ViewOutline(
                startDrawingRect.width(),
                startDrawingRect.height(),
                from.borderRadius
        )
        val toOutline = ViewOutline(
                endDrawingRect.width(),
                endDrawingRect.height(),
                to.borderRadius
        )

        to.setBorderRadius(0f)
        val outlineProvider = OutlineProvider(to, ViewOutline(fromOutline.width, fromOutline.height, fromOutline.radius))
        setOutlineProvider(outlineProvider)

        return ObjectAnimator.ofObject(
                OutlineEvaluator() { outlineProvider.update(it) },
                fromOutline,
                toOutline
        ).apply {
            doOnEnd {
                to.setBorderRadius(outlineProvider.radius)
                to.outlineProvider = null
                to.clipToOutline = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setOutlineProvider(provider: OutlineProvider) {
        to.outlineProvider = provider
        to.clipToOutline = true
        to.invalidateOutline()
    }
}