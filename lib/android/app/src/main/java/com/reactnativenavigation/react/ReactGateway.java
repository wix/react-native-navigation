package com.reactnativenavigation.react;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import com.reactnativenavigation.NavigationActivity;

import java.util.List;

public class ReactGateway {

	private final ReactNativeHost host;
	private final NavigationReactInitializer initializer;
	private final JsDevReloadHandler jsDevReloadHandler;

    @SuppressWarnings("unused")
    public ReactGateway(final Application application, final boolean isDebug, final List<ReactPackage> additionalReactPackages) {
		this(application, isDebug, new NavigationReactNativeHost(application, isDebug, additionalReactPackages));
	}

	public ReactGateway(final Application application, final boolean isDebug, final ReactNativeHost host) {
        SoLoader.init(application, false);
		this.host = host;
		initializer = new NavigationReactInitializer(host.getReactInstanceManager(), isDebug);
		jsDevReloadHandler = new JsDevReloadHandler(host.getReactInstanceManager().getDevSupportManager());
        if (host instanceof BundleDownloadListenerProvider) {
            ((BundleDownloadListenerProvider) host).setBundleLoaderListener(jsDevReloadHandler);
        }
	}

	public ReactNativeHost getReactNativeHost() {
		return host;
	}

	public void onActivityCreated(NavigationActivity activity) {
		initializer.onActivityCreated(activity);
        jsDevReloadHandler.setReloadListener(activity);
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
        jsDevReloadHandler.removeReloadListener(activity);
		initializer.onActivityDestroyed(activity);
	}

	public boolean onKeyUp(final int keyCode) {
		return jsDevReloadHandler.onKeyUp(keyCode);
	}

    public void onBackPressed() {
	    host.getReactInstanceManager().onBackPressed();
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        host.getReactInstanceManager().onActivityResult(activity, requestCode, resultCode, data);
    }
}
