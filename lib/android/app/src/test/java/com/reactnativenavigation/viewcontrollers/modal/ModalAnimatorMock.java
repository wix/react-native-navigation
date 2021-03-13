package com.reactnativenavigation.viewcontrollers.modal;

import android.content.Context;

import com.reactnativenavigation.options.TransitionAnimationOptions;
import com.reactnativenavigation.utils.ScreenAnimationListener;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;

public class ModalAnimatorMock extends ModalAnimator {

    ModalAnimatorMock(Context context) {
        super(context);
    }

    @Override
    public void show(@NotNull ViewController<?> appearing, @Nullable ViewController<?> disappearing, @NotNull TransitionAnimationOptions show, @NotNull ScreenAnimationListener listener) {
        try {
            listener.onStart();
            Thread.sleep(10);
            listener.onEnd();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dismiss(@Nullable ViewController<?> appearing, @NotNull ViewController<?> disappearing, @NotNull TransitionAnimationOptions dismiss, @NotNull ScreenAnimationListener listener) {
        try {
            listener.onStart();
            Thread.sleep(10);
            listener.onEnd();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
