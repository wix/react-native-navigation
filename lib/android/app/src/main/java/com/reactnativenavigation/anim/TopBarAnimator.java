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

    private TopBar topBar;
    private View contentView;

    public TopBarAnimator(TopBar topBar, View contentView) {
        decelerateInterpolator = new DecelerateInterpolator();
        accelerateInterpolator = new AccelerateInterpolator();
        this.topBar = topBar;
        this.contentView = contentView;
    }

    public void show() {
        show(-1 * topBar.getMeasuredHeight(), decelerateInterpolator, DURATION_TOPBAR);
    }

    public void show(float startTranslation, TimeInterpolator interpolator, int duration) {
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
                if (contentView != null) {
                    ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    contentView.setLayoutParams(layoutParams);
                }
            }
        });
        topbarAnim.start();
    }

    public void hide() {
        hide(0, accelerateInterpolator, DURATION_TOPBAR);
    }

    public void hide(float startTranslation, TimeInterpolator interpolator, int duration) {
        ObjectAnimator topbarAnim = ObjectAnimator.ofFloat(topBar, View.TRANSLATION_Y, startTranslation, -1 * topBar.getMeasuredHeight());
        topbarAnim.setInterpolator(interpolator);
        topbarAnim.setDuration(duration);

        topbarAnim.addListener(new AnimatorListenerAdapter() {
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
        topbarAnim.start();
    }
}
