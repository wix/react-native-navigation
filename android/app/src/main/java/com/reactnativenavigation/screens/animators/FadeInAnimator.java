package com.reactnativenavigation.screens.animators;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.reactnativenavigation.screens.CustomAnimator;
import com.reactnativenavigation.screens.Screen;

public class FadeInAnimator implements CustomAnimator {

    public Animator createAnimator(Bundle animation, final Screen screen, final Runnable onAnimationEnd) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(screen, View.ALPHA, 0, 1);
        fadeIn.setInterpolator(new LinearInterpolator());
        fadeIn.setDuration(animation.getInt("durationMs", DEFAULT_ANIMATION_DURATION_MS));
        fadeIn.addListener(new AnimatorListenerAdapter() {
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

        return fadeIn;
    }
}
