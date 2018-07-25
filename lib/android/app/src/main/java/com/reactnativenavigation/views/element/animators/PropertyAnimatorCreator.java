package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.view.View;

import com.reactnativenavigation.parse.Transition;

public abstract class PropertyAnimatorCreator {

    protected View from;
    protected View to;

    PropertyAnimatorCreator(View from, View to) {
        this.from = from;
        this.to = to;
    }

    public abstract boolean shouldAnimateProperty();

    public abstract Animator create(Transition transition);
}
