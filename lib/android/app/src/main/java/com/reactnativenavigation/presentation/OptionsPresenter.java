package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;
import com.reactnativenavigation.parse.StatusBarOptions;

@SuppressWarnings("FieldCanBeLocal")
public class OptionsPresenter {

    private Activity activity;

    public OptionsPresenter(Activity activity) {
        this.activity = activity;
    }

    public void present(View view, Options options) {
        applyOrientation(options.orientationOptions);
        applyViewOptions(view, options);
        applyStatusBarOptions(options.statusBar);
    }

    private void applyOrientation(OrientationOptions options) {
        activity.setRequestedOrientation(options.getValue());
    }

    private void applyViewOptions(View view, Options options) {
        if (options.screenBackgroundColor.hasValue()) {
            view.setBackgroundColor(options.screenBackgroundColor.get());
        }
    }

    public void onViewBroughtToFront(Options options) {
        applyStatusBarOptions(options.statusBar);
    }

    private void applyStatusBarOptions(StatusBarOptions statusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(statusBar.backgroundColor.get(Color.BLACK));
        }
    }
}
