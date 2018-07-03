package com.reactnativenavigation.react;

import com.facebook.react.devsupport.interfaces.DevBundleDownloadListener;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.Navigator;

import javax.annotation.Nullable;

public class ReloadListener implements JsDevReloadHandler.ReloadListener, DevBundleDownloadListener {
    private Navigator navigator;

    public ReloadListener(Navigator navigator) {
        this.navigator = navigator;
    }

    /**
     * Called on RR and adb reload events
     */
    @Override
    public void onReload() {
        navigator.destroyViews();
    }

    /**
     * Called when the bundle was successfully reloaded
     */
    @Override
    public void onSuccess() {
        UiUtils.runOnMainThread(navigator::destroyViews);
    }

    /**
     * Bundle progress updates
     */
    @Override
    public void onProgress(@Nullable String status, @Nullable Integer done, @Nullable Integer total) {

    }

    /**
     * Bundle load failure
     */
    @Override
    public void onFailure(Exception cause) {

    }

    public void destroy() {
        navigator = null;
    }
}
