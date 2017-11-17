package com.reactnativenavigation.layouts;

import com.facebook.react.bridge.Promise;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.LeftButtonOnClickListener;

public interface ScreenStackContainer extends LeftButtonOnClickListener {
    void push(ScreenParams screenParams, Promise onPushComplete);

    void pop(ScreenParams screenParams);

    void popToRoot(ScreenParams params);

    void newStack(ScreenParams params);

    void destroy();
}
