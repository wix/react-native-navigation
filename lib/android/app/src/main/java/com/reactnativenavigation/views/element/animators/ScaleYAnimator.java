package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.views.text.ReactTextView;
import com.reactnativenavigation.views.element.Element;

import java.util.Collections;
import java.util.List;

public class ScaleYAnimator extends PropertyAnimatorCreator<ViewGroup> {

    public ScaleYAnimator(Element from, Element to) {
        super(from, to);
    }

    @Override
    public boolean shouldAnimateProperty(ViewGroup fromChild, ViewGroup toChild) {
        return fromChild.getChildCount() == 0 && toChild.getChildCount() == 0;
    }

    @Override
    protected List<Class> excludedViews() {
        return Collections.singletonList(ReactTextView.class);
    }

    @Override
    public Animator create() {
        return ObjectAnimator.ofFloat(
                to.getChild(),
                View.SCALE_Y,
                ((float) from.getChild().getHeight()) / to.getChild().getHeight(),
                1
        );
    }
}
