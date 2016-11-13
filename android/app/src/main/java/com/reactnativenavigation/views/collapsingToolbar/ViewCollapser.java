package com.reactnativenavigation.views.collapsingToolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

public class ViewCollapser {
    private static final int DURATION = 160;
    private CollapsingView view;
    private boolean animating;

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

    public void collapse(float amount) {
        view.asView().setTranslationY(amount);
    }

    private void collapseView(boolean animate, float translation) {
        if (animate) {
            animate(translation);
        } else {
            view.asView().setTranslationY(translation);
        }
    }

    private void animate(final float translation) {
        if (animating) {
            return;
        }
        view.asView().animate()
                .translationY(translation)
                .setDuration(DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        animating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animating = false;
                    }
                })
                .start();
    }
}
