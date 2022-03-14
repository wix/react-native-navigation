package com.reactnativenavigation.viewcontrollers.viewcontroller;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;

import com.reactnativenavigation.options.NavigationBarOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.OrientationOptions;
import com.reactnativenavigation.options.StatusBarOptions;
import com.reactnativenavigation.options.StatusBarOptions.TextColorScheme;
import com.reactnativenavigation.options.layout.LayoutInsets;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;

public class Presenter {
    private final Activity activity;
    private Options defaultOptions;

    public Presenter(Activity activity, Options defaultOptions) {
        this.activity = activity;
        this.defaultOptions = defaultOptions;

    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public Options getDefaultOptions() {
        return defaultOptions;
    }

    public void mergeOptions(ViewController<?> viewController, Options options) {
        final Options withDefaults = viewController.resolveCurrentOptions().copy().mergeWith(options).withDefaultOptions(defaultOptions);
        mergeStatusBarOptions(viewController.getView(), withDefaults.statusBar);
        mergeNavigationBarOptions(withDefaults.navigationBar);
        applyLayoutInsetsOnMostTopParent(viewController,withDefaults.layout.getInsets());
    }

    private void applyLayoutInsetsOnMostTopParent(ViewController<?> viewController, LayoutInsets layoutInsets) {
        final ViewController<?> topMostParent = viewController.getTopMostParent();
        applyLayoutInsets(topMostParent.getView(), layoutInsets);
    }

    public void applyOptions(ViewController view, Options options) {
        Options withDefaultOptions = options.copy().withDefaultOptions(defaultOptions);
        applyOrientation(withDefaultOptions.layout.orientation);
        applyViewOptions(view, withDefaultOptions);
        applyStatusBarOptions(view, withDefaultOptions);
        applyNavigationBarOptions(withDefaultOptions.navigationBar);
    }

    public void onViewBroughtToFront(ViewController<?> viewController, Options options) {
        Options withDefaultOptions = options.copy().withDefaultOptions(defaultOptions);
        applyStatusBarOptions(viewController, withDefaultOptions);
    }

    private void applyOrientation(OrientationOptions options) {
        activity.setRequestedOrientation(options.getValue());
    }

    private void applyViewOptions(ViewController view, Options options) {
        applyBackgroundColor(view, options);
        applyTopMargin(view.getView(), options);
        applyLayoutInsetsOnMostTopParent(view, options.layout.getInsets());
    }

    private void applyLayoutInsets(ViewGroup view, LayoutInsets layoutInsets) {
        if ( view!=null && layoutInsets.hasValue()) {
            view.setPadding(layoutInsets.getLeft() == null ? view.getPaddingLeft() : layoutInsets.getLeft(),
                    layoutInsets.getTop() == null ? view.getPaddingTop() : layoutInsets.getTop(),
                    layoutInsets.getRight() == null ?view.getPaddingRight() : layoutInsets.getRight(),
                    layoutInsets.getBottom() == null ? view.getPaddingBottom() : layoutInsets.getBottom());
        }
    }

    private void applyTopMargin(View view, Options options) {
        if (view.getLayoutParams() instanceof MarginLayoutParams && options.layout.topMargin.hasValue()) {
            ((MarginLayoutParams) view.getLayoutParams()).topMargin = options.layout.topMargin.get(0);
        }
    }

    private void applyBackgroundColor(ViewController view, Options options) {
        if (options.layout.backgroundColor.hasValue()) {
            if (view instanceof Navigator) return;

            LayerDrawable ld = new LayerDrawable(new Drawable[]{new ColorDrawable(options.layout.backgroundColor.get())});
            int top = view.resolveCurrentOptions().statusBar.drawBehind.isTrue() ? 0 : SystemUiUtils.getStatusBarHeight(view.getActivity());
            if (!(view instanceof ParentController)) {
                MarginLayoutParams lp = (MarginLayoutParams) view.getView().getLayoutParams();
                if (lp != null && lp.topMargin != 0) top = 0;
            }
            ld.setLayerInset(0, 0, top, 0, 0);
            view.getView().setBackground(ld);
        }
    }

    private void applyStatusBarOptions(ViewController viewController, Options options) {
        StatusBarOptions statusBar = options.copy().withDefaultOptions(defaultOptions).statusBar;
        setStatusBarBackgroundColor(statusBar);
        setTextColorScheme(statusBar);
        setTranslucent(statusBar);
        setStatusBarVisible(viewController, statusBar.visible);
    }

    private void setTranslucent(StatusBarOptions options) {
        Window window = activity.getWindow();
        if (options.translucent.isTrue()) {
            SystemUiUtils.setStatusBarTranslucent(window);
        } else if (SystemUiUtils.isTranslucent(window)) {
            SystemUiUtils.clearStatusBarTranslucency(window);
        }
    }

    private void setStatusBarVisible(ViewController viewController, Bool visible) {
        final View view = viewController.view != null ? viewController.view : activity.getWindow().getDecorView();
        if (visible.isFalse()) {
            SystemUiUtils.hideStatusBar(activity.getWindow(), view);
        } else {
            SystemUiUtils.showStatusBar(activity.getWindow(), view);
        }
    }

    private void setStatusBarBackgroundColor(StatusBarOptions statusBar) {
        if (statusBar.backgroundColor.canApplyValue()) {
            final int statusBarBackgroundColor = getStatusBarBackgroundColor(statusBar);
            SystemUiUtils.setStatusBarColor(activity.getWindow(), statusBarBackgroundColor,
                    statusBar.translucent.isTrue());
        }
    }

    private boolean isDarkTextColorScheme(StatusBarOptions statusBar) {
        if (statusBar.textColorScheme == TextColorScheme.Dark) {
            return true;
        } else if (statusBar.textColorScheme == TextColorScheme.Light) {
            return false;
        }

        return isColorLight(getStatusBarBackgroundColor(statusBar));
    }

    private int getStatusBarBackgroundColor(StatusBarOptions statusBar) {
        int defaultColor = statusBar.visible.isTrueOrUndefined() ? Color.BLACK : Color.TRANSPARENT;
        return statusBar.backgroundColor.get(defaultColor);
    }

    private void setTextColorScheme(StatusBarOptions statusBar) {
        final View view = activity.getWindow().getDecorView();
        //View.post is a Workaround, added to solve internal Samsung 
        //Android 9 issues. For more info see https://github.com/wix/react-native-navigation/pull/7231
        view.post(() -> {
            SystemUiUtils.setStatusBarColorScheme(activity.getWindow(), view, isDarkTextColorScheme(statusBar));
        });
    }

    private void mergeStatusBarOptions(View view, StatusBarOptions statusBar) {
        mergeStatusBarBackgroundColor(statusBar);
        mergeTextColorScheme(statusBar);
        mergeTranslucent(statusBar);
        mergeStatusBarVisible(view, statusBar.visible);
    }

    private void mergeStatusBarBackgroundColor(StatusBarOptions statusBar) {
        if (statusBar.backgroundColor.hasValue()) {
            final int statusBarBackgroundColor = getStatusBarBackgroundColor(statusBar);
            SystemUiUtils.setStatusBarColor(activity.getWindow(), statusBarBackgroundColor,
                    statusBar.translucent.isTrue());
        }
    }

    private void mergeTextColorScheme(StatusBarOptions statusBar) {
        if (!statusBar.textColorScheme.hasValue()) return;
        setTextColorScheme(statusBar);
    }

    private void mergeTranslucent(StatusBarOptions options) {
        Window window = activity.getWindow();
        if (options.translucent.isTrue()) {
            SystemUiUtils.setStatusBarTranslucent(window);
        } else if (options.translucent.isFalse() && SystemUiUtils.isTranslucent(window)) {
            SystemUiUtils.clearStatusBarTranslucency(window);
        }
    }

    private void mergeStatusBarVisible(View view, Bool visible) {
        if (visible.hasValue()) {
            if (visible.isTrue()) {
                SystemUiUtils.showStatusBar(activity.getWindow(), view);
            } else {
                SystemUiUtils.hideStatusBar(activity.getWindow(), view);
            }
        }
    }

    private void applyNavigationBarOptions(NavigationBarOptions options) {
        applyNavigationBarVisibility(options);
        setNavigationBarBackgroundColor(options);
    }

    private void mergeNavigationBarOptions(NavigationBarOptions options) {
        mergeNavigationBarVisibility(options);
        setNavigationBarBackgroundColor(options);
    }

    private void mergeNavigationBarVisibility(NavigationBarOptions options) {
        if (options.isVisible.hasValue()) applyNavigationBarOptions(options);
    }

    private void applyNavigationBarVisibility(NavigationBarOptions options) {
        View decorView = activity.getWindow().getDecorView();
        if (options.isVisible.isTrueOrUndefined()) {
            SystemUiUtils.showNavigationBar(activity.getWindow(), decorView);
        } else {
            SystemUiUtils.hideNavigationBar(activity.getWindow(), decorView);
        }
    }

    private void setNavigationBarBackgroundColor(NavigationBarOptions navigationBar) {
        int navigationBarDefaultColor = SystemUiUtils.INSTANCE.getNavigationBarDefaultColor();
        navigationBarDefaultColor = navigationBarDefaultColor == -1 ? Color.BLACK : navigationBarDefaultColor;
        if (navigationBar.backgroundColor.canApplyValue()) {
            int color = navigationBar.backgroundColor.get(navigationBarDefaultColor);
            SystemUiUtils.setNavigationBarBackgroundColor(activity.getWindow(), color, isColorLight(color));
        } else {
            SystemUiUtils.setNavigationBarBackgroundColor(activity.getWindow(), navigationBarDefaultColor, isColorLight(navigationBarDefaultColor));

        }
    }

    private boolean isColorLight(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }

    public void onConfigurationChanged(ViewController controller, Options options) {
        Options withDefault = options.withDefaultOptions(defaultOptions);
        setNavigationBarBackgroundColor(withDefault.navigationBar);
        setStatusBarBackgroundColor(withDefault.statusBar);
        setTextColorScheme(withDefault.statusBar);
        applyBackgroundColor(controller, withDefault);
    }
}
