package com.reactnativenavigation.viewcontrollers.sheet;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.viewcontrollers.component.ComponentPresenterBase;
import com.reactnativenavigation.views.sheet.SheetLayout;

public class SheetPresenter extends ComponentPresenterBase {
    public Options defaultOptions;

    public SheetPresenter(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void applyOptions(SheetLayout view, Options options) {
        setBackgroundColor(view, options);
    }

    public void mergeOptions(SheetLayout view, Options options) {
        if (options.overlayOptions.interceptTouchOutside.hasValue())
            view.setInterceptTouchOutside(options.overlayOptions.interceptTouchOutside);
        setBackgroundColor(view, options);
    }

    private void setBackgroundColor(SheetLayout view, Options options) {
        if (options.layout.componentBackgroundColor.hasValue()) {
            view.setSheetBackgroundColor(options.layout.componentBackgroundColor.get());
        }
    }

    public void onConfigurationChanged(SheetLayout view, Options options) {
        if (view == null) return;
        Options withDefault = options.withDefaultOptions(defaultOptions);
        setBackgroundColor(view, withDefault);
    }
}
