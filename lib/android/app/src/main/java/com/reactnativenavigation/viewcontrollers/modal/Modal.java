package com.reactnativenavigation.viewcontrollers.modal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.R;
import com.reactnativenavigation.viewcontrollers.ViewController;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

public class Modal implements DialogInterface.OnKeyListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener {
    public final ViewController viewController;
    private final Dialog dialog;
    private ModalListener modalListener;
    @Nullable private Promise dismissPromise;

    public Modal(final ViewController viewController, ModalListener modalListener) {
        this.viewController = viewController;
        dialog = new Dialog(viewController.getActivity(), R.style.Modal);
        this.modalListener = modalListener;
        dialog.setOnKeyListener(this);
        dialog.setOnDismissListener(this);
        dialog.setOnShowListener(this);
    }

    public void show() {
        preMeasureView();
        dialog.setContentView(viewController.getView());
        dialog.show();
    }

    public void dismiss(Promise promise) {
        dismissPromise = promise;
        dialog.dismiss();
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
