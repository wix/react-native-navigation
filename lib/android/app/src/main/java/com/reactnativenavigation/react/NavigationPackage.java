package com.reactnativenavigation.react;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.reactnativenavigation.parse.LayoutFactory;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

import static java.util.Collections.singletonList;

public class NavigationPackage implements ReactPackage {

    private ReactNativeHost reactNativeHost;

    public NavigationPackage(final ReactNativeHost reactNativeHost) {
        this.reactNativeHost = reactNativeHost;
    }

    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        return singletonList(new NavigationModule(
                        reactContext,
                        reactNativeHost.getReactInstanceManager(),
                        new LayoutFactory(reactNativeHost.getReactInstanceManager())
                )
        );
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
