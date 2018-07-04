package com.reactnativenavigation.react;

import com.facebook.react.devsupport.interfaces.DevBundleDownloadListener;

import javax.annotation.Nullable;

public class ReloadHandler implements JsDevReloadHandler.ReloadListener, DevBundleDownloadListener {

    private Runnable onReloadListener = () -> {};

    public void setOnReloadListener(Runnable onReload) {
        this.onReloadListener = onReload;
    }

    /**
     * Called on RR and adb reload events
     */
    @Override
    public void onReload() {
        onReloadListener.run();
    }

    /**
     * Called when the bundle was successfully reloaded
     */
    @Override
    public void onSuccess() {
        onReloadListener.run();
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
        onReloadListener = null;
    }
}
