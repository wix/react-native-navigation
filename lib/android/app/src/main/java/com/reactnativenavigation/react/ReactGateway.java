package com.reactnativenavigation.react;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.NavigationApplication;

import java.util.List;

public class ReactGateway {

	private final NavigationReactNativeHost reactNativeHost;
	private final NavigationReactInitializer initializer;
	private final JsDevReloadHandler jsDevReloadHandler;

	public ReactGateway(final NavigationApplication application, final boolean isDebug, final List<ReactPackage> additionalReactPackages) {
		SoLoader.init(application, false);
		reactNativeHost = new NavigationReactNativeHost(application, isDebug, additionalReactPackages);
		initializer = new NavigationReactInitializer(reactNativeHost.getReactInstanceManager(), isDebug);
		jsDevReloadHandler = new JsDevReloadHandler(reactNativeHost.getReactInstanceManager());
	}

	public ReactNativeHost getReactNativeHost() {
		return reactNativeHost;
	}

	public void onActivityCreated(NavigationActivity activity) {
		initializer.onActivityCreated(activity);
	}

	public void onActivityResumed(NavigationActivity activity) {
		initializer.onActivityResumed(activity);
		jsDevReloadHandler.onActivityResumed(activity);
	}

	public void onActivityPaused(NavigationActivity activity) {
		initializer.onActivityPaused(activity);
		jsDevReloadHandler.onActivityPaused(activity);
	}

	public void onActivityDestroyed(NavigationActivity activity) {
		initializer.onActivityDestroyed(activity);
	}

	public boolean onKeyUp(final int keyCode) {
		return jsDevReloadHandler.onKeyUp(keyCode);
	}
}
