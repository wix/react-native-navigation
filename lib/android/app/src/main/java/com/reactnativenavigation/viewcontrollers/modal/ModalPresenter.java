package com.reactnativenavigation.viewcontrollers.modal;

import android.view.ViewGroup;

import com.reactnativenavigation.viewcontrollers.Navigator.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

import javax.annotation.Nullable;

public class ModalPresenter {

    private ViewGroup content;

    public void setContentLayout(ViewGroup contentLayout) {
        this.content = contentLayout;
    }

    public void showModal(ViewController toAdd, @Nullable ViewController toRemove, CommandListener listener) {
        content.addView(toAdd.getView());
        if (toRemove != null) content.removeView(toRemove.getView());
        listener.onSuccess(toAdd.getId());
    }

    public void dismissModal(ViewController toDismiss, @Nullable ViewController toAdd, CommandListener listener) {
        if (toAdd != null) content.addView(toAdd.getView());
        toDismiss.destroy();
        listener.onSuccess(toDismiss.getId());
    }
}
