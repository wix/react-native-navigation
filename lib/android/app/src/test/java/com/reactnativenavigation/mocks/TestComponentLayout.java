package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.views.component.ComponentLayout;

public class TestComponentLayout extends ComponentLayout implements ButtonController.OnClickListener {

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
    public void sendComponentWillStart() {
        reactView.sendComponentStart(ComponentType.Component);
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
    public void onPress(@NonNull ButtonOptions button) {

    }
}
