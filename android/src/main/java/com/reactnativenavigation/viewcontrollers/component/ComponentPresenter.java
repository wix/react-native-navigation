package com.reactnativenavigation.viewcontrollers.component;

import android.animation.Animator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.viewcontrollers.statusbar.StatusBarPresenter;
import com.reactnativenavigation.views.component.ComponentLayout;

public class ComponentPresenter extends ComponentPresenterBase {
    public Options defaultOptions;

    public ComponentPresenter(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void applyOptions(ComponentLayout view, Options options) {
        setBackgroundColor(view, options);
    }

    public void mergeOptions(ComponentLayout view, Options options) {
        if (options.overlayOptions.interceptTouchOutside.hasValue())
            view.setInterceptTouchOutside(options.overlayOptions.interceptTouchOutside);
        setBackgroundColor(view, options);
    }

    private void setBackgroundColor(ComponentLayout view, Options options) {
        if (options.layout.componentBackgroundColor.hasValue()) {
            view.setBackgroundColor(options.layout.componentBackgroundColor.get());
        }
    }

    public void onConfigurationChanged(ComponentLayout view, Options options) {
        if (view == null) return;
        Options withDefault = options.withDefaultOptions(defaultOptions);
        setBackgroundColor(view, withDefault);
    }

    @Nullable
    public Animator getStatusBarPushAnimation(@NonNull Options appearingOptions) {
        Options appearingOptionsWithDefault = appearingOptions.copy().withDefaultOptions(defaultOptions);
        return StatusBarPresenter.instance.getStatusBarPushAnimation(appearingOptionsWithDefault);
    }

    @Nullable
    public Animator getStatusBarPopAnimation(@NonNull Options appearingOptions, @NonNull Options disappearingOptions) {
        Options appearingOptionsWithDefault = appearingOptions.copy().withDefaultOptions(defaultOptions);
        return StatusBarPresenter.instance.getStatusBarPopAnimation(appearingOptionsWithDefault, disappearingOptions);
    }
}
