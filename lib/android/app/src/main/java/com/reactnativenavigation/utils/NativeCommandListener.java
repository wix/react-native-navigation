package com.reactnativenavigation.utils;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.react.EventEmitter;

public class NativeCommandListener extends CommandListenerAdapter {
    private String commandId;
    @Nullable private Promise promise;
    private EventEmitter eventEmitter;
    private Now now;

    public NativeCommandListener(String commandId, @Nullable Promise promise, EventEmitter eventEmitter, Now now) {
        this.commandId = commandId;
        this.promise = promise;
        this.eventEmitter = eventEmitter;
        this.now = now;
    }

    @Override
    public void onSuccess(String childId) {
        if (promise != null) promise.resolve(childId);
        eventEmitter.emitCommandCompleted(commandId, now.now());
    }

    @Override
    public void onError(String message) {
        if (promise != null) promise.reject(new Throwable(message));
    }
}
