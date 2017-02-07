package com.reactnativenavigation.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.reactnativenavigation.params.SlidingOverlayParams.Position;

import static android.view.View.TRANSLATION_Y;

public class PeekingAnimator {

    private static final int SLIDE_OUT_DURATION = 300;
    private static final int SLIDE_IN_DURATION = 600;

    private final Animator animator;

    public PeekingAnimator(View view, Position position, final boolean show) {
        final int offsetPixels = view.getHeight() * (position == Position.Top ? -1 : 1);

        this.animator = show ?
                createSlideInAnimator(view, offsetPixels) :
                createSlideOutAnimator(view, offsetPixels);
    }

    public void addListener(Animator.AnimatorListener listener) {
        this.animator.addListener(listener);
    }

    public void animate() {
        animator.start();
    }

    private ObjectAnimator createSlideInAnimator(View view, int offset) {

        view.setTranslationY(offset);

        ObjectAnimator slideIn = ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0);
        slideIn.setDuration(SLIDE_IN_DURATION);
        slideIn.setInterpolator(new OvershootInterpolator(0.8f));
        return slideIn;
    }

    private ObjectAnimator createSlideOutAnimator(View view, int offset) {
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset);
        slideOut.setDuration(SLIDE_OUT_DURATION);
        return slideOut;
    }
}
