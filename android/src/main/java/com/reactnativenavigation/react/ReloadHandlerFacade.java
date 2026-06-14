package com.reactnativenavigation.react;

import com.facebook.react.devsupport.interfaces.DevBundleDownloadListener;

import javax.annotation.Nullable;

public abstract class ReloadHandlerFacade implements DevBundleDownloadListener {
    @Override
    public void onSuccess() {}

    public void onProgress(@Nullable String status, @Nullable Integer done, @Nullable Integer total) {
        onProgress(status, done, total, null);
    }

    public void onProgress(@Nullable String status, @Nullable Integer done, @Nullable Integer total, @Nullable Integer totalDone) {}

    @Override
    public void onFailure(Exception cause) {}
}
