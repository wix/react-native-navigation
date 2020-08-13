package com.reactnativenavigation.viewcontrollers.modal;

import android.content.Context;
import android.view.View;

import com.reactnativenavigation.options.AnimationOptions;
import com.reactnativenavigation.utils.ScreenAnimationListener;

public class ModalAnimatorMock extends ModalAnimator {

    ModalAnimatorMock(Context context) {
        super(context);
    }

    @Override
    public void show(View view, AnimationOptions show, ScreenAnimationListener listener) {
        try {
            listener.onStart();
            Thread.sleep(10);
            listener.onEnd();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dismiss(View view, AnimationOptions dismiss, ScreenAnimationListener listener) {
        try {
            listener.onStart();
            Thread.sleep(10);
            listener.onEnd();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
