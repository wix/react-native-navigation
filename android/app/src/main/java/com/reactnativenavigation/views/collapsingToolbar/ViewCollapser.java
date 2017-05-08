package com.reactnativenavigation.views.collapsingToolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;

public class ViewCollapser {
    private static final int DURATION = 160;
    public static final int FLING_DURATION = 40;
    private CollapsingView view;
    private ViewPropertyAnimator animator;
    private final ValueAnimator.AnimatorUpdateListener LISTENER =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                }
            };

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

    void fling(final CollapseAmount amount, final CollapsingTitleBar titleBar, final CollapsingTopBarReactHeader header) {
        fling(amount, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                titleBar.collapse(new CollapseAmount((Float) animation.getAnimatedValue()));
                header.collapse((Float) animation.getAnimatedValue());
            }
        });
    }

    public void fling(CollapseAmount amount) {
        fling(amount, LISTENER);
    }

    private void fling(final CollapseAmount amount, @NonNull final ValueAnimator.AnimatorUpdateListener updateListener) {
        final float translation = amount.collapseToTop() ? view.getFinalCollapseValue() : 0;
        ObjectAnimator animator = ObjectAnimator.ofFloat(view.asView(), View.TRANSLATION_Y, translation);
        animator.setDuration(FLING_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateListener.onAnimationUpdate(animation);
            }
        });
        animator.start();
    }
}
