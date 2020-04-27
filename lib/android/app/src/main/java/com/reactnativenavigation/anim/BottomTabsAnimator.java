package com.reactnativenavigation.anim;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.views.BottomTabs;

public class BottomTabsAnimator {

    private BottomTabs bottomTabs;

    public BottomTabsAnimator(BottomTabs bottomTabs) {
        this.bottomTabs = bottomTabs;
    }

    public void hide(AnimationsOptions animationsOptions) {
        if (animationsOptions.pop.bottomTabs.hasValue()) {
            AnimatorSet set = animationsOptions.pop.bottomTabs.getAnimation(bottomTabs);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    bottomTabs.hide();
                }
            });
            set.start();
        } else {
            bottomTabs.hide();
        }
    }

    public void show(AnimationsOptions animationsOptions) {
        if (animationsOptions.push.bottomTabs.hasValue()) {
            AnimatorSet set = animationsOptions.push.bottomTabs.getAnimation(bottomTabs);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    bottomTabs.show();
                }
            });
            set.start();
        } else {
            bottomTabs.show();
        }
    }

}
