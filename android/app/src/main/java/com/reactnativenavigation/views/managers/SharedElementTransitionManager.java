package com.reactnativenavigation.views.managers;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.reactnativenavigation.params.parsers.SharedElementParamsParser;
import com.reactnativenavigation.views.sharedElementTransition.SharedElementTransition;

public class SharedElementTransitionManager extends ViewGroupManager<SharedElementTransition> {
    SharedElementParamsParser paramsParser = new SharedElementParamsParser();

    @Override
    public String getName() {
        return "SharedElementTransition";
    }

    @Override
    protected SharedElementTransition createViewInstance(ThemedReactContext reactContext) {
        return new SharedElementTransition(reactContext);
    }

    @ReactProp(name = "sharedElementId")
    public void setSharedElementId(SharedElementTransition elementTransition, String key) {
        elementTransition.registerSharedElementTransition(key);
    }

    @ReactProp(name = "duration")
    public void setDuration(SharedElementTransition view, int duration) {
        paramsParser.setDuration(duration);
    }

    @ReactProp(name = "hideDuration")
    public void setHideDuration(SharedElementTransition view, int duration) {
        paramsParser.setHideDuration(duration);
    }

    @ReactProp(name = "showDuration")
    public void setShowDuration(SharedElementTransition view, int duration) {
        paramsParser.setShowDuration(duration);
    }

    @ReactProp(name = "showInterpolation")
    public void setShowInterpolation(SharedElementTransition view, ReadableMap interpolation) {
        paramsParser.setShowInterpolation(interpolation);
    }

    @ReactProp(name = "hideInterpolation")
    public void setHideInterpolation(SharedElementTransition view, ReadableMap interpolation) {
        paramsParser.setHideInterpolation(interpolation);
    }

    @Override
    protected void onAfterUpdateTransaction(SharedElementTransition view) {
        view.showTransitionParams = paramsParser.parseShowTransitionParams();
        view.hideTransitionParams = paramsParser.parseHideTransitionParams();
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }
}
