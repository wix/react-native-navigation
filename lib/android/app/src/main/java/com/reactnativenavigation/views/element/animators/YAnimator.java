package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.view.View;

import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.utils.ViewUtils;

public class YAnimator extends PropertyAnimatorCreator {

    private final int dy;

    public YAnimator(View from, View to) {
        super(from, to);
        final Point fromXy = ViewUtils.getLocationOnScreen(from);
        final Point toXy = ViewUtils.getLocationOnScreen(to);
        dy = fromXy.y - toXy.y;
    }

    @Override
    public boolean shouldAnimateProperty() {
        return dy != 0;
    }

    @Override
    public Animator create(Transition transition) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(to, View.TRANSLATION_Y, dy, 0);
        animator.setDuration(transition.duration.get());
        return animator;
    }
}
