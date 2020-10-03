package com.reactnativenavigation.viewcontrollers.bottomtabs;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

import com.reactnativenavigation.options.AnimationsOptions;
import com.reactnativenavigation.views.bottomtabs.BottomTabs;

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
                    bottomTabs.hideBottomNavigation(false);
                }
            });
            set.start();
        } else {
            bottomTabs.hideBottomNavigation();
        }
    }

    public void show(AnimationsOptions animationsOptions) {
        if (animationsOptions.push.bottomTabs.hasValue()) {
            AnimatorSet set = animationsOptions.push.bottomTabs.getAnimation(bottomTabs);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    bottomTabs.restoreBottomNavigation(false);
                }
            });
            set.start();
        } else {
            bottomTabs.restoreBottomNavigation();
        }
    }

}
