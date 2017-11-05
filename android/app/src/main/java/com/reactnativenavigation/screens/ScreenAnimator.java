package com.reactnativenavigation.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.sharedElementTransition.SharedElementsAnimator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

class ScreenAnimator {
    private static final int DURATION = 250;
    private static final int ALPHA_START_DELAY = 100;
    private static final int ALPHA_SHORT_DURATION = 150;
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private final float translationY;
    private final float translationX;
    private Screen screen;

    ScreenAnimator(Screen screen) {
        this.screen = screen;
        translationY = 0.08f * ViewUtils.getWindowHeight(screen.activity);
        translationX = ViewUtils.getWindowWidth(screen.activity);
    }

    public void show(boolean animate, final Runnable onAnimationEnd) {
        if (animate) {
            createShowAnimator(onAnimationEnd).start();
        } else {
            screen.setVisibility(View.VISIBLE);
            NavigationApplication.instance.runOnMainThread(onAnimationEnd, DURATION);
        }
    }

    public void show(boolean animate) {
        if (animate) {
            createShowAnimator(null).start();
        } else {
            screen.setVisibility(View.VISIBLE);
        }
    }

    public void hide(boolean animate, Runnable onAnimationEnd) {
        if (animate) {
            createHideAnimator(onAnimationEnd).start();
        } else {
            screen.setVisibility(View.INVISIBLE);
            onAnimationEnd.run();
        }
    }

    private Animator createShowAnimator(final @Nullable Runnable onAnimationEnd) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(screen, View.ALPHA, 0, 1);
        alpha.setInterpolator(DECELERATE_INTERPOLATOR);

        AnimatorSet set = new AnimatorSet();
        switch (String.valueOf(this.screen.screenParams.animationType)) {
            case "fade": {
                alpha.setDuration(DURATION);
                set.play(alpha);
                break;
            }
            case "slide-horizontal": {
                ObjectAnimator translationX = ObjectAnimator.ofFloat(screen, View.TRANSLATION_X, this.translationX, 0);
                translationX.setInterpolator(ACCELERATE_DECELERATE_INTERPOLATOR);
                translationX.setDuration(DURATION);
                set.play(translationX);
                break;
            }
            default: {
                ObjectAnimator translationY = ObjectAnimator.ofFloat(screen, View.TRANSLATION_Y, this.translationY, 0);
                translationY.setInterpolator(DECELERATE_INTERPOLATOR);
                translationY.setDuration(DURATION);
                alpha.setDuration(ALPHA_SHORT_DURATION);
                set.playTogether(translationY, alpha);
                break;
            }
        }

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                screen.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }
        });
        return set;
    }

    private Animator createHideAnimator(final Runnable onAnimationEnd) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(screen, View.ALPHA, 0);
        alpha.setInterpolator(DECELERATE_INTERPOLATOR);

        AnimatorSet set = new AnimatorSet();
        switch (String.valueOf(this.screen.screenParams.animationType)) {
            case "fade": {
                alpha.setDuration(DURATION);
                set.play(alpha);
                break;
            }
            case "slide-horizontal": {
                ObjectAnimator translationX = ObjectAnimator.ofFloat(screen, View.TRANSLATION_X, this.translationX);
                translationX.setInterpolator(ACCELERATE_INTERPOLATOR);
                translationX.setDuration(DURATION);
                set.play(translationX);
                break;
            }
            default: {
                ObjectAnimator translationY = ObjectAnimator.ofFloat(screen, View.TRANSLATION_Y, this.translationY);
                translationY.setInterpolator(ACCELERATE_INTERPOLATOR);
                translationY.setDuration(DURATION);
                alpha.setStartDelay(ALPHA_START_DELAY);
                alpha.setDuration(ALPHA_SHORT_DURATION);
                set.playTogether(translationY, alpha);
                break;
            }
        }

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }
        });
        return set;
    }

    void showWithSharedElementsTransitions(Runnable onAnimationEnd) {
        hideContentViewAndTopBar();
        screen.setVisibility(View.VISIBLE);
        new SharedElementsAnimator(this.screen.sharedElements).show(new Runnable() {
            @Override
            public void run() {
                animateContentViewAndTopBar(1, 280);
            }
        }, onAnimationEnd);
    }

    private void hideContentViewAndTopBar() {
        if (screen.screenParams.animateScreenTransitions) {
            screen.getContentView().setAlpha(0);
        }
        screen.getTopBar().setAlpha(0);
    }

    void hideWithSharedElementsTransition(Runnable onAnimationEnd) {
        new SharedElementsAnimator(screen.sharedElements).hide(new Runnable() {
            @Override
            public void run() {
                animateContentViewAndTopBar(0, 200);
            }
        }, onAnimationEnd);
    }

    private void animateContentViewAndTopBar(int alpha, int duration) {
        List<Animator> animators = new ArrayList<>();
        if (screen.screenParams.animateScreenTransitions) {
            animators.add(ObjectAnimator.ofFloat(screen.getContentView(), View.ALPHA, alpha));
        }
        animators.add(ObjectAnimator.ofFloat(screen.getTopBar(), View.ALPHA, alpha));
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setDuration(duration);
        set.start();
    }
}
