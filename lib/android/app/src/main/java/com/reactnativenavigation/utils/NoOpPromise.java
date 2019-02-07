package com.reactnativenavigation.utils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;

import javax.annotation.Nonnull;
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
    public void reject(Throwable throwable, WritableMap userInfo) {

    }

    @Override
    public void reject(String code, @Nonnull WritableMap userInfo) {

    }

    @Override
    public void reject(String code, Throwable throwable, WritableMap userInfo) {

    }

    @Override
    public void reject(String code, String message, @Nonnull WritableMap userInfo) {

    }

    @Override
    public void reject(String code, String message, Throwable throwable, WritableMap userInfo) {

    }
}