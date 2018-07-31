package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.view.View;

import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.element.Element;

public class YAnimator extends PropertyAnimatorCreator<View> {

    private final int dy;

    public YAnimator(Element from, Element to) {
        super(from, to);
        final Point fromXy = ViewUtils.getLocationOnScreen(from.getChild());
        final Point toXy = ViewUtils.getLocationOnScreen(to.getChild());
        dy = fromXy.y - toXy.y;
    }

    @Override
    public boolean shouldAnimateProperty(View fromChild, View toChild) {
        return dy != 0;
    }

    @Override
    public Animator create() {
        return ObjectAnimator.ofFloat(to.getChild(), View.TRANSLATION_Y, dy, 0);
    }
}
