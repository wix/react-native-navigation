package com.reactnativenavigation.viewcontrollers.component;

import com.reactnativenavigation.options.Options;
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
}
