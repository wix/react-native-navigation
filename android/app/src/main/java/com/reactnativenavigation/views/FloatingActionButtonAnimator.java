package com.reactnativenavigation.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

public class FloatingActionButtonAnimator {
    private static final int SHOW_DURATION = 120;
    private static final int HIDE_DURATION = 120;
    private static final float SCALE = 0.6f;
    private static final int ANGLE = 90;
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator(1.5f);

    private final FloatingActionButton collapsedFab;
    private final FloatingActionButton expendedFab;

    FloatingActionButtonAnimator(FloatingActionButton collapsedFab, FloatingActionButton expendedFab) {
        this.collapsedFab = collapsedFab;
        this.expendedFab = expendedFab;
    }

    void show() {
        collapsedFab.setScaleX(SCALE);
        collapsedFab.setScaleY(SCALE);
        collapsedFab.setAlpha(0.0f);
        collapsedFab.setAlpha(0.0f);
        collapsedFab.animate()
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(DECELERATE_INTERPOLATOR)
                .setDuration(SHOW_DURATION)
                .start();
    }

    public void collapse() {
        hideExpended();
        showCollapsed();
    }

    void hideCollapsed() {
        animateFab(collapsedFab, 0, ANGLE);
    }

    void showExpended() {
        animateFab(expendedFab, 1, 0);
    }

    private void showCollapsed() {
        animateFab(collapsedFab, 1, 0);
        collapsedFab.bringToFront();
    }

    private void hideExpended() {
        animateFab(expendedFab, 0, -ANGLE);
    }

    private void animateFab(final FloatingActionButton fab, final int alpha, int rotation) {
        fab.animate()
                .alpha(alpha)
                .setDuration(HIDE_DURATION)
                .rotation(rotation)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (fab.getVisibility() == View.GONE) {
                            fab.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fab.setVisibility(alpha == 0 ? View.GONE : View.VISIBLE);
                    }
                })
                .start();
    }

    void removeFabFromScreen(FloatingActionButton fab, final AnimatorListenerAdapter animationListener) {
        if (fab == null) {
            return;
        }
        fab.animate()
                .alpha(0)
                .scaleX(SCALE)
                .scaleY(SCALE)
                .setDuration(HIDE_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animationListener != null) {
                            animationListener.onAnimationEnd(animation);
                        }
                    }
                })
                .start();
    }

    void removeActionsFromScreen(List<FloatingActionButton> actions) {
        for (FloatingActionButton action : actions) {
            action.animate()
                    .alpha(0)
                    .scaleX(0)
                    .scaleY(0)
                    .setDuration(HIDE_DURATION)
                    .start();
        }
    }
}
