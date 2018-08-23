package com.reactnativenavigation.viewcontrollers.modal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.reactnativenavigation.anim.ModalAnimator;
import com.reactnativenavigation.parse.ModalPresentationStyle;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.react.EventEmitter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

public class ModalPresenter {

    private ViewGroup content;
    private ModalAnimator animator;
    private Options defaultOptions = new Options();
    private EventEmitter eventEmitter;

    ModalPresenter(ModalAnimator animator) {
        this.animator = animator;
    }

    public void setContentLayout(ViewGroup contentLayout) {
        this.content = contentLayout;
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void showModal(ViewController toAdd, ViewController toRemove, CommandListener listener) {
        Options options = toAdd.resolveCurrentOptions(defaultOptions);
        toAdd.setWaitForRender(options.animations.showModal.waitForRender);
        content.addView(toAdd.getView());
        if (options.animations.showModal.enable.isTrueOrUndefined()) {
            if (options.animations.showModal.waitForRender.isTrue()) {
                toAdd.setOnAppearedListener(() -> animateShow(toAdd, toRemove, listener, options));
            } else {
                animateShow(toAdd, toRemove, listener, options);
            }
        } else {
            if (options.animations.showModal.waitForRender.isTrue()) {
                toAdd.setOnAppearedListener(() -> onShowModalEnd(toAdd, toRemove, listener));
            } else {
                onShowModalEnd(toAdd, toRemove, listener);
            }
        }
    }

    private void animateShow(ViewController toAdd, ViewController toRemove, CommandListener listener, Options options) {
        animator.show(toAdd.getView(), options.animations.showModal, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onShowModalEnd(toAdd, toRemove, listener);
            }
        });
    }

    private void onShowModalEnd(ViewController toAdd, ViewController toRemove, CommandListener listener) {
        if (toAdd.options.modal.presentationStyle != ModalPresentationStyle.OverCurrentContext) {
            toRemove.detachView();
        }
        listener.onSuccess(toAdd.getId());
    }

    public void dismissTopModal(ViewController toDismiss, @NonNull ViewController toAdd, CommandListener listener) {
        toAdd.attachView(content, 0);
        dismissModal(toDismiss, listener);
    }

    public void dismissModal(ViewController toDismiss, CommandListener listener) {
        if (toDismiss.options.animations.dismissModal.enable.isTrueOrUndefined()) {
            animator.dismiss(toDismiss.getView(), toDismiss.options.animations.dismissModal, new AnimatorListenerAdapter() {
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
