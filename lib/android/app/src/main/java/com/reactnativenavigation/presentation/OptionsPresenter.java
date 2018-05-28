package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;
import com.reactnativenavigation.parse.StatusBarOptions;
import com.reactnativenavigation.parse.StatusBarOptions.TextColorScheme;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.utils.UiUtils;

@SuppressWarnings("FieldCanBeLocal")
public class OptionsPresenter {

    private Activity activity;

    public OptionsPresenter(Activity activity) {
        this.activity = activity;
    }

    public void present(View view, Options options) {
        applyOrientation(options.orientationOptions);
        applyViewOptions(view, options);
        applyStatusBarOptions(view, options.statusBar);
    }

    public void applyRootOptions(View view, Options options) {
        setDrawBehindStatusBar(view, options.statusBar);
    }

    private void applyOrientation(OrientationOptions options) {
        activity.setRequestedOrientation(options.getValue());
    }

    private void applyViewOptions(View view, Options options) {
        if (options.screenBackgroundColor.hasValue()) {
            view.setBackgroundColor(options.screenBackgroundColor.get());
        }
    }

    public void onViewBroughtToFront(View view, Options options) {
        applyStatusBarOptions(view, options.statusBar);
    }

    private void applyStatusBarOptions(View view, StatusBarOptions statusBar) {
        setStatusBarBackgroundColor(statusBar);
        setTextColorScheme(statusBar.textColorScheme);
        setStatusBarVisible(view, statusBar.visible, statusBar.drawBehind);
    }

    private void setStatusBarVisible(View view, Bool visible, Bool drawBehind) {
        if (visible.isFalse()) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else if (drawBehind.isTrue()) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void setStatusBarBackgroundColor(StatusBarOptions statusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(statusBar.backgroundColor.get(Color.BLACK));
        }
    }

    private void setTextColorScheme(TextColorScheme scheme) {
        final View view = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        if (scheme == TextColorScheme.Dark) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        } else {
            clearDarkTextColorScheme(view);
        }
    }

    private static void clearDarkTextColorScheme(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        int flags = view.getSystemUiVisibility();
        flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
    }

    private void setDrawBehindStatusBar(View view, StatusBarOptions statusBar) {
        ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = statusBar.drawBehind.isFalseOrUndefined() ?
                UiUtils.getStatusBarHeight(activity) :
                0;
    }
}
