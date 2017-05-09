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
    private static final int FLING_DURATION = 160;
    private CollapsingView view;

    private final ValueAnimator.AnimatorUpdateListener LISTENER =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                }
            };
    private ViewPropertyAnimator animator;
    private ObjectAnimator flingAnimator;

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
        cancelAnimator();
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
        cancelAnimator();
        final float translation = amount.collapseToTop() ? view.getFinalCollapseValue() : 0;
        flingAnimator = ObjectAnimator.ofFloat(view.asView(), View.TRANSLATION_Y, translation);
        flingAnimator.setDuration(FLING_DURATION);
        flingAnimator.setInterpolator(new DecelerateInterpolator());
        flingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateListener.onAnimationUpdate(animation);
            }
        });
        flingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateListener.onAnimationUpdate(animation);
            }
        });
        flingAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                flingAnimator = null;
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                flingAnimator = null;
            }
        });
        flingAnimator.start();

    }

    private void cancelAnimator() {
        if (animator != null) {
            animator.cancel();
        }
    }
}
