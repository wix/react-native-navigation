package com.reactnativenavigation.viewcontrollers.modal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;

import com.reactnativenavigation.R;
import com.reactnativenavigation.viewcontrollers.ViewController;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

public class Modal implements DialogInterface.OnKeyListener {
    public final ViewController viewController;
    private final Dialog dialog;

    public Modal(final ViewController viewController) {
        this.viewController = viewController;
        dialog = new Dialog(viewController.getActivity(), R.style.Modal);
        dialog.setOnKeyListener(this);
    }

    public void show() {
        preMeasureView();
        dialog.setContentView(viewController.getView());
        dialog.show();
    }

    public void dismiss() {
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
                if (viewController.handleBack()) {
                    return true;
                }
                dialog.dismiss();
            }
        }
        return false;
    }
}
