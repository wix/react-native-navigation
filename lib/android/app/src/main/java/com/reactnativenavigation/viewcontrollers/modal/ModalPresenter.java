package com.reactnativenavigation.viewcontrollers.modal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.anim.ModalAnimator;
import com.reactnativenavigation.parse.ModalPresentationStyle;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentLP;

public class ModalPresenter {

    private ViewGroup rootLayout;
    private CoordinatorLayout modalsLayout;
    private ModalAnimator animator;
    private Options defaultOptions = new Options();

    ModalPresenter(ModalAnimator animator) {
        this.animator = animator;
    }

    void setRootLayout(ViewGroup rootLayout) {
        this.rootLayout = rootLayout;
    }

    void setModalsLayout(CoordinatorLayout modalsLayout) {
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
        modalsLayout.setVisibility(View.VISIBLE);
        modalsLayout.addView(toAdd.getView(), matchParentLP());

        if (options.animations.showModal.enabled.isTrueOrUndefined()) {
            toAdd.getView().setAlpha(0);
            if (options.animations.showModal.waitForRender.isTrue()) {
                toAdd.addOnAppearedListener(() -> animateShow(toAdd, toRemove, listener, options));
            } else {
                animateShow(toAdd, toRemove, listener, options);
            }
        } else {
            if (options.animations.showModal.waitForRender.isTrue()) {
                toAdd.addOnAppearedListener(() -> onShowModalEnd(toAdd, toRemove, listener));
            } else {
                onShowModalEnd(toAdd, toRemove, listener);
            }
        }
    }

    private void animateShow(ViewController toAdd, ViewController toRemove, CommandListener listener, Options options) {
        animator.show(toAdd.getView(), options.animations.showModal, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                toAdd.getView().setAlpha(1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onShowModalEnd(toAdd, toRemove, listener);
            }
        });
    }

    private void onShowModalEnd(ViewController toAdd, @Nullable ViewController toRemove, CommandListener listener) {
        toAdd.onViewDidAppear();
        if (toRemove != null && toAdd.resolveCurrentOptions(defaultOptions).modal.presentationStyle != ModalPresentationStyle.OverCurrentContext) {
            toRemove.detachView();
        }
        listener.onSuccess(toAdd.getId());
    }

    void dismissModal(ViewController toDismiss, @Nullable ViewController toAdd, ViewController root, CommandListener listener) {
        if (modalsLayout == null) {
            listener.onError("Can not dismiss modal before activity is created");
            return;
        }
        if (toAdd != null) {
            toAdd.attachView(toAdd == root ? rootLayout : modalsLayout, 0);
            toAdd.onViewDidAppear();
        }
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
        listener.onSuccess(toDismiss.getId());
        toDismiss.destroy();
        if (isEmpty()) modalsLayout.setVisibility(View.GONE);
    }

    private boolean isEmpty() {
        return modalsLayout.getChildCount() == 0;
    }
}
