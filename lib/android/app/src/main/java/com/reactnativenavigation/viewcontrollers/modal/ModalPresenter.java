package com.reactnativenavigation.viewcontrollers.modal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.reactnativenavigation.anim.ModalAnimator;
import com.reactnativenavigation.parse.ModalPresentationStyle;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

public class ModalPresenter {

    private ViewGroup rootLayout;
    private ViewGroup modalsLayout;
    private ModalAnimator animator;
    private Options defaultOptions = new Options();

    ModalPresenter(ModalAnimator animator) {
        this.animator = animator;
    }

    public void setRootLayout(ViewGroup rootLayout) {
        this.rootLayout = rootLayout;
    }

    void setModalsLayout(ViewGroup modalsLayout) {
        this.modalsLayout = modalsLayout;
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    void showModal(ViewController toAdd, ViewController toRemove, CommandListener listener) {
        if (modalsLayout == null) {
            listener.onError("Can not show modal before activity is created");
            return;
        }
        Options options = toAdd.resolveCurrentOptions(defaultOptions);
        toAdd.setWaitForRender(options.animations.showModal.waitForRender);
        modalsLayout.addView(toAdd.getView());
        if (options.animations.showModal.enabled.isTrueOrUndefined()) {
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

    private void onShowModalEnd(ViewController toAdd, @Nullable ViewController toRemove, CommandListener listener) {
        if (toRemove != null && toAdd.options.modal.presentationStyle != ModalPresentationStyle.OverCurrentContext) {
            toRemove.detachView();
        }
        listener.onSuccess(toAdd.getId());
    }

    void dismissModal(ViewController toDismiss, @Nullable ViewController toAdd, ViewController root, CommandListener listener) {
        if (modalsLayout == null) {
            listener.onError("Can not dismiss modal before activity is created");
            return;
        }
        if (toAdd != null) toAdd.attachView(toAdd == root ? rootLayout : modalsLayout, 0);
        Options options = toDismiss.resolveCurrentOptions(defaultOptions);
        if (options.animations.dismissModal.enabled.isTrueOrUndefined()) {
            animator.dismiss(toDismiss.getView(), options.animations.dismissModal, new AnimatorListenerAdapter() {
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
