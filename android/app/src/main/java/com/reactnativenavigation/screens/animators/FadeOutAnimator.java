package com.reactnativenavigation.screens.animators;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.reactnativenavigation.screens.CustomAnimator;
import com.reactnativenavigation.screens.Screen;

public class FadeOutAnimator implements CustomAnimator {

    public Animator createAnimator(Bundle animation, final Screen screen, final Runnable onAnimationEnd) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(screen, View.ALPHA, 1, 0);
        fadeOut.setInterpolator(new LinearInterpolator());
        fadeOut.setDuration(animation.getInt("durationMs", DEFAULT_ANIMATION_DURATION_MS));
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                screen.setAlpha(1);
                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }
        });

        return fadeOut;
    }
}
