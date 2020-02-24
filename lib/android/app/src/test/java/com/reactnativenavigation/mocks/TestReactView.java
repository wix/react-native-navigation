package com.reactnativenavigation.mocks;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.viewcontrollers.IReactView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TestReactView extends ReactView implements IReactView {

    public TestReactView(@NonNull Context context) {
        super(context, null, "", "");
    }

    @Override
    public void startReactApplication(ReactInstanceManager reactInstanceManager, String moduleName, @Nullable Bundle initialProperties, @Nullable String initialUITemplate) {

    }

    @Override
    public void sendComponentStart(ComponentType type) {

    }

    @Override
    public void sendComponentStop(ComponentType type) {

    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }

    @Override
    public ScrollEventListener getScrollEventListener() {
        return null;
    }

    @Override
    public void dispatchTouchEventToJs(MotionEvent event) {

    }

    @Override
    public boolean isRendered() {
        return getChildCount() >= 1;
    }
}
