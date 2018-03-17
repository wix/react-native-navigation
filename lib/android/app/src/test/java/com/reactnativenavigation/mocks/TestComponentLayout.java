package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.views.ComponentLayout;

public class TestComponentLayout extends ComponentLayout implements TopBarButtonController.OnClickListener {

    private IReactView reactView;

    public TestComponentLayout(final Context context, IReactView reactView) {
        super(context, reactView);
        this.reactView = reactView;
    }


    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void sendComponentStart() {
        reactView.sendComponentStart();
    }

    @Override
    public void sendComponentStop() {
        reactView.sendComponentStop();
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
