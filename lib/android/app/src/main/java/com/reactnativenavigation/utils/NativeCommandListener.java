package com.reactnativenavigation.utils;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.react.NavigationEvent;

import java.util.HashMap;

public class NativeCommandListener extends CommandListenerAdapter {
    private String commandId;
    private Promise promise;
    private NavigationEvent eventEmitter;
    private Now now;

    public NativeCommandListener(String commandId, Promise promise, NavigationEvent  eventEmitter, Now now) {
        this.commandId = commandId;
        this.promise = promise;
        this.eventEmitter = eventEmitter;
        this.now = now;
    }

    @Override
    public void onSuccess(String childId) {
        promise.resolve(childId);
        eventEmitter.navigationEvent(navigationEventData());
    }

    @Override
    public void onError(String message) {
        promise.reject(new Throwable(message));
    }

    private HashMap<String, Object> navigationEventData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("commandId", commandId);
        map.put("completionTime", now.now());
        return map;
    }
}
