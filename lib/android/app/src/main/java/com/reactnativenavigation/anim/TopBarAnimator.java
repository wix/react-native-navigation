package com.reactnativenavigation.anim;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.reactnativenavigation.views.TopBar;

public class TopBarAnimator {

    private static final int DURATION_TOPBAR = 300;
    private final DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private final AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();

    private TopBar topBar;
    private View contentView;
    private ObjectAnimator hideAnimator;
    private ObjectAnimator showAnimator;

    public TopBarAnimator(TopBar topBar) {
        this.topBar = topBar;
    }

    public void show() {
        show(-1 * topBar.getMeasuredHeight(), decelerateInterpolator, DURATION_TOPBAR);
    }

    public void show(float startTranslation, TimeInterpolator interpolator, int duration) {
        showAnimator = ObjectAnimator.ofFloat(topBar, View.TRANSLATION_Y, startTranslation, 0);
        showAnimator.setInterpolator(interpolator);
        showAnimator.setDuration(duration);

        showAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                topBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (contentView != null) {
                    ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    contentView.setLayoutParams(layoutParams);
                }
            }
        });
        showAnimator.start();
    }

    public void hide() {
        hide(0, accelerateInterpolator, DURATION_TOPBAR);
    }

    void hide(float startTranslation, TimeInterpolator interpolator, int duration) {
        hideAnimator = ObjectAnimator.ofFloat(topBar, View.TRANSLATION_Y, startTranslation, -1 * topBar.getMeasuredHeight());
        hideAnimator.setInterpolator(interpolator);
        hideAnimator.setDuration(duration);

        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (contentView != null) {
                    ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    contentView.setLayoutParams(layoutParams);
                }

                topBar.setVisibility(View.GONE);
            }
        });
        hideAnimator.start();
    }

    public boolean isRunning() {
        return (hideAnimator != null && hideAnimator.isRunning()) || (showAnimator != null && showAnimator.isRunning());
    }
}
