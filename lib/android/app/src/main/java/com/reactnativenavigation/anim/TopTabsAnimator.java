package com.reactnativenavigation.anim;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.views.topbar.TopBar;
import com.reactnativenavigation.views.toptabs.TopTabs;

import javax.annotation.Nullable;

public class TopTabsAnimator {

    private static final int DEFAULT_COLLAPSE_DURATION = 100;
    private static final int DURATION_TOPBAR = 300;
    private final DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private final AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();

    private TopTabs topTabs;
    private String stackId;
    private AnimatorSet hideAnimator;
    private AnimatorSet showAnimator;

    public TopTabsAnimator(TopTabs topTabs) {
        this.topTabs = topTabs;
    }

    public TopTabsAnimator(TopTabs topTabs, @Nullable String stackId) {
        this.topTabs = topTabs;
        this.stackId = stackId;
    }

    public void show(AnimationOptions options) {
        if (options.hasValue() && (!options.id.hasValue() || options.id.get().equals(stackId))) {
            showAnimator = options.getAnimation(topTabs);
        } else {
            showAnimator = getDefaultShowAnimator(-1 * topTabs.getMeasuredHeight(), decelerateInterpolator, DURATION_TOPBAR);
        }
        show();
    }

    public void show(float startTranslation) {
        showAnimator = getDefaultShowAnimator(startTranslation, null, DEFAULT_COLLAPSE_DURATION);
        show();
    }

    private void show() {
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                topTabs.setVisibility(View.VISIBLE);
            }
        });
        showAnimator.start();
    }

    private AnimatorSet getDefaultShowAnimator(float startTranslation, TimeInterpolator interpolator, int duration) {
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(topTabs, View.TRANSLATION_Y, startTranslation, 0);
        showAnimator.setInterpolator(interpolator);
        showAnimator.setDuration(duration);
        AnimatorSet set = new AnimatorSet();
        set.play(showAnimator);
        return set;
    }

    public void hide(AnimationOptions options, AnimationListener listener) {
        if (options.hasValue() && (!options.id.hasValue() || options.id.get().equals(stackId))) {
            hideAnimator = options.getAnimation(topTabs);
        } else {
            hideAnimator = getDefaultHideAnimator(0, accelerateInterpolator, DURATION_TOPBAR);
        }
        hide(listener);
    }

    void hide(float startTranslation) {
        hideAnimator = getDefaultHideAnimator(startTranslation, null, DEFAULT_COLLAPSE_DURATION);
        hide(null);
    }

    private void hide(AnimationListener listener) {
        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                topTabs.setVisibility(View.GONE);
                if (listener != null) listener.onAnimationEnd();
            }
        });
        hideAnimator.start();
    }

    private AnimatorSet getDefaultHideAnimator(float startTranslation, TimeInterpolator interpolator, int duration) {
        ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(topTabs, View.TRANSLATION_Y, startTranslation, -1 * topTabs.getMeasuredHeight());
        hideAnimator.setInterpolator(interpolator);
        hideAnimator.setDuration(duration);
        AnimatorSet set = new AnimatorSet();
        set.play(hideAnimator);
        return set;
    }

    public boolean isRunning() {
        return (hideAnimator != null && hideAnimator.isRunning()) || (showAnimator != null && showAnimator.isRunning());
    }
}
