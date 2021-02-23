package com.reactnativenavigation.views.animations

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.reactnativenavigation.utils.ViewUtils

class DefaultViewAnimatorCreatorFake : ViewAnimatorCreator {
    private var showAnimator: Animator? = null
    private var hideAnimator: Animator? = null

    override fun getShowAnimator(view: View, hideDirection: BaseViewAnimator.HideDirection, translationStart: Float): Animator {
        if (showAnimator == null) {
            val direction = if (hideDirection == BaseViewAnimator.HideDirection.Up) 1 else -1
            showAnimator = ObjectAnimator.ofFloat(
                    view,
                    View.TRANSLATION_Y,
                    direction * (-ViewUtils.getHeight(view) - translationStart),
                    0f)
        }
        return showAnimator!!
    }

    override fun getHideAnimator(view: View, hideDirection: BaseViewAnimator.HideDirection, additionalDy: Float): Animator {
        if (hideAnimator == null) {
            val direction = if (hideDirection == BaseViewAnimator.HideDirection.Up) -1 else 1
            hideAnimator = ObjectAnimator.ofFloat(
                    view,
                    View.TRANSLATION_Y,
                    view.translationY,
                    direction * (view.measuredHeight + additionalDy)
            )
        }
        return hideAnimator!!
    }
}