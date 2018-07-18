package com.reactnativenavigation.viewcontrollers.modal;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

import com.reactnativenavigation.anim.ModalAnimator;
import com.reactnativenavigation.parse.AnimationOptions;

public class ModalAnimatorMock extends ModalAnimator {

    ModalAnimatorMock(Context context) {
        super(context);
    }

    @Override
    public void show(View view, AnimationOptions show, AnimatorListenerAdapter listener) {
        try {
            listener.onAnimationStart(null);
            Thread.sleep(10);
            listener.onAnimationEnd(null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dismiss(View view, AnimationOptions dismiss, AnimatorListenerAdapter listener) {
        try {
            listener.onAnimationStart(null);
            Thread.sleep(10);
            listener.onAnimationEnd(null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
