package com.reactnativenavigation.bridge;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.reactnativenavigation.views.managers.SharedElementTransitionManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NavigationReactPackage implements ReactPackage {

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
                new NavigationReactModule(reactContext)
        );
    }

    // Depreciated RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new SharedElementTransitionManager()
        );
    }
}
