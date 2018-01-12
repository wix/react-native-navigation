package com.reactnativenavigation.mocks;

import com.facebook.react.bridge.*;

import javax.annotation.*;


public class MockPromise implements Promise {

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

    @Override
    public void reject(String message) {

    }

    @Override
    public void reject(Throwable reason) {

    }
}
