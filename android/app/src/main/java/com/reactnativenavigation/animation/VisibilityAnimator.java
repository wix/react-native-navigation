package com.reactnativenavigation.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
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

    public void setVisible(boolean visible, boolean animate, @Nullable Runnable onAnimationEnd) {
        cancelAnimator();
        if (visible) {
            show(animate, onAnimationEnd);
        } else {
            hide(animate, onAnimationEnd);
        }
    }

    private void cancelAnimator() {
        if (animator != null && animator.isRunning()) {
            view.clearAnimation();
            animator.cancel();
        }
    }

    private void show(boolean animate, @Nullable Runnable onAnimationEnd) {
        if (animate) {
            animator = createAnimator(true, onAnimationEnd);
            animator.start();
        } else {
            view.setTranslationY(SHOW_END_VALUE);
            view.setVisibility(View.VISIBLE);
            if (onAnimationEnd != null) onAnimationEnd.run();
        }
    }

    private void hide(boolean animate, @Nullable Runnable onAnimationEnd) {
        if (animate) {
            animator = createAnimator(false, onAnimationEnd);
            animator.start();
        } else {
            view.setTranslationY(hiddenEndValue);
            view.setVisibility(View.GONE);
            if (onAnimationEnd != null) onAnimationEnd.run();
        }
    }

    private ObjectAnimator createAnimator(final boolean show, @Nullable final Runnable onAnimationEnd) {
        view.setVisibility(View.VISIBLE);
        final ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, show ? SHOW_END_VALUE : hiddenEndValue);
        animator.setDuration(DURATION);
        animator.setInterpolator(interpolator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimationEnd != null) onAnimationEnd.run();
            }
        });
        return animator;
    }
}
