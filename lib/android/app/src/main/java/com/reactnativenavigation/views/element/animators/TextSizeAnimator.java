package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.widget.TextView;

import com.facebook.react.views.text.ReactTextView;
import com.reactnativenavigation.utils.TextViewUtils;
import com.reactnativenavigation.views.element.Element;

import static com.reactnativenavigation.utils.TextViewUtils.getTextSize;

public class TextSizeAnimator extends PropertyAnimatorCreator<ReactTextView> {

    public TextSizeAnimator(Element from, Element to) {
        super(from, to);
    }

    @Override
    protected boolean shouldAnimateProperty(ReactTextView fromChild, ReactTextView toChild) {
        return getTextSize(fromChild) != getTextSize(toChild);
    }

    @Override
    public Animator create() {
        return ObjectAnimator.ofFloat(
                to,
                "textSize",
                TextViewUtils.getTextSize((TextView) from.getChild()),
                TextViewUtils.getTextSize((TextView) to.getChild())
        );
    }
}
