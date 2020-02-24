package com.reactnativenavigation.react;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.ReactNativeHost;
import com.reactnativenavigation.NavigationActivity;

public class ReactGateway {

	private final ReactNativeHost host;
	private final NavigationReactInitializer initializer;
	private final JsDevReloadHandler jsDevReloadHandler;

    public ReactGateway(ReactNativeHost host) {
        this.host = host;
        initializer = new NavigationReactInitializer(host.getReactInstanceManager(), host.getUseDeveloperSupport());
        jsDevReloadHandler = new JsDevReloadHandler(host.getReactInstanceManager().getDevSupportManager());
        if (host instanceof BundleDownloadListenerProvider) {
            ((BundleDownloadListenerProvider) host).setBundleLoaderListener(jsDevReloadHandler);
        }
    }

	public void onActivityCreated(NavigationActivity activity) {
		initializer.onActivityCreated();
        jsDevReloadHandler.setReloadListener(activity);
	}

	public void onActivityResumed(NavigationActivity activity) {
		initializer.onActivityResumed(activity);
		jsDevReloadHandler.onActivityResumed(activity);
	}

    public boolean onNewIntent(Intent intent) {
        if (host.hasInstance()) {
            host.getReactInstanceManager().onNewIntent(intent);
            return true;
        }
        return false;
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
