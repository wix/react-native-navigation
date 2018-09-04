package com.reactnativenavigation.viewcontrollers.modal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.reactnativenavigation.anim.ModalAnimator;
import com.reactnativenavigation.parse.ModalPresentationStyle;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

public class ModalPresenter {

    private ViewGroup modalsContainer;
    private ModalAnimator animator;
    private Options defaultOptions = new Options();

    ModalPresenter(ModalAnimator animator) {
        this.animator = animator;
    }

    public void setModalsContainer(ViewGroup modalsLayout) {
        this.modalsContainer = modalsLayout;
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void showModal(ViewController toAdd, ViewController toRemove, CommandListener listener) {
        if (modalsContainer == null) {
            listener.onError("Can not show modal before activity is created");
            return;
        }
        Options options = toAdd.resolveCurrentOptions(defaultOptions);
        toAdd.setWaitForRender(options.animations.showModal.waitForRender);
        modalsContainer.addView(toAdd.getView());
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

    private void onShowModalEnd(ViewController toAdd, @Nullable ViewController toRemove, CommandListener listener) {
        if (toRemove != null && toAdd.options.modal.presentationStyle != ModalPresentationStyle.OverCurrentContext) {
            toRemove.detachView();
        }
        listener.onSuccess(toAdd.getId());
    }

    public void dismissTopModal(ViewController toDismiss, @NonNull ViewController toAdd, CommandListener listener) {
        if (modalsContainer == null) {
            listener.onError("Can not dismiss modal before activity is created");
            return;
        }
        toAdd.attachView(modalsContainer, 0);
        dismissModal(toDismiss, listener);
    }

    public void dismissModal(ViewController toDismiss, CommandListener listener) {
        if (modalsContainer == null) {
            listener.onError("Can not dismiss modal before activity is created");
            return;
        }
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
