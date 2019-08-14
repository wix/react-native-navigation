package com.reactnativenavigation.anim;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.topbar.TopBar;

import static android.view.View.TRANSLATION_Y;
import static com.reactnativenavigation.utils.ObjectUtils.perform;
import static com.reactnativenavigation.utils.ViewUtils.getHeight;

public class TopBarAnimator {

    private static final int DEFAULT_COLLAPSE_DURATION = 100;
    private static final int DURATION = 300;
    private static final TimeInterpolator DECELERATE = new DecelerateInterpolator();
    private static final TimeInterpolator LINEAR = new LinearInterpolator();

    private TopBar topBar;
    private String stackId;
    private Animator hideAnimator;
    private Animator showAnimator;

    public TopBarAnimator() {
    }

    TopBarAnimator(TopBar topBar) {
        this.topBar = topBar;
    }

    public void bindView(TopBar topBar, StackLayout stack) {
        this.topBar = topBar;
        stackId = stack.getStackId();
    }

    public void show(AnimationOptions options, int translationStartDy) {
        topBar.setVisibility(View.VISIBLE);
        if (options.hasValue() && (!options.id.hasValue() || options.id.get().equals(stackId))) {
            options.setValueDy(TRANSLATION_Y, -translationStartDy, 0);
            showAnimator = options.getAnimation(topBar);
        } else {
            showAnimator = getDefaultShowAnimator(translationStartDy, DECELERATE, DURATION);
        }
        showInternal();
    }

    public void show(float startTranslation) {
        showAnimator = getDefaultShowAnimator(startTranslation, LINEAR, DEFAULT_COLLAPSE_DURATION);
        showInternal();
    }

    private void showInternal() {
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                topBar.setVisibility(View.VISIBLE);
            }
        });
        if (isAnimatingHide()) hideAnimator.cancel();
        showAnimator.start();
    }

    public void hide(AnimationOptions options, Runnable onAnimationEnd, float translationStartDy, float translationEndDy) {
        if (options.hasValue() && (!options.id.hasValue() || options.id.get().equals(stackId))) {
            options.setValueDy(TRANSLATION_Y, translationStartDy, -translationEndDy);
            hideAnimator = options.getAnimation(topBar);
        } else {
            hideAnimator = getDefaultHideAnimator(translationStartDy, translationEndDy, DECELERATE, DURATION);
        }
        hideInternal(onAnimationEnd);
    }

    public void hide(float translationStart, float translationEndDy) {
        hideAnimator = getDefaultHideAnimator(translationStart, translationEndDy, LINEAR, DEFAULT_COLLAPSE_DURATION);
        hideInternal(() -> {});
    }

    private void hideInternal(Runnable onAnimationEnd) {
        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                topBar.setVisibility(View.GONE);
                onAnimationEnd.run();
            }
        });
        if (isAnimatingShow()) showAnimator.cancel();
        hideAnimator.start();
    }

    public boolean isAnimatingHide() {
        return hideAnimator != null && hideAnimator.isRunning();
    }

    public boolean isAnimatingShow() {
         return showAnimator != null && showAnimator.isRunning();
    }

    public boolean isAnimating() {
        return perform(showAnimator, false, Animator::isRunning) ||
               perform(hideAnimator, false, Animator::isRunning);
    }

    private AnimatorSet getDefaultShowAnimator(float translationStart, TimeInterpolator interpolator, int duration) {
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(topBar, TRANSLATION_Y, -getHeight(topBar) - translationStart, 0);
        showAnimator.setInterpolator(interpolator);
        showAnimator.setDuration(duration);
        AnimatorSet set = new AnimatorSet();
        set.play(showAnimator);
        return set;
    }

    private Animator getDefaultHideAnimator(float translationStart, float translationEndDy, TimeInterpolator interpolator, int duration) {
        ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(topBar, TRANSLATION_Y, translationStart, -topBar.getMeasuredHeight() - translationEndDy);
        hideAnimator.setInterpolator(interpolator);
        hideAnimator.setDuration(duration);
        return hideAnimator;
    }
}
