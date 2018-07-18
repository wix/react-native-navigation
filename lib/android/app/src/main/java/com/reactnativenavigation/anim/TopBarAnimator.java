package com.reactnativenavigation.anim;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.views.topbar.TopBar;

import javax.annotation.Nullable;

import static android.view.View.TRANSLATION_Y;

public class TopBarAnimator {

    private static final int DEFAULT_COLLAPSE_DURATION = 100;
    private static final int DURATION = 300;
    private static final TimeInterpolator DECELERATE = new DecelerateInterpolator();
    private static final TimeInterpolator LINEAR = new LinearInterpolator();
    private static final TimeInterpolator ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();

    private TopBar topBar;
    private String stackId;
    private Animator hideAnimator;
    private Animator showAnimator;

    public TopBarAnimator(TopBar topBar) {
        this.topBar = topBar;
    }

    public TopBarAnimator(TopBar topBar, @Nullable String stackId) {
        this.topBar = topBar;
        this.stackId = stackId;
    }

    public void show(AnimationOptions options) {
        if (options.hasValue() && (!options.id.hasValue() || options.id.get().equals(stackId))) {
            showAnimator = options.getAnimation(topBar);
        } else {
            showAnimator = getDefaultShowAnimator(-1 * topBar.getMeasuredHeight(), DECELERATE, DURATION);
        }
        show();
    }

    public void show(float startTranslation) {
        showAnimator = getDefaultShowAnimator(startTranslation, LINEAR, DEFAULT_COLLAPSE_DURATION);
        show();
    }

    private void show() {
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                topBar.setVisibility(View.VISIBLE);
            }
        });
        topBar.resetAnimationOptions();
        showAnimator.start();
    }

    private AnimatorSet getDefaultShowAnimator(float startTranslation, TimeInterpolator interpolator, int duration) {
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(topBar, TRANSLATION_Y, startTranslation, 0);
        showAnimator.setInterpolator(interpolator);
        showAnimator.setDuration(duration);
        AnimatorSet set = new AnimatorSet();
        set.play(showAnimator);
        return set;
    }

    public void hide(AnimationOptions options, Runnable onAnimationEnd) {
        if (options.hasValue() && (!options.id.hasValue() || options.id.get().equals(stackId))) {
            hideAnimator = options.getAnimation(topBar);
        } else {
            hideAnimator = getDefaultHideAnimator(0, ACCELERATE_DECELERATE, DURATION);
        }
        hide(onAnimationEnd);
    }

    void hide(float startTranslation) {
        hideAnimator = getDefaultHideAnimator(startTranslation, LINEAR, DEFAULT_COLLAPSE_DURATION);
        hide(() -> {});
    }

    private void hide(Runnable onAnimationEnd) {
        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                topBar.setVisibility(View.GONE);
                onAnimationEnd.run();
            }
        });
        hideAnimator.start();
    }

    private Animator getDefaultHideAnimator(float startTranslation, TimeInterpolator interpolator, int duration) {
        ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(topBar, TRANSLATION_Y, startTranslation, -1 * topBar.getMeasuredHeight());
        hideAnimator.setInterpolator(interpolator);
        hideAnimator.setDuration(duration);
        return hideAnimator;
    }

    public boolean isAnimatingHide() {
        return hideAnimator != null && hideAnimator.isRunning();
    }

    public boolean isAnimatingShow() {
         return showAnimator != null && showAnimator.isRunning();
    }
}
