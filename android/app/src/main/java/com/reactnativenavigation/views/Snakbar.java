package com.reactnativenavigation.views;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.SnackbarParams;

class Snakbar {
    private final OnDismissListener parent;
    private final String navigatorEventId;
    private final SnackbarParams params;
    private Snackbar snackbar;

    interface OnDismissListener {
        void onDismiss(Snakbar snakbar);
    }

    public void show() {
        snackbar.show();
    }

    void dismiss() {
        snackbar.dismiss();
    }

    public View getView() {
        return snackbar.getView();
    }

    Snakbar(OnDismissListener parent, String navigatorEventId, SnackbarParams params) {
        this.parent = parent;
        this.navigatorEventId = navigatorEventId;
        this.params = params;
        create();
    }

    private void create() {
        snackbar = Snackbar.make((View) parent, params.text, params.duration);
        setAction(navigatorEventId, params, snackbar);
        setStyle(snackbar, params);
        setOnDismissListener();
    }

    private void setAction(final String navigatorEventId, final SnackbarParams params, Snackbar snackbar) {
        if (params.eventId != null) {
            snackbar.setAction(params.buttonText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationApplication.instance.getEventEmitter().sendNavigatorEvent(params.eventId, navigatorEventId);
                }
            });
        }
    }

    private void setStyle(Snackbar snackbar, SnackbarParams params) {
        if (params.buttonColor.hasColor()) {
            snackbar.setActionTextColor(params.buttonColor.getColor());
        }
    }

    private void setOnDismissListener() {
        snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                parent.onDismiss(Snakbar.this);
            }
        });
    }
}
