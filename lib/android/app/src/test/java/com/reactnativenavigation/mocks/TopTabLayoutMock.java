package com.reactnativenavigation.mocks;

import android.content.*;
import android.view.*;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.TopBar;

public class TopTabLayoutMock extends View implements ReactComponent {

    public TopTabLayoutMock(Context context) {
        super(context);
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

    }

    @Override
    public void sendComponentStop() {

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
    public void applyOptions(Options options) {

    }

    @Override
    public void drawBehindTopBar() {

    }

    @Override
    public void drawBelowTopBar(TopBar topBar) {

    }
}
