package com.reactnativenavigation.anim;


import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;

import com.reactnativenavigation.parse.AnimationsOptions;

public class ModalAnimator extends BaseAnimator {

    public ModalAnimator(Context context, AnimationsOptions options) {
        super(context);
        this.options = options;
    }

    public void animateShow(View contentView, AnimatorListenerAdapter listener) {
        AnimatorSet animatorSet;
        if (options.showModal.hasValue()) {
            animatorSet = options.showModal.getAnimation(contentView);
        } else {
            animatorSet = getDefaultPushAnimation(contentView);
        }
        animatorSet.addListener(listener);
        animatorSet.start();
    }

    public void animateDismiss(View contentView, AnimatorListenerAdapter listener) {
        AnimatorSet animatorSet;
        if (options.dismissModal.hasValue()) {
            animatorSet = options.dismissModal.getAnimation(contentView);
        } else {
            animatorSet = getDefaultPopAnimation(contentView);
        }
        animatorSet.addListener(listener);
        animatorSet.start();
    }
}
