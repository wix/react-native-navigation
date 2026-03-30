package com.reactnativenavigation.views.element.animators

import android.animation.Animator
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.react.views.text.ReactTextView
import com.reactnativenavigation.options.SharedElementTransitionOptions
import com.reactnativenavigation.utils.*
import com.shazam.android.widget.text.reflow.ReflowTextAnimatorHelper

class TextChangeAnimator(from: View, to: View) : PropertyAnimatorCreator<ReactTextView>(from, to) {
    override fun shouldAnimateProperty(fromChild: ReactTextView, toChild: ReactTextView): Boolean {
        val fromXy = ViewUtils.getLocationOnScreen(from)
        val toXy = ViewUtils.getLocationOnScreen(to)
        return fromChild.text.toString() == toChild.text.toString() && (
                TextViewUtils.getTextSize(fromChild) != TextViewUtils.getTextSize(toChild) || !fromXy.equals(toXy.x, toXy.y)
                )
    }

    override fun create(options: SharedElementTransitionOptions): Animator {
        return ReflowTextAnimatorHelper.Builder(from as TextView, to as TextView)
                .calculateDuration(false)
                .setBoundsCalculator { view: View ->
                    val loc = IntArray(2)
                    view.getLocationInWindow(loc)
                    val x = if (view == to) (to.layoutParams as ViewGroup.MarginLayoutParams).leftMargin else loc[0]
                    val y = if (view == to) (to.layoutParams as ViewGroup.MarginLayoutParams).topMargin else loc[1]
                    Rect(
                            x,
                            y,
                            x + view.width,
                            y + view.height
                    )
                }
                .setTextColorGetter {
                    TextViewUtils.getTextColor(it)
                }.buildAnimator()
    }
}