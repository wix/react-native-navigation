package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.viewcontrollers.TitleBarButtonController;
import com.reactnativenavigation.views.ComponentLayout;

public class TestComponentLayout extends ComponentLayout implements TitleBarButtonController.OnClickListener {

    private ReactView reactView;

    public TestComponentLayout(final Context context, ReactView reactView) {
        super(context, reactView);
        this.reactView = reactView;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public ViewGroup asView() {
        return this;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void sendComponentStart() {
        reactView.sendComponentStart(ComponentType.Component);
    }

    @Override
    public void sendComponentStop() {
        reactView.sendComponentStop(ComponentType.Component);
    }

    @Override
    public void applyOptions(Options options) {

    }

    @Override
    public void sendOnNavigationButtonPressed(String id) {

    }

    @Override
    public ScrollEventListener getScrollEventListener() {
        return null;
    }

    @Override
    public void dispatchTouchEventToJs(MotionEvent event) {

    }

    @Override
    public void onPress(String buttonId) {

    }
}
