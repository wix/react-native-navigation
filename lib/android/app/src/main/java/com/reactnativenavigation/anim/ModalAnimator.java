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

    public void show(View view, View otherView, AnimationOptions show, AnimatorListenerAdapter listener) {
        animator = show.getAnimation(view, otherView, getDefaultPushAnimation(view), false);
        animator.addListener(listener);
        animator.start();
    }

    public void dismiss(View view, View otherView, AnimationOptions dismiss, AnimatorListenerAdapter listener) {
        animator = dismiss.getAnimation(view, otherView, getDefaultPopAnimation(view), true);
        animator.addListener(listener);
        animator.start();
    }

    public boolean isRunning() {
        return animator != null && animator.isRunning();
    }
}
