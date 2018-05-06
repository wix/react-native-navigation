package com.reactnativenavigation.utils;

import com.facebook.react.bridge.Promise;

public class NativeCommandListener extends CommandListenerAdapter {
    private Promise promise;

    public NativeCommandListener(Promise promise) {
        this.promise = promise;
    }

    @Override
    public void onSuccess(String childId) {
        promise.resolve(childId);
    }

    @Override
    public void onError(String message) {
        promise.reject(new Throwable(message));
    }
}
