package com.reactnativenavigation.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.sharedElementTransition.SharedElementsAnimator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

class ScreenAnimator {
    private final float translationY;
    private final float translationX;
    private Screen screen;

    ScreenAnimator(Screen screen) {
        this.screen = screen;
        translationY = 0.08f * ViewUtils.getScreenHeight();
        translationX = 0.08f * ViewUtils.getScreenWidth();
    }

    public void show(boolean animate, final Runnable onAnimationEnd) {
        if (animate) {
            createShowAnimator(onAnimationEnd).start();
        } else {
            screen.setVisibility(View.VISIBLE);
            NavigationApplication.instance.runOnMainThread(onAnimationEnd, 200);
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
        alpha.setInterpolator(new DecelerateInterpolator());
        alpha.setDuration(200);

        AnimatorSet set = new AnimatorSet();
        switch (String.valueOf(this.screen.screenParams.animationType)) {
            case "fade": {
                set.play(alpha);
                break;
            }
            case "slide-horizontal": {
                ObjectAnimator translationX = ObjectAnimator.ofFloat(screen, View.TRANSLATION_X, this.translationX, 0);
                translationX.setInterpolator(new DecelerateInterpolator());
                translationX.setDuration(280);

                set.playTogether(translationX, alpha);
                break;
            }
            default: {
                ObjectAnimator translationY = ObjectAnimator.ofFloat(screen, View.TRANSLATION_Y, this.translationY, 0);
                translationY.setInterpolator(new DecelerateInterpolator());
                translationY.setDuration(280);

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
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setStartDelay(100);
        alpha.setDuration(150);

        AnimatorSet set = new AnimatorSet();
        switch (String.valueOf(this.screen.screenParams.animationType)) {
            case "fade": {
                set.play(alpha);
                break;
            }
            case "slide-horizontal": {
                ObjectAnimator translationX = ObjectAnimator.ofFloat(screen, View.TRANSLATION_X, this.translationX);
                translationX.setInterpolator(new AccelerateInterpolator());
                translationX.setDuration(250);

                set.playTogether(translationX, alpha);
                break;
            }
            default: {
                ObjectAnimator translationY = ObjectAnimator.ofFloat(screen, View.TRANSLATION_Y, this.translationY);
                translationY.setInterpolator(new AccelerateInterpolator());
                translationY.setDuration(250);

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
