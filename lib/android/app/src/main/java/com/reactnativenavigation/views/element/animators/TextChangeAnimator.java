package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.widget.TextView;

import com.facebook.react.views.text.ReactTextView;
import com.reactnativenavigation.views.element.Element;
import com.shazam.android.widget.text.reflow.ReflowTextAnimatorHelper;

import static com.reactnativenavigation.utils.TextViewUtils.getTextSize;

public class TextChangeAnimator extends PropertyAnimatorCreator<ReactTextView> {

    public TextChangeAnimator(Element from, Element to) {
        super(from, to);
    }

    @Override
    protected boolean shouldAnimateProperty(ReactTextView fromChild, ReactTextView toChild) {
        return getTextSize(fromChild) != getTextSize(toChild);
    }

    @Override
    public Animator create() {
        return new ReflowTextAnimatorHelper
                .Builder((TextView) from.getChild(), (TextView) to.getChild())
                .calculateDuration(false)
                .buildAnimator();
    }
}
