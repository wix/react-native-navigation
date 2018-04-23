package com.reactnativenavigation.anim;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

import com.reactnativenavigation.parse.AnimationOptions;

public class ModalAnimator2 extends BaseAnimator {

    private Animator animator;

    public ModalAnimator2(Context context) {
        super(context);
    }

    public void show(View view, AnimationOptions animation, AnimatorListenerAdapter listener) {
        animator = animation.getAnimation(view, getDefaultPushAnimation(view));
        animator.addListener(listener);
        animator.start();
    }

    public void dismiss(View view, AnimatorListenerAdapter listener) {
        animator = options.dismissModal.getAnimation(view, getDefaultPopAnimation(view));
        animator.addListener(listener);
        animator.start();
    }

    public boolean isRunning() {
        return animator != null && animator.isRunning();
    }
}
