package com.reactnativenavigation.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
            view.setVisibility(View.VISIBLE);
        }
    }

    private void hide(boolean animate) {
        if (animate) {
            animator = createAnimator(false);
            animator.start();
        } else {
            view.setTranslationY(hiddenEndValue);
            view.setY(hiddenEndValue);
            view.setVisibility(View.GONE);
        }
    }

    private ObjectAnimator createAnimator(final boolean show) {
        view.setVisibility(View.VISIBLE);
        final ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, show ? SHOW_END_VALUE : hiddenEndValue);
        animator.setDuration(DURATION);
        animator.setInterpolator(interpolator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(show ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });
        return animator;
    }
}
