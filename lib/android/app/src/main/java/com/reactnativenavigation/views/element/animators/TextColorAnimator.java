package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.widget.TextView;

import com.facebook.react.views.text.ReactTextView;
import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.utils.ColorUtils;
import com.reactnativenavigation.utils.TextViewUtils;
import com.reactnativenavigation.views.element.Element;

import static com.reactnativenavigation.utils.TextViewUtils.getTextColor;

public class TextColorAnimator extends PropertyAnimatorCreator<ReactTextView> {

    public TextColorAnimator(Element from, Element to) {
        super(from, to);
    }

    @Override
    protected boolean shouldAnimateProperty(ReactTextView fromChild, ReactTextView toChild) {
        return getTextColor(fromChild) != getTextColor(toChild);
    }

    @Override
    public Animator create(Transition transition) {
        return ObjectAnimator.ofObject(
                to,
                "textColor",
                new LabColorEvaluator(),
                ColorUtils.colorToLAB(TextViewUtils.getTextColor((TextView) from.getChild())),
                ColorUtils.colorToLAB(TextViewUtils.getTextColor((TextView) to.getChild()))
        ).setDuration(transition.duration.get());
    }
}
