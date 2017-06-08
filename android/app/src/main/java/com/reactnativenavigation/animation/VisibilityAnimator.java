package com.reactnativenavigation.animation;

import android.animation.ObjectAnimator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

public class VisibilityAnimator {

    private final LinearOutSlowInInterpolator interpolator = new LinearOutSlowInInterpolator();
    private ObjectAnimator animator;

    public enum HideDirection {
        Up, Down
    }

    private static final int SHOW_END_VALUE = 0;
    private static final int DURATION = 300;

    private final View view;
    private final int hiddenEndValue;

    public VisibilityAnimator(View view, HideDirection hideDirection, int height) {
        this.view = view;
        this.hiddenEndValue = hideDirection == HideDirection.Up ? -height : height;
    }

    public void setVisible(boolean visible, boolean animate) {
        cancelAnimator();
        if (visible) {
            show(animate);
        } else {
            hide(animate);
        }
    }

    private void cancelAnimator() {
        if (animator != null && animator.isRunning()) {
            view.clearAnimation();
            animator.cancel();
        }
    }

    private void show(boolean animate) {
        if (animate) {
            animator = createAnimator(true);
            animator.start();
        } else {
            view.setTranslationY(SHOW_END_VALUE);
            view.setY(SHOW_END_VALUE);
        }
    }

    private void hide(boolean animate) {
        if (animate) {
            animator = createAnimator(false);
            animator.start();
        } else {
            view.setTranslationY(hiddenEndValue);
            view.setY(hiddenEndValue);
        }
    }

    private ObjectAnimator createAnimator(final boolean show) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, show ? SHOW_END_VALUE : hiddenEndValue);
        animator.setDuration(DURATION);
        animator.setInterpolator(interpolator);
        return animator;
    }
}
