package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.viewcontrollers.viewcontroller.IReactView;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener;

public class TestReactView extends ReactView implements IReactView {

    public TestReactView(@NonNull Context context) {
        super(context, "", "");
    }

    @Override
    public void sendComponentWillStart(ComponentType type) {

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
