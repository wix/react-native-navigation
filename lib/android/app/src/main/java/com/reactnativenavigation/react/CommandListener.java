package com.reactnativenavigation.react;

public interface CommandListener {
    void onSuccess(String childId);

    void onError(String message);
}
