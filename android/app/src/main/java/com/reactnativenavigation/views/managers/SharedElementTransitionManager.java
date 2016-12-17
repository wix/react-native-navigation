package com.reactnativenavigation.views.managers;

import android.util.Log;

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

    @Override
    public void updateExtraData(SharedElementTransition root, Object extraData) {

    }
}
