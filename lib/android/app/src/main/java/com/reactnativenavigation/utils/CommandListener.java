package com.reactnativenavigation.utils;

public interface CommandListener {
    void onSuccess(String childId);

    void onError(String message);
}
