package com.reactnativenavigation.react;

import com.facebook.react.bridge.NativeDeltaClient;
import com.facebook.react.devsupport.interfaces.DevBundleDownloadListener;

import javax.annotation.Nullable;

public abstract class ReloadHandlerFacade implements DevBundleDownloadListener {
    @Override
    public void onSuccess(@Nullable NativeDeltaClient nativeDeltaClient) {

    }

    @Override
    public void onProgress(@Nullable String status, @Nullable Integer done, @Nullable Integer total) {

    }

    @Override
    public void onFailure(Exception cause) {

    }

    protected abstract void onSuccess();
}
