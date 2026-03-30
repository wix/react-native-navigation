package com.reactnativenavigation.react;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

import com.facebook.react.ReactHost;
import com.facebook.react.ReactNativeHost;
import com.reactnativenavigation.NavigationActivity;

import androidx.annotation.NonNull;

public class ReactGateway {

    private final ReactHost host;
    private final NavigationReactInitializer initializer;
    private final JsDevReloadHandler jsDevReloadHandler;

    public ReactGateway(ReactHost host) {
        this.host = host;
        initializer = new NavigationReactInitializer(host);
        jsDevReloadHandler = new JsDevReloadHandler(host.getDevSupportManager());
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
        if (host.getCurrentReactContext() != null) {
            host.onNewIntent(intent);
            return true;
        }
        return false;
    }

    public void onConfigurationChanged(NavigationActivity activity, @NonNull Configuration newConfig) {
        host.onConfigurationChanged(activity);
    }

    public void onActivityPaused(NavigationActivity activity) {
        initializer.onActivityPaused(activity);
        jsDevReloadHandler.onActivityPaused(activity);
    }

    public void onActivityDestroyed(NavigationActivity activity) {
        jsDevReloadHandler.removeReloadListener(activity);
        initializer.onActivityDestroyed(activity);
    }

    public boolean onKeyUp(Activity activity, int keyCode) {
        return jsDevReloadHandler.onKeyUp(activity, keyCode);
    }

    public void onBackPressed() {
        host.onBackPressed();
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        host.onActivityResult(activity, requestCode, resultCode, data);
    }
}
