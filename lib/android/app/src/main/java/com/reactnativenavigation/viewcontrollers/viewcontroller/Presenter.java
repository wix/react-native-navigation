package com.reactnativenavigation.viewcontrollers.viewcontroller;

import static com.reactnativenavigation.utils.ColorUtils.isColorLight;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

import com.reactnativenavigation.options.NavigationBarOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.OrientationOptions;
import com.reactnativenavigation.options.StatusBarOptions;
import com.reactnativenavigation.options.layout.LayoutInsets;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.statusbar.StatusBarPresenter;

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
        applyStatusBarOptions(view, withDefaultOptions.statusBar);
        applyNavigationBarOptions(withDefaultOptions.navigationBar);
    }

    public void onViewBroughtToFront(ViewController<?> viewController, Options options) {
        Options withDefaultOptions = options.copy().withDefaultOptions(defaultOptions);
        applyStatusBarOptions(viewController, withDefaultOptions.statusBar);
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

    private void applyStatusBarOptions(ViewController viewController, StatusBarOptions options) {
        StatusBarPresenter.instance.applyOptions(viewController, options);
    }

    private void mergeStatusBarOptions(View view, StatusBarOptions statusBarOptions) {
        StatusBarPresenter.instance.mergeOptions(view, statusBarOptions);
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

    public void onConfigurationChanged(ViewController controller, Options options) {
        Options withDefault = options.withDefaultOptions(defaultOptions);
        setNavigationBarBackgroundColor(withDefault.navigationBar);
        StatusBarPresenter.instance.onConfigurationChanged(withDefault.statusBar);
        applyBackgroundColor(controller, withDefault);
    }
}
