package com.reactnativenavigation.views.collapsingToolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewPropertyAnimator;

public class ViewCollapser {
    private static final int DURATION = 160;
    private CollapsingView view;
    private ViewPropertyAnimator animator;

    public ViewCollapser(CollapsingView view) {
        this.view = view;
    }

    public void collapse(CollapseAmount amount) {
        if (amount.collapseToTop()) {
            collapseView(true, view.getFinalCollapseValue());
        } else if (amount.collapseToBottom()) {
            collapseView(true, 0);
        } else {
            collapse(amount.get());
        }
    }

    private void collapseView(boolean animate, float translation) {
        if (animate) {
            animate(translation);
        } else {
            collapse(translation);
        }
    }

    public void collapse(float amount) {
        if (animator != null) {
            animator.cancel();
        }
        view.asView().setTranslationY(amount);
    }

    private void animate(final float translation) {
        animator = view.asView().animate()
                .translationY(translation)
                .setDuration(DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        animator = null;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animator = null;
                    }
                });
        animator.start();
    }
}
