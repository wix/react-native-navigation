package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.view.View;

import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.element.Element;

public class XAnimator extends PropertyAnimatorCreator {

    private final int dx;

    public XAnimator(Element from, Element to) {
        super(from, to);
        final Point fromXy = ViewUtils.getLocationOnScreen(from.getChild());
        final Point toXy = ViewUtils.getLocationOnScreen(to.getChild());
        dx = fromXy.x - toXy.x;
    }

    @Override
    public boolean shouldAnimateProperty() {
        return dx != 0;
    }

    @Override
    public Animator create(Transition transition) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(to.getChild(), View.TRANSLATION_X, dx, 0);
        animator.setDuration(transition.duration.get());
        return animator;
    }
}
