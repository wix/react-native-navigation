package com.reactnativenavigation.mocks;

import com.facebook.react.bridge.*;

import javax.annotation.*;


public class MockPromise implements Promise {
    private boolean invoked;

    @Override
    public void resolve(@Nullable Object value) {
        throwIfInvoked();
        invoked = true;
    }

    @Override
    public void reject(String code, String message) {
        throwIfInvoked();
        invoked = true;
    }

    @Override
    public void reject(String code, Throwable e) {
        throwIfInvoked();
        invoked = true;
    }

    @Override
    public void reject(String code, String message, Throwable e) {
        throwIfInvoked();
        invoked = true;
    }

    @Override
    public void reject(String message) {
        throwIfInvoked();
        invoked = true;
    }

    @Override
    public void reject(Throwable reason) {
        throwIfInvoked();
        invoked = true;
    }

    private void throwIfInvoked() {
        if (invoked) throw new RuntimeException("Promise can be resolved or rejected only once");
    }
}
