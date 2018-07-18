package com.reactnativenavigation.anim;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

import com.reactnativenavigation.parse.AnimationOptions;

public class ModalAnimator extends BaseAnimator {

    private Animator animator;

    public ModalAnimator(Context context) {
        super(context);
    }

    public void show(View view, AnimationOptions show, AnimatorListenerAdapter listener) {
        animator = show.getAnimation(view, getDefaultPushAnimation(view));
        animator.addListener(listener);
        animator.start();
    }

    public void dismiss(View view, AnimationOptions dismiss, AnimatorListenerAdapter listener) {
        animator = dismiss.getAnimation(view, getDefaultPopAnimation(view));
        animator.addListener(listener);
        animator.start();
    }

    public boolean isRunning() {
        return animator != null && animator.isRunning();
    }
}
