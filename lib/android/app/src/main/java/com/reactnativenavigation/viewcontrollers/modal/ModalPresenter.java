package com.reactnativenavigation.viewcontrollers.modal;

import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.options.AnimationOptions;
import com.reactnativenavigation.options.ModalPresentationStyle;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.utils.ScreenAnimationListener;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import org.jetbrains.annotations.NotNull;

import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentLP;

public class ModalPresenter {

    private ViewGroup rootLayout;
    private CoordinatorLayout modalsLayout;
    private final ModalAnimator modalAnimator;
    private Options defaultOptions = new Options();

    ModalPresenter(ModalAnimator animator) {
        this.modalAnimator = animator;
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

    void showModal(ViewController<?> appearing, ViewController<?> disappearing, CommandListener listener) {
        if (modalsLayout == null) {
            listener.onError("Can not show modal before activity is created");
            return;
        }

        Options options = appearing.resolveCurrentOptions(defaultOptions);

        AnimationOptions enterAnimationOptions = options.animations.showModal.getEnter();
        appearing.setWaitForRender(enterAnimationOptions.waitForRender);
        modalsLayout.setVisibility(View.VISIBLE);
        modalsLayout.addView(appearing.getView(), matchParentLP());

        if (enterAnimationOptions.enabled.isTrueOrUndefined()) {
            if (enterAnimationOptions.shouldWaitForRender().isTrue()) {
                appearing.addOnAppearedListener(() -> modalAnimator.show(appearing, disappearing, options.animations.showModal, createListener(appearing, disappearing, listener)));
            } else {
                modalAnimator.show(appearing, disappearing, options.animations.showModal, createListener(appearing, disappearing, listener));
            }
        } else {
            if (enterAnimationOptions.waitForRender.isTrue()) {
                appearing.addOnAppearedListener(() -> onShowModalEnd(appearing, disappearing, listener));
            } else {
                onShowModalEnd(appearing, disappearing, listener);
            }
        }
    }


    @NotNull
    private ScreenAnimationListener createListener(ViewController<?> toAdd, ViewController<?> toRemove, CommandListener listener) {
        return new ScreenAnimationListener() {
            @Override
            public void onStart() {
                toAdd.getView().setAlpha(1);
            }

            @Override
            public void onEnd() {
                onShowModalEnd(toAdd, toRemove, listener);
            }

            @Override
            public void onCancel() {
                listener.onSuccess(toAdd.getId());
            }
        };
    }

    private void onShowModalEnd(ViewController<?> toAdd, @Nullable ViewController<?> toRemove, CommandListener listener) {
        toAdd.onViewDidAppear();
        if (toRemove != null && toAdd.resolveCurrentOptions(defaultOptions).modal.presentationStyle != ModalPresentationStyle.OverCurrentContext) {
            toRemove.detachView();
        }
        listener.onSuccess(toAdd.getId());
    }

    void dismissModal(ViewController<?> toDismiss, @Nullable ViewController<?> toAdd, ViewController<?> root, CommandListener listener) {
        if (modalsLayout == null) {
            listener.onError("Can not dismiss modal before activity is created");
            return;
        }
        if (toAdd != null) {
            toAdd.attachView(toAdd == root ? rootLayout : modalsLayout, 0);
            toAdd.onViewDidAppear();
        }
        Options options = toDismiss.resolveCurrentOptions(defaultOptions);
        if (options.animations.dismissModal.getExit().enabled.isTrueOrUndefined()) {
            modalAnimator.dismiss(toAdd, toDismiss, options.animations.dismissModal, new ScreenAnimationListener() {
                @Override
                public void onEnd() {
                    onDismissEnd(toDismiss, listener);
                }
            });
        } else {
            onDismissEnd(toDismiss, listener);
        }
    }

    boolean shouldDismissModal(ViewController<?> toDismiss) {
        return toDismiss.resolveCurrentOptions(defaultOptions).hardwareBack.dismissModalOnPress.get(true);
    }

    public Options resolveOptions(ViewController<?> modalController){
        return modalController.resolveCurrentOptions(defaultOptions);
    }
    private void onDismissEnd(ViewController<?> toDismiss, CommandListener listener) {
        listener.onSuccess(toDismiss.getId());
        toDismiss.destroy();
        if (isEmpty()) modalsLayout.setVisibility(View.GONE);
    }

    private boolean isEmpty() {
        return modalsLayout.getChildCount() == 0;
    }
}
