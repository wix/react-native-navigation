package com.reactnativenavigation.viewcontrollers;

import android.view.MotionEvent;
import android.view.ViewGroup;

import com.reactnativenavigation.interfaces.ScrollEventListener;

public interface IReactView extends Destroyable {

    boolean isReady();

    ViewGroup asView();

    void sendOnNavigationButtonPressed(String buttonId);

    ScrollEventListener getScrollEventListener();

    void dispatchTouchEventToJs(MotionEvent event);

    boolean isRendered();
}
