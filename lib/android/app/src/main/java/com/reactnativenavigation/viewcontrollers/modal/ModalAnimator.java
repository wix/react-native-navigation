package com.reactnativenavigation.viewcontrollers.modal;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

import com.reactnativenavigation.options.AnimationOptions;
import com.reactnativenavigation.utils.ScreenAnimationListener;
import com.reactnativenavigation.viewcontrollers.common.BaseAnimator;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class ModalAnimator extends BaseAnimator {

    private Map<View, Animator> runningAnimators = new HashMap<>();

    public ModalAnimator(Context context) {
        super(context);
    }

    public void show(View view, AnimationOptions show, ScreenAnimationListener listener) {
        Animator animator = show.getAnimation(view, getDefaultPushAnimation(view));
        animator.addListener(new AnimatorListenerAdapter() {
            private boolean isCancelled = false;

            @Override
            public void onAnimationStart(Animator animation) {
                runningAnimators.put(view, animator);
                listener.onStart();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isCancelled = true;
                listener.onCancel();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                runningAnimators.remove(view);
                if (!isCancelled) listener.onEnd();
            }
        });
        animator.start();
    }

    public void dismiss(View view, AnimationOptions dismiss, ScreenAnimationListener listener) {
        if (runningAnimators.containsKey(view)) {
            requireNonNull(runningAnimators.get(view)).cancel();
            listener.onEnd();
            return;
        }
        Animator animator = dismiss.getAnimation(view, getDefaultPopAnimation(view));
        animator.addListener(new AnimatorListenerAdapter() {
            private boolean isCancelled = false;

            @Override
            public void onAnimationStart(Animator animation) {
                listener.onStart();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isCancelled = true;
                listener.onCancel();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                runningAnimators.remove(view);
                if (!isCancelled) listener.onEnd();
            }
        });
        animator.start();
    }

    public boolean isRunning() {
        return !runningAnimators.isEmpty();
    }
}
