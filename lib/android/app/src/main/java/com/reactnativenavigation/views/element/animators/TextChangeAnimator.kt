package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.react.views.text.ReactTextView
import com.reactnativenavigation.parse.SharedElementTransitionOptions
import com.reactnativenavigation.utils.TextViewUtils
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.views.element.SharedElementTransition
import com.shazam.android.widget.text.reflow.ReflowTextAnimatorHelper

class TextChangeAnimator(private val transition: SharedElementTransition, from: View, to: View) : PropertyAnimatorCreator<ReactTextView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ReactTextView, toChild: ReactTextView): Boolean {
        val fromXy = ViewUtils.getLocationOnScreen(from)
        val toXy = ViewUtils.getLocationOnScreen(to)
        return TextViewUtils.getTextSize(fromChild) != TextViewUtils.getTextSize(toChild) ||
                !fromXy.equals(toXy.x, toXy.y)
    }

    override fun create(transition: SharedElementTransitionOptions): Animator {
        (to.layoutParams as ViewGroup.MarginLayoutParams).topMargin += this.transition.topInset
        return ReflowTextAnimatorHelper.Builder(from as TextView, to as TextView)
                .calculateDuration(false)
                .setBoundsCalculator { view: View ->
                    val loc = IntArray(2)
                    view.getLocationInWindow(loc)
                    val topInset = if (view == to) this.transition.topInset else 0
                    Rect(
                            loc[0],
                            loc[1] + topInset,
                            loc[0] + view.width,
                            loc[1] + view.height
                    )
                }
                .setTextColorGetter {
                    TextViewUtils.getTextColor(it)
                }
                .buildAnimator()
                .setDuration(transition.getDuration())
    }
}