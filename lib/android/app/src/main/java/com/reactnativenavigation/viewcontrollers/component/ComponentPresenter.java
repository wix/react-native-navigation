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
        mergeBackgroundColor(view, options);
    }

    public void mergeOptions(ComponentLayout view, Options options) {
        if (options.getOverlayOptions().interceptTouchOutside.hasValue()) view.setInterceptTouchOutside(options.getOverlayOptions().interceptTouchOutside);
        mergeBackgroundColor(view, options);
    }

    private void mergeBackgroundColor(ComponentLayout view, Options options) {
        if (options.getLayout().componentBackgroundColor.hasValue()) {
            view.setBackgroundColor(options.getLayout().componentBackgroundColor.get());
        }
    }
}
