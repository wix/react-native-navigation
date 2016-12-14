package com.reactnativenavigation.views.managers;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
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
}
