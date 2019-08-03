package com.reactnativenavigation.react;

import android.support.annotation.NonNull;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.reactnativenavigation.parse.LayoutFactory;

import java.util.Collections;
import java.util.List;

public class NavigationPackage implements ReactPackage {

    private ReactNativeHost reactNativeHost;

    @SuppressWarnings("WeakerAccess")
    public NavigationPackage(final ReactNativeHost reactNativeHost) {
        this.reactNativeHost = reactNativeHost;
    }

    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        return Collections.singletonList(new NavigationModule(
                        reactContext,
                        reactNativeHost.getReactInstanceManager(),
                        new LayoutFactory(reactNativeHost.getReactInstanceManager())
                )
        );
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.singletonList(new ElementViewManager());
    }
}
