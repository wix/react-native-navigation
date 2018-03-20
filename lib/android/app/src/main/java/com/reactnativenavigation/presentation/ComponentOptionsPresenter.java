package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.view.View;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;

public class ComponentOptionsPresenter {
    private View component;

    public ComponentOptionsPresenter(View component) {
        this.component = component;
    }

    public void present(Options options) {
        applyOrientation(options.orientationOptions);
    }

    private void applyOrientation(OrientationOptions options) {
        ((Activity) component.getContext()).setRequestedOrientation(options.getValue());
    }
}
