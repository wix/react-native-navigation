package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.ViewGroup;

import com.facebook.react.views.text.ReactTextView;
import com.facebook.react.views.view.ReactViewBackgroundDrawable;
import com.reactnativenavigation.utils.ColorUtils;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.element.Element;

import java.util.Collections;
import java.util.List;

public class BackgroundColorAnimator extends PropertyAnimatorCreator<ViewGroup> {

    public BackgroundColorAnimator(Element from, Element to) {
        super(from, to);
    }

    @Override
    public boolean shouldAnimateProperty(ViewGroup fromChild, ViewGroup toChild) {
        return fromChild.getBackground() instanceof ReactViewBackgroundDrawable &&
               toChild.getBackground() instanceof ReactViewBackgroundDrawable &&
               ((ReactViewBackgroundDrawable) fromChild.getBackground()).getColor() != ((ReactViewBackgroundDrawable) toChild.getBackground()).getColor();
    }

    @Override
    protected List<Class> excludedViews() {
        return Collections.singletonList(ReactTextView.class);
    }

    @Override
    public Animator create() {
        return ObjectAnimator.ofObject(
                to,
                "backgroundColor",
                new LabColorEvaluator(),
                ColorUtils.colorToLAB(ViewUtils.getBackgroundColor(from.getChild())),
                ColorUtils.colorToLAB(ViewUtils.getBackgroundColor(to.getChild()))
        );
    }
}
