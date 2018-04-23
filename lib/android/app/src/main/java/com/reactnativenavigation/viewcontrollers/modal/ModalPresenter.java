package com.reactnativenavigation.viewcontrollers.modal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewGroup;

import com.reactnativenavigation.anim.ModalAnimator2;
import com.reactnativenavigation.viewcontrollers.Navigator.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

import javax.annotation.Nullable;

public class ModalPresenter {

    private ViewGroup content;
    private ModalAnimator2 animator;

    public ModalPresenter(ModalAnimator2 animator) {
        this.animator = animator;
    }

    public void setContentLayout(ViewGroup contentLayout) {
        this.content = contentLayout;
    }

    public void showModal(ViewController toAdd, @Nullable ViewController toRemove, CommandListener listener) {
        content.addView(toAdd.getView());
        if (toAdd.options.animations.showModal.enable.isTrueOrUndefined()) {
            animator.show(toAdd.getView(), toAdd.options.animations.showModal, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    onShowModalEnd(toRemove, listener, toAdd);
                }
            });
        } else {
            onShowModalEnd(toRemove, listener, toAdd);
        }
    }

    private void onShowModalEnd(@Nullable ViewController toRemove, CommandListener listener, ViewController toAdd) {
        if (toRemove != null) content.removeView(toRemove.getView());
        listener.onSuccess(toAdd.getId());
    }

    public void dismissModal(ViewController toDismiss, @Nullable ViewController toAdd, CommandListener listener) {
        if (toAdd != null) content.addView(toAdd.getView(), 0);
        if (toDismiss.options.animations.dismissModal.enable.isTrueOrUndefined()) {
            animator.dismiss(toDismiss.getView(), new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    onDismissEnd(toDismiss, listener);
                }
            });
        } else {
            onDismissEnd(toDismiss, listener);
        }
    }

    private void onDismissEnd(ViewController toDismiss, CommandListener listener) {
        toDismiss.destroy();
        listener.onSuccess(toDismiss.getId());
    }
}
