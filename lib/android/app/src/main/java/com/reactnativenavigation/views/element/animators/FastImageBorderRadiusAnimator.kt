package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import com.facebook.react.views.image.ReactImageView
import com.facebook.react.views.view.ReactViewBackgroundDrawable
import com.reactnativenavigation.R
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.RoundCornersOutlineProvider
import com.reactnativenavigation.utils.ViewTags

class FastImageBorderRadiusAnimator(from: View, to: View) : PropertyAnimatorCreator<ImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ImageView, toChild: ImageView) =
            fromChild !is ReactImageView
                    && toChild !is ReactImageView
                    && getBorderRadius(from) != getBorderRadius(to)

    override fun create(options: SharedElementTransitionOptions): Animator {
        from as ImageView; to as ImageView
        val fromRadius = getBorderRadius(from)
        val toRadius = getBorderRadius(to)
        val outlineProvider = RoundCornersOutlineProvider(to, fromRadius)
        setInitialOutline(to, outlineProvider)

        return ObjectAnimator.ofObject(
                CornerRadiusEvaluator { outlineProvider.updateRadius(it) },
                fromRadius,
                toRadius
        ).apply {
            doOnEnd { to.outlineProvider = null }
        }
    }

    private fun getBorderRadius(child: View): Float {
        val parent = ViewTags.get<ViewGroup>(child, R.id.original_parent, child.parent as ViewGroup)
        val parentIsUsedOnlyToDrawBorderRadiusOverImage = parent.childCount <= 1 || parent.children.contains(child)
        val background = parent.background as? ReactViewBackgroundDrawable
        return if (parentIsUsedOnlyToDrawBorderRadiusOverImage && background?.hasRoundedBorders() == true)
            background.fullBorderRadius else 0f
    }

    private fun setInitialOutline(to: ImageView, provider: RoundCornersOutlineProvider) {
        to.outlineProvider = provider
        to.clipToOutline = true
        to.invalidateOutline()
    }
}
