package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;

import com.reactnativenavigation.parse.NavigationBarOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;
import com.reactnativenavigation.parse.StatusBarOptions;
import com.reactnativenavigation.parse.StatusBarOptions.TextColorScheme;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.utils.StatusBarUtils;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;

import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

public class Presenter {
    private Activity activity;
    private Options defaultOptions;

    public Presenter(Activity activity, Options defaultOptions) {
        this.activity = activity;
        this.defaultOptions = defaultOptions;
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void mergeOptions(View view, Options options) {
        mergeStatusBarOptions(view, options.statusBar);
        mergeNavigationBarOptions(options.navigationBar);
    }

    public void applyOptions(ViewController view, Options options) {
        Options withDefaultOptions = options.copy().withDefaultOptions(defaultOptions);
        applyOrientation(withDefaultOptions.layout.orientation);
        applyViewOptions(view, withDefaultOptions);
        applyStatusBarOptions(withDefaultOptions);
        applyNavigationBarOptions(withDefaultOptions.navigationBar);
    }

    public void onViewBroughtToFront(Options options) {
        Options withDefaultOptions = options.copy().withDefaultOptions(defaultOptions);
        applyStatusBarOptions(withDefaultOptions);
    }

    private void applyOrientation(OrientationOptions options) {
        activity.setRequestedOrientation(options.getValue());
    }

    private void applyViewOptions(ViewController view, Options options) {
        applyBackgroundColor(view, options);
        applyTopMargin(view.getView(), options);
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
            int top = view.resolveCurrentOptions().statusBar.drawBehind.isTrue() ? 0 : StatusBarUtils.getStatusBarHeight(view.getActivity());
            if (!(view instanceof ParentController)) {
                MarginLayoutParams lp = (MarginLayoutParams) view.getView().getLayoutParams();
                if (lp.topMargin != 0) top = 0;
            }
            ld.setLayerInset(0, 0, top, 0, 0);
            view.getView().setBackground(ld);
        }
    }

    private void applyStatusBarOptions(Options options) {
        StatusBarOptions statusBar = options.copy().withDefaultOptions(defaultOptions).statusBar;
        setStatusBarBackgroundColor(statusBar);
        setTextColorScheme(statusBar.textColorScheme);
        setTranslucent(statusBar);
        setStatusBarVisible(statusBar.visible);
    }

    private void setTranslucent(StatusBarOptions options) {
        Window window = activity.getWindow();
        if (options.translucent.isTrue()) {
            window.setFlags(FLAG_TRANSLUCENT_STATUS, FLAG_TRANSLUCENT_STATUS);
        } else if (StatusBarUtils.isTranslucent(window)) {
            window.clearFlags(FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void setStatusBarVisible(Bool visible) {
        View decorView = activity.getWindow().getDecorView();
        int flags = decorView.getSystemUiVisibility();
        if (visible.isFalse()) {
            flags |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        } else {
            flags &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        decorView.setSystemUiVisibility(flags);
    }

    private void setStatusBarBackgroundColor(StatusBarOptions statusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && statusBar.backgroundColor.canApplyValue()) {
            int defaultColor = statusBar.visible.isTrueOrUndefined() ? Color.BLACK : Color.TRANSPARENT;
            activity.getWindow().setStatusBarColor(statusBar.backgroundColor.get(defaultColor));
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

    private void clearDarkTextColorScheme(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        int flags = view.getSystemUiVisibility();
        flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
    }

    private void mergeStatusBarOptions(View view, StatusBarOptions statusBar) {
        mergeStatusBarBackgroundColor(statusBar);
        mergeTextColorScheme(statusBar.textColorScheme);
        mergeTranslucent(statusBar);
        mergeStatusBarVisible(view, statusBar.visible, statusBar.drawBehind);
    }

    private void mergeStatusBarBackgroundColor(StatusBarOptions statusBar) {
        if (statusBar.backgroundColor.hasValue() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(statusBar.backgroundColor.get(Color.BLACK));
        }
    }

    private void mergeTextColorScheme(TextColorScheme scheme) {
        if (!scheme.hasValue() || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        final View view = activity.getWindow().getDecorView();
        if (scheme == TextColorScheme.Dark) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        } else {
            clearDarkTextColorScheme(view);
        }
    }

    private void mergeTranslucent(StatusBarOptions options) {
        Window window = activity.getWindow();
        if (options.translucent.isTrue()) {
            window.setFlags(FLAG_TRANSLUCENT_STATUS, FLAG_TRANSLUCENT_STATUS);
        } else if (options.translucent.isFalse() && StatusBarUtils.isTranslucent(window)) {
            window.clearFlags(FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void mergeStatusBarVisible(View view, Bool visible, Bool drawBehind) {
        if (visible.hasValue()) {
            int flags = view.getSystemUiVisibility();
            if (visible.isTrue()) {
                flags &= ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN & ~View.SYSTEM_UI_FLAG_FULLSCREEN;
            } else {
                flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;
            }
            if (flags != view.getSystemUiVisibility()) view.requestLayout();
            view.setSystemUiVisibility(flags);
        } else if (drawBehind.hasValue()) {
            if (drawBehind.isTrue()) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                view.setSystemUiVisibility(~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
        int flags = decorView.getSystemUiVisibility();
        boolean defaultVisibility = (flags & View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) == 0;
        int hideNavigationBarFlags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (options.isVisible.get(defaultVisibility)) {
            flags &= ~hideNavigationBarFlags;
        } else {
            flags |= hideNavigationBarFlags;
        }
        decorView.setSystemUiVisibility(flags);
    }

    private void setNavigationBarBackgroundColor(NavigationBarOptions navigationBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && navigationBar.backgroundColor.canApplyValue()) {
            int defaultColor = activity.getWindow().getNavigationBarColor();
            int color = navigationBar.backgroundColor.get(defaultColor);
            activity.getWindow().setNavigationBarColor(color);
            setNavigationBarButtonsColor(color);
        }
    }

    private void setNavigationBarButtonsColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = activity.getWindow().getDecorView();
            int flags = decorView.getSystemUiVisibility();
            if (isColorLight(color)) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            decorView.setSystemUiVisibility(flags);
        }
    }

    private boolean isColorLight(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }
}
