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
    private DecelerateInterpolator decelerateInterpolator;
    private AccelerateInterpolator accelerateInterpolator;

    public TopBarAnimator() {
        decelerateInterpolator = new DecelerateInterpolator();
        accelerateInterpolator = new AccelerateInterpolator();
    }

    public void animateShowTopBar(final TopBar topBar, final View contentView) {
        animateShowTopBar(topBar, contentView, -1 * topBar.getMeasuredHeight(), decelerateInterpolator, DURATION_TOPBAR);
    }

    public void animateShowTopBar(final TopBar topBar, final View container, float startTranslation, TimeInterpolator interpolator, int duration) {
        ObjectAnimator topbarAnim = ObjectAnimator.ofFloat(topBar, View.TRANSLATION_Y, startTranslation, 0);
        topbarAnim.setInterpolator(interpolator);
        topbarAnim.setDuration(duration);

        topbarAnim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                topBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (container != null) {
                    ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    container.setLayoutParams(layoutParams);
                }
            }
        });
        topbarAnim.start();
    }

    public void animateHideTopBar(final TopBar topBar, final View contentView) {
        animateHideTopBar(topBar, contentView, 0, accelerateInterpolator, DURATION_TOPBAR);
    }

    public void animateHideTopBar(final TopBar topBar, final View container, float startTranslation, TimeInterpolator interpolator, int duration) {
        ObjectAnimator topbarAnim = ObjectAnimator.ofFloat(topBar, View.TRANSLATION_Y, startTranslation, -1 * topBar.getMeasuredHeight());
        topbarAnim.setInterpolator(interpolator);
        topbarAnim.setDuration(duration);

        topbarAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (container != null) {
                    ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    container.setLayoutParams(layoutParams);
                }

                topBar.setVisibility(View.GONE);
            }
        });
        topbarAnim.start();
    }
}
