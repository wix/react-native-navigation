package com.reactnativenavigation.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import static android.view.View.TRANSLATION_Y;

public class PeekingAnimator {

    private static final int SLIDE_OUT_DURATION = 300;
    private static final int SLIDE_IN_DURATION = 600;
    private static final int SUSTAIN_DURATION = 3000;

    private final Animator animator;

    public PeekingAnimator(View view) {
        this.animator = createAnimator(view);
    }

    public void addListener(Animator.AnimatorListener listener) {
        this.animator.addListener(listener);
    }

    public void animate() {
        animator.start();
    }

    private Animator createAnimator(View view) {
        final int heightPixels = view.getLayoutParams().height;

        view.setTranslationY(-heightPixels);

        ObjectAnimator slideIn = createSlideInAnimator(view);
        ObjectAnimator slideOut = createSlideOutAnimator(view, heightPixels, slideIn);
        AnimatorSet animatorSet = createAnimatorSet(slideIn, slideOut);
        return animatorSet;
    }

    private ObjectAnimator createSlideInAnimator(View view) {
        ObjectAnimator slideIn = ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0);
        slideIn.setDuration(SLIDE_IN_DURATION);
        slideIn.setInterpolator(new OvershootInterpolator(0.8f));
        return slideIn;
    }

    private ObjectAnimator createSlideOutAnimator(View view, int heightPixels, ObjectAnimator slideIn) {
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(view, TRANSLATION_Y, -heightPixels);
        slideIn.setDuration(SLIDE_OUT_DURATION);
        return slideOut;
    }

    private AnimatorSet createAnimatorSet(ObjectAnimator slideIn, ObjectAnimator slideOut) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(slideOut).after(SUSTAIN_DURATION).after(slideIn);
        return animatorSet;
    }
}
