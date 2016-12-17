package com.reactnativenavigation.views.sharedElementTransition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SharedElementAnimatorCreator {
    private final SharedElementTransition from;
    private final SharedElementTransition to;

    public SharedElementAnimatorCreator(SharedElementTransition from, SharedElementTransition to) {
        this.from = from;
        this.to = to;
    }

    @NonNull
    public List<Animator> create() {
        List<Animator> result = new ArrayList<>();
        createXAnimator(result);
        createYAnimator(result);
        createScaleXAnimator(result);
        createScaleYAnimator(result);
        return result;
    }

    private void createXAnimator(List<Animator> result) {
        final float fromX = from.getX();
        final float toX = to.getX();
        if (fromX != toX) {
            result.add(ObjectAnimator.ofFloat(to, View.X, fromX, toX));
        }
    }

    private void createYAnimator(List<Animator> result) {
        final float fromY = from.getY();
        final float toY = to.getY();
        if (fromY != toY) {
            result.add(ObjectAnimator.ofFloat(to, View.Y, fromY, toY));
        }
    }

    private void createScaleXAnimator(List<Animator> result) {
        final int fromWidth = from.getWidth();
        final int toWidth = to.getWidth();
        if (fromWidth != toWidth) {
            to.setPivotX(from.getX());
            result.add(ObjectAnimator.ofFloat(to, View.SCALE_X, (float) (fromWidth / toWidth), 1));
        }
    }

    private void createScaleYAnimator(List<Animator> result) {
        final int fromHeight = from.getWidth();
        final int toHeight = to.getWidth();
        if (fromHeight != toHeight) {
            to.setPivotY(from.getY());
            result.add(ObjectAnimator.ofFloat(to, View.SCALE_Y, (float) (fromHeight / toHeight), 1));
        }
    }
}
