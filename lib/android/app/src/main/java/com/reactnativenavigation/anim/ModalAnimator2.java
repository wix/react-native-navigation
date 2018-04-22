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

    public void show(View contentView, AnimationOptions animation, AnimatorListenerAdapter listener) {
        animator = animation.getAnimation(contentView, getDefaultPushAnimation(contentView));
        animator.addListener(listener);
        animator.start();
    }

//    public void dismiss(View contentView, AnimatorListenerAdapter listener) {
//        AnimatorSet animatorSet;
//        if (options.dismissModal.hasValue()) {
//            animatorSet = options.dismissModal.getAnimation(contentView);
//        } else {
//            animatorSet = getDefaultPopAnimation(contentView);
//        }
//        animatorSet.addListener(listener);
//        animatorSet.start();
//    }

    public boolean isRunning() {
        return animator.isRunning();
    }
}
