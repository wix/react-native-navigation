package com.reactnativenavigation.react;

import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.NavigationApplication;

public class ReactGateway {

	private final NavigationReactNativeHost reactNativeHost;
	private final NavigationReactInitializer initializer;
	private final JsDevReloadHandler jsDevReloadHandler;

	public ReactGateway(final NavigationApplication application, final boolean isDebug) {
		reactNativeHost = new NavigationReactNativeHost(application, isDebug);
		initializer = new NavigationReactInitializer(reactNativeHost.getReactInstanceManager(), isDebug);
		jsDevReloadHandler = new JsDevReloadHandler(reactNativeHost.getReactInstanceManager());
	}

	public NavigationReactNativeHost getReactNativeHost() {
		return reactNativeHost;
	}

	public void onActivityCreated(NavigationActivity activity) {
		initializer.onActivityCreated(activity);
	}

	public void onActivityResumed(NavigationActivity activity) {
		initializer.onActivityResumed(activity);
	}

	public void onActivityPaused(NavigationActivity activity) {
		initializer.onActivityPaused(activity);
	}

	public void onActivityDestroyed(NavigationActivity activity) {
		initializer.onActivityDestroyed(activity);
	}

	public boolean onKeyUp(final int keyCode) {
		return jsDevReloadHandler.onKeyUp(keyCode);
	}
}
