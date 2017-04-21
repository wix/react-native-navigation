package com.reactnativenavigation.views.managers;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.reactnativenavigation.views.sharedElementTransition.SharedElementTransition;

public class SharedElementTransitionManager extends ViewGroupManager<SharedElementTransition> {

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
        view.paramsParser.setDuration(duration);
    }

    @ReactProp(name = "hideDuration")
    public void setHideDuration(SharedElementTransition view, int duration) {
        view.paramsParser.setHideDuration(duration);
    }

    @ReactProp(name = "showDuration")
    public void setShowDuration(SharedElementTransition view, int duration) {
        view.paramsParser.setShowDuration(duration);
    }

    @ReactProp(name = "showInterpolation")
    public void setShowInterpolation(SharedElementTransition view, ReadableMap interpolation) {
        view.paramsParser.setShowInterpolation(interpolation);
    }

    @ReactProp(name = "hideInterpolation")
    public void setHideInterpolation(SharedElementTransition view, ReadableMap interpolation) {
        view.paramsParser.setHideInterpolation(interpolation);
    }

    @ReactProp(name = "animateClipBounds")
    public void setAnimateClipBounds(SharedElementTransition view, boolean animateClipBounds) {
        view.paramsParser.animateClipBounds = animateClipBounds;
    }

    @Override
    protected void onAfterUpdateTransaction(SharedElementTransition view) {
        view.showTransitionParams = view.paramsParser.parseShowTransitionParams();
        view.hideTransitionParams = view.paramsParser.parseHideTransitionParams();
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }
}
