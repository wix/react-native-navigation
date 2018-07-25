package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;

import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.views.element.Element;

public abstract class PropertyAnimatorCreator {

    protected Element from;
    protected Element to;

    PropertyAnimatorCreator(Element from, Element to) {
        this.from = from;
        this.to = to;
    }

    public abstract boolean shouldAnimateProperty();

    public abstract Animator create(Transition transition);
}
