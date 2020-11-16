package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import com.facebook.react.views.image.ReactImageView
import com.facebook.react.views.view.ReactViewBackgroundDrawable
import com.reactnativenavigation.R
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.BorderRadiusOutlineProvider
import com.reactnativenavigation.utils.ViewTags

class FastImageBorderRadiusAnimator(from: View, to: View) : PropertyAnimatorCreator<ImageView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ImageView, toChild: ImageView) =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
            fromChild !is ReactImageView
                    && toChild !is ReactImageView
                    && (getBorderRadius(from) != 0f || getBorderRadius(to) != 0f)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun create(options: SharedElementTransitionOptions): Animator {
        from as ImageView; to as ImageView
        val fromRadius = getBorderRadius(from)
        val toRadius = getBorderRadius(to)
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

    private fun getBorderRadius(child: View): Float {
        val parent = ViewTags.get<ViewGroup>(child, R.id.original_parent, child.parent as ViewGroup)
        val parentIsUsedOnlyToDrawBorderRadiusOverImage = parent.childCount <= 1 || parent.children.contains(child)
        val background = parent.background as? ReactViewBackgroundDrawable
        return if (parentIsUsedOnlyToDrawBorderRadiusOverImage && background?.hasRoundedBorders() == true)
            background.fullBorderRadius else 0f
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setInitialOutline(to: ImageView, provider: BorderRadiusOutlineProvider) {
        to.outlineProvider = provider
        to.clipToOutline = true
        to.invalidateOutline()
    }
}
