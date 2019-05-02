package com.reactnativenavigation.utils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;

import javax.annotation.Nullable;

public class NoOpPromise implements Promise {
    @Override
    public void resolve(@Nullable Object value) {

    }

    @Override
    public void reject(String code, String message) {

    }

    @Override
    public void reject(String code, Throwable e) {

    }

    @Override
    public void reject(String code, String message, Throwable e) {

    }

    @Deprecated
    @Override
    public void reject(String message) {

    }

    @Override
    public void reject(Throwable reason) {

    }

    @Override
    public void reject(Throwable reason, WritableMap map) {

    }

    @Override
    public void reject(String message, WritableMap map) {

    }
    
    @Override
    public void reject(String message, Throwable reason, WritableMap map) {

    }

    @Override
    public void reject(String message1, String message2, WritableMap map) {

    }

    @Override
    public void reject(String message1, String message2, Throwable reason, WritableMap map) {

    }
}
