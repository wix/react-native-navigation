package com.reactnativenavigation.viewcontrollers.modal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.R;
import com.reactnativenavigation.anim.ModalAnimator;
import com.reactnativenavigation.viewcontrollers.ViewController;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

public class Modal implements DialogInterface.OnKeyListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener {
    public final ViewController viewController;
    private final Dialog dialog;
    private ModalListener modalListener;
    @Nullable private Promise dismissPromise;

    private ModalAnimator animator;

    public Modal(final ViewController viewController, ModalListener modalListener) {
        this.viewController = viewController;
        dialog = new Dialog(viewController.getActivity(), R.style.Modal);
        this.modalListener = modalListener;
        dialog.setOnKeyListener(this);
        dialog.setOnDismissListener(this);
        dialog.setOnShowListener(this);
        animator = new ModalAnimator(viewController.getActivity(), viewController.options.animationsOptions);
    }

    public void show() {
        preMeasureView();
        final View contentView = viewController.getView();
        dialog.show();
        animator.animateShow(contentView, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                dialog.setContentView(contentView);
            }
        });
    }

    public void dismiss(Promise promise) {
        dismissPromise = promise;
        animator.animateDismiss(viewController.getView(), new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dialog.dismiss();
            }
        });
    }

    public boolean containsDeepComponentId(String componentId) {
        return viewController.findControllerById(componentId) != null;
    }

    private void preMeasureView() {
        View decorView = viewController.getActivity().getWindow().getDecorView();
        viewController.getView().measure(makeMeasureSpec(decorView.getMeasuredWidth(), EXACTLY), makeMeasureSpec(decorView.getMeasuredHeight(), EXACTLY));
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                viewController.getActivity().onBackPressed();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        modalListener.onModalDismiss(this);
        if (dismissPromise != null) {
            dismissPromise.resolve(viewController.getId());
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        modalListener.onModalDisplay(this);
    }

    public boolean handleBack() {
        if (!viewController.handleBack()) {
            dialog.dismiss();
        }
        return true;
    }
}
