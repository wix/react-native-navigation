package com.reactnativenavigation.viewcontrollers.modal;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

import com.reactnativenavigation.anim.ModalAnimator2;
import com.reactnativenavigation.parse.AnimationOptions;

public class ModalAnimatorMock extends ModalAnimator2 {

    ModalAnimatorMock(Context context) {
        super(context);
    }

    @Override
    public void show(View view, AnimationOptions animation, AnimatorListenerAdapter listener) {
        try {
            listener.onAnimationStart(null);
            Thread.sleep(10);
            listener.onAnimationEnd(null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dismiss(View view, AnimatorListenerAdapter listener) {
        try {
            listener.onAnimationStart(null);
            Thread.sleep(10);
            listener.onAnimationEnd(null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
