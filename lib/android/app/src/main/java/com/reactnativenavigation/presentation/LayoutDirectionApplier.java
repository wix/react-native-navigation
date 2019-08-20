package com.reactnativenavigation.presentation;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.modules.i18nmanager.I18nUtil;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;

public class LayoutDirectionApplier {
    public void apply(ViewController root, Options options, ReactInstanceManager instanceManager) {
        if (options.layout.direction.hasValue() && instanceManager.getCurrentReactContext() != null) {
            root.getActivity().getWindow().getDecorView().setLayoutDirection(options.layout.direction.get());
            I18nUtil.getInstance().allowRTL(instanceManager.getCurrentReactContext(), options.layout.direction.isRtl());
            I18nUtil.getInstance().forceRTL(instanceManager.getCurrentReactContext(), options.layout.direction.isRtl());
        }
    }
}
