package com.reactnativenavigation.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.screens.animators.FadeInAnimator;
import com.reactnativenavigation.screens.animators.FadeOutAnimator;
import com.reactnativenavigation.screens.animators.SlideInFromRightAnimator;
import com.reactnativenavigation.screens.animators.SlideOutFromLeftAnimator;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.sharedElementTransition.SharedElementsAnimator;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

class ScreenAnimator {
    private static final String FADE_IN_ANIMATION = "fadeIn";
    private static final String FADE_OUT_ANIMATION = "fadeOut";
    private static final String SLIDE_IN_FROM_RIGHT_ANIMATION = "slideInFromRight";
    private static final String SLIDE_OUT_FROM_LEFT_ANIMATION = "slideOutFromLeft";

    private final float translationY;
    private Screen screen;

    private static Map<String, CustomAnimator> customAnimators = new HashMap<>();

    ScreenAnimator(Screen screen) {
        this.screen = screen;
        translationY = 0.08f * ViewUtils.getScreenHeight();
    }

    /**
     * Method that tries to load and instantiate CustomAnimator based on fully qualified name
     * @param customAnimator fully qualified name of CustomAnimator
     * @return instance of CustomAnimator or null
     */
    private CustomAnimator loadCustomAnimator(String customAnimator) {
        Class clazz;
        try {
            clazz = Class.forName(customAnimator);
            return (CustomAnimator) clazz.newInstance();
        } catch (Exception e) {
            Log.e("loadCustomAnimator", "error loading CustomAnimator class '" + customAnimator + "'. " + e.getCause().getMessage(), e);
            return null;
        }
    }

    /**
     * Method that returns animator identified by androidCustomAnimator bundle param, if applicable.
     * @param animation bundle with attributes for CustomAnimator
     * @param onAnimationEnd optional task to be run when screen is shown
     * @return instance of CustomAnimator or null
     */
    private Animator resolveCustomAnimator(Bundle animation, Runnable onAnimationEnd) {
        if (animation != null && animation.containsKey("androidCustomAnimator")) {
            String androidCustomAnimator = animation.getString("androidCustomAnimator");
            if (customAnimators == null) {
                customAnimators = new HashMap<>();
            }
            // use cached copy
            if (customAnimators.containsKey(androidCustomAnimator)) {
                return customAnimators.get(androidCustomAnimator).createAnimator(animation, screen, onAnimationEnd);
            }
            CustomAnimator customAnimator = loadCustomAnimator(androidCustomAnimator);
            if (customAnimator != null) {
                customAnimators.put(androidCustomAnimator, customAnimator);
                return customAnimator.createAnimator(animation, screen, onAnimationEnd);
            }
        }
        return null;
    }

    /**
     * Method that returns animator based on showScreenAnimation param. First it looks into built-in ones, then custom one and fallbacks to default one
     * @param showScreenAnimation bundle with attributes for CustomAnimator
     * @param onAnimationEnd optional task to be run when screen is shown
     * @return animator to be used
     */
    private Animator resolveShowAnimator(Bundle showScreenAnimation, Runnable onAnimationEnd) {
        if (showScreenAnimation != null && showScreenAnimation.containsKey("type")) {
            switch (showScreenAnimation.getString("type")) {
                case FADE_IN_ANIMATION: return new FadeInAnimator().createAnimator(showScreenAnimation, screen, onAnimationEnd);
                case SLIDE_IN_FROM_RIGHT_ANIMATION: return new SlideInFromRightAnimator().createAnimator(showScreenAnimation, screen, onAnimationEnd);
            }
        }
        Animator customAnimator = resolveCustomAnimator(showScreenAnimation, onAnimationEnd);
        if (customAnimator != null) {
            return customAnimator;
        }
        return createShowAnimator(onAnimationEnd);
    }

    /**
     * Method that shows the new view, with optional animation and task to run when screen is shown
     * @param animate flag whether to animate showing process or show straight away
     * @param showScreenAnimation optional bundle with attributes for CustomAnimator
     * @param onAnimationEnd optional task to be run when screen is shown
     */
    public void show(boolean animate, Bundle showScreenAnimation, final Runnable onAnimationEnd) {
        if (animate) {
            resolveShowAnimator(showScreenAnimation, onAnimationEnd).start();
        } else {
            screen.setVisibility(View.VISIBLE);
            if (onAnimationEnd != null) {
                NavigationApplication.instance.runOnMainThread(onAnimationEnd, 200);
            }
        }
    }

    /**
     * Method that returns animator based on hideScreenAnimation param. First it looks into built-in ones, then custom one and fallbacks to default one
     * @param hideScreenAnimation bundle with attributes for CustomAnimator
     * @param onAnimationEnd optional task to be run when screen is hidden
     * @return animator to be used
     */
    private Animator resolveHideAnimator(Bundle hideScreenAnimation, Runnable onAnimationEnd) {
        if (hideScreenAnimation != null && hideScreenAnimation.containsKey("type")) {
            switch (hideScreenAnimation.getString("type")) {
                case FADE_OUT_ANIMATION: return new FadeOutAnimator().createAnimator(hideScreenAnimation, screen, onAnimationEnd);
                case SLIDE_OUT_FROM_LEFT_ANIMATION: return new SlideOutFromLeftAnimator().createAnimator(hideScreenAnimation, screen, onAnimationEnd);
            }
        }
        Animator customAnimation = resolveCustomAnimator(hideScreenAnimation, onAnimationEnd);
        if (customAnimation != null) {
            return customAnimation;
        }
        return createHideAnimator(onAnimationEnd);
    }

    /**
     * Method that hides the old view, with optional animation and task to run when screen is hidden
     * @param animate flag whether to animate hiding process or hide straight away
     * @param hideScreenAnimation optional bundle with attributes for CustomAnimator
     * @param onAnimationEnd optional task to be run when screen is hidden
     */
    public void hide(boolean animate, Bundle hideScreenAnimation, Runnable onAnimationEnd) {
        if (animate) {
            resolveHideAnimator(hideScreenAnimation, onAnimationEnd).start();
        } else {
            screen.setVisibility(View.INVISIBLE);
            if (onAnimationEnd != null) {
                onAnimationEnd.run();
            }
        }
    }

    /**
     * Default show screen animation
     * @param onAnimationEnd optional task to be run when screen is shown
     * @return default animator
     */
    private Animator createShowAnimator(final @Nullable Runnable onAnimationEnd) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(screen, View.ALPHA, 0, 1);
        alpha.setInterpolator(new DecelerateInterpolator());
        alpha.setDuration(200);

        ObjectAnimator translationY = ObjectAnimator.ofFloat(screen, View.TRANSLATION_Y, this.translationY, 0);
        translationY.setInterpolator(new DecelerateInterpolator());
        translationY.setDuration(280);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(translationY, alpha);
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

    /**
     * Default hide screen animation
     * @param onAnimationEnd optional task to be run when screen is hidden
     * @return default animator
     */
    private Animator createHideAnimator(final Runnable onAnimationEnd) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(screen, View.ALPHA, 0);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setStartDelay(100);
        alpha.setDuration(150);

        ObjectAnimator translationY = ObjectAnimator.ofFloat(screen, View.TRANSLATION_Y, this.translationY);
        translationY.setInterpolator(new AccelerateInterpolator());
        translationY.setDuration(250);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(translationY, alpha);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
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
