package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.views.element.Element;

public class ScaleXAnimator extends PropertyAnimatorCreator<ViewGroup> {

    public ScaleXAnimator(Element from, Element to) {
        super(from, to);
    }

    @Override
    public boolean shouldAnimateProperty(ViewGroup fromChild, ViewGroup toChild) {
        return fromChild.getChildCount() == 0 && toChild.getChildCount() == 0;
    }

    @Override
    public Animator create() {
        return ObjectAnimator.ofFloat(
                to.getChild(),
                View.SCALE_X,
                ((float) from.getChild().getWidth()) / to.getChild().getWidth(),
                0
        );
    }
}
