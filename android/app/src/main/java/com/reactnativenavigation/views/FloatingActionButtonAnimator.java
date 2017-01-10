package com.reactnativenavigation.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import java.util.List;

public class FloatingActionButtonAnimator {

    private final FloatingActionButton collapsedFab;
    private final FloatingActionButton expendedFab;
    private int crossFadeAnimationDuration;

    private enum State{Showing, Idle, Removing}
    private State state = State.Idle;

    public FloatingActionButtonAnimator(FloatingActionButton collapsedFab, FloatingActionButton expendedFab, int crossFadeAnimationDuration) {
        this.collapsedFab = collapsedFab;
        this.expendedFab = expendedFab;
        this.crossFadeAnimationDuration = crossFadeAnimationDuration;
    }

    boolean isAnimating() {
        return state == State.Showing || state == State.Removing;
    }

    void show() {
        state = State.Showing;
        collapsedFab.setScaleX(0);
        collapsedFab.setScaleY(0);
        collapsedFab.animate()
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        state = State.Idle;
                    }
                })
                .setDuration(crossFadeAnimationDuration)
                .start();
    }

    public void collapse() {
        hideExpended();
        showCollapsed();
    }

    void hideCollapsed() {
        animateFab(collapsedFab, 0, 90);
    }

    void showExpended() {
        animateFab(expendedFab, 1, 0);
    }

    void showCollapsed() {
        animateFab(collapsedFab, 1, 0);
    }

    void hideExpended() {
        animateFab(expendedFab, 0, -90);
    }

    private void animateFab(final FloatingActionButton fab, final int alpha, int rotation) {
        fab.animate()
                .alpha(alpha)
                .setDuration(crossFadeAnimationDuration)
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
        state = State.Removing;
        fab.animate()
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .setDuration(crossFadeAnimationDuration)
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
                    .setDuration(crossFadeAnimationDuration)
                    .start();
        }
    }
}
