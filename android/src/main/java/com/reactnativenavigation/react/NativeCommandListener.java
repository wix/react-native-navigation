package com.reactnativenavigation.react;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.Now;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;

import androidx.annotation.Nullable;

public class NativeCommandListener extends CommandListenerAdapter {
    private String commandId;
    private String commandName;
    @Nullable private Promise promise;
    private EventEmitter eventEmitter;
    private Now now;
    @Nullable private Navigator navigator;

    public NativeCommandListener(String commandName, String commandId, @Nullable Promise promise, EventEmitter eventEmitter, Now now) {
        this(commandName, commandId, promise, eventEmitter, now, null);
    }

    public NativeCommandListener(String commandName, String commandId, @Nullable Promise promise, EventEmitter eventEmitter, Now now, @Nullable Navigator navigator) {
        this.commandName = commandName;
        this.commandId = commandId;
        this.promise = promise;
        this.eventEmitter = eventEmitter;
        this.now = now;
        this.navigator = navigator;
    }

    @Override
    public void onSuccess(String childId) {
        if (promise != null) promise.resolve(childId);
        eventEmitter.emitCommandCompleted(commandName, commandId, now.now());
        if (navigator != null) {
            NavigationStateEmitter.emitStateChanged(navigator, eventEmitter, commandName, commandId);
        }
    }

    @Override
    public void onError(String message) {
        if (promise != null) promise.reject(new Throwable(message));
    }
}
