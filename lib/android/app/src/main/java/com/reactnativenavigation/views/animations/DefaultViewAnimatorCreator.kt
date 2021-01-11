package com.reactnativenavigation.views.animations

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.reactnativenavigation.utils.ViewUtils

class DefaultViewAnimatorCreator : ViewAnimatorCreator {
    companion object {
        private const val DURATION = 300L
        private val fastOutSlowInInterpolator = FastOutSlowInInterpolator()
    }

    override fun getShowAnimator(
            view: View,
            hideDirection: BaseViewAnimator.HideDirection,
            translationStart: Float
    ): Animator {
        val direction = if (hideDirection == BaseViewAnimator.HideDirection.Up) 1 else -1
        return ObjectAnimator.ofFloat(
                view,
                View.TRANSLATION_Y,
                direction * (-ViewUtils.getHeight(view) - translationStart),
                0f
        ).apply {
            interpolator = fastOutSlowInInterpolator
            duration = DURATION
        }
    }

    override fun getHideAnimator(
            view: View,
            hideDirection: BaseViewAnimator.HideDirection,
            additionalDy: Float
    ): Animator {
        val direction = if (hideDirection == BaseViewAnimator.HideDirection.Up) -1 else 1
        return ObjectAnimator.ofFloat(
                view,
                View.TRANSLATION_Y,
                view.translationY,
                direction * (view.measuredHeight + additionalDy)
        ).apply {
            interpolator = fastOutSlowInInterpolator
            duration = DURATION
        }
    }
}