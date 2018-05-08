package com.reactnativenavigation.utils;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.react.EventEmitter;

import java.util.HashMap;

public class NativeCommandListener extends CommandListenerAdapter {
    private String commandId;
    private Promise promise;
    private EventEmitter eventEmitter;
    private Now now;

    public NativeCommandListener(String commandId, Promise promise, EventEmitter eventEmitter, Now now) {
        this.commandId = commandId;
        this.promise = promise;
        this.eventEmitter = eventEmitter;
        this.now = now;
    }

    @Override
    public void onSuccess(String childId) {
        promise.resolve(childId);
        eventEmitter.emitCommandCompletedEvent(commandId, now.now());
    }

    @Override
    public void onError(String message) {
        promise.reject(new Throwable(message));
    }
}
