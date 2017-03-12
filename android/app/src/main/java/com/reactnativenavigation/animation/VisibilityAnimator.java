package com.reactnativenavigation.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

public class VisibilityAnimator {

    public enum HideDirection {
        Up, Down
    }

    private enum VisibilityState {
        Hidden, AnimateHide, Shown, AnimateShow
    }

    private static final int SHOW_END_VALUE = 0;
    private static final int DURATION = 300;

    private final View view;
    private final int hiddenEndValue;
    private VisibilityState visibilityState = VisibilityState.Shown;

    public VisibilityAnimator(View view, HideDirection hideDirection, int height) {
        this.view = view;
        this.hiddenEndValue = hideDirection == HideDirection.Up ? -height : height;
    }

    public void setVisible(boolean visible, boolean animate) {
        if (visible && isHiding()) {
            show(animate);
        } else if (!visible && isShowing()) {
            hide(animate);
        }
    }

    private void show(boolean animate) {
        if (animate) {
            ObjectAnimator animator = createAnimator(true);
            animator.start();
        } else {
            visibilityState = VisibilityState.Shown;
            view.setVisibility(View.VISIBLE);
        }
    }

    private void hide(boolean animate) {
        if (animate) {
            ObjectAnimator animator = createAnimator(false);
            animator.start();
        } else {
            visibilityState = VisibilityState.Hidden;
            view.setVisibility(View.GONE);
        }
    }

    private boolean isShowing() {
        return visibilityState == VisibilityState.Shown || visibilityState == VisibilityState.AnimateShow;
    }

    private boolean isHiding() {
        return visibilityState == VisibilityState.Hidden || visibilityState == VisibilityState.AnimateHide;
    }

    private ObjectAnimator createAnimator(final boolean show) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, show ? SHOW_END_VALUE : hiddenEndValue);
        animator.setDuration(DURATION);
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                visibilityState = show ? VisibilityState.AnimateShow : VisibilityState.AnimateHide;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                visibilityState = show ? VisibilityState.Shown : VisibilityState.Hidden;
            }
        });
        return animator;
    }
}
