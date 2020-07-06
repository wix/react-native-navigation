package com.reactnativenavigation.controllers.viewcontroller;

import android.view.MotionEvent;
import android.view.ViewGroup;

public interface IReactView extends Destroyable {

    boolean isReady();

    ViewGroup asView();

    void sendOnNavigationButtonPressed(String buttonId);

    ScrollEventListener getScrollEventListener();

    void dispatchTouchEventToJs(MotionEvent event);

    boolean isRendered();
}
