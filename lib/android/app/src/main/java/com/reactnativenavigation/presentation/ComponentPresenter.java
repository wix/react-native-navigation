package com.reactnativenavigation.presentation;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.ComponentLayout;

public class ComponentPresenter {

    public void mergeOptions(ComponentLayout view, Options options) {
        if (options.overlayOptions.interceptTouchOutside.hasValue()) view.setInterceptTouchOutside(options.overlayOptions.interceptTouchOutside);
    }
}
