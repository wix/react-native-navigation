package com.reactnativenavigation.react;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Collections;
import java.util.List;

public class NavigationPackage implements ReactPackage {

	private ReactNativeHost reactNativeHost;

	@SuppressWarnings("WeakerAccess")
    public NavigationPackage(final ReactNativeHost reactNativeHost) {
		this.reactNativeHost = reactNativeHost;
	}

	@Override
	public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
		return Collections.singletonList(new NavigationModule(reactContext, reactNativeHost.getReactInstanceManager()));
	}

	@Override
	public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.singletonList(new ElementViewManager());
    }
}
