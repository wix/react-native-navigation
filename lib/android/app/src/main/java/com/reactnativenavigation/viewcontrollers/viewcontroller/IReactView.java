package com.reactnativenavigation.viewcontrollers.viewcontroller;

import android.view.MotionEvent;
import android.view.ViewGroup;

public interface IReactView extends Destroyable {

    boolean isReady();

    ViewGroup asView();

    void sendOnNavigationButtonPressed(String buttonId);

    ScrollEventListener getScrollEventListener();

    void dispatchTouchEventToJs(MotionEvent event);

    boolean isRendered();

    void sendOnPIPStateChanged(String prevState, String newState);

    void sendOnPIPButtonPressed(String buttonId);
}
