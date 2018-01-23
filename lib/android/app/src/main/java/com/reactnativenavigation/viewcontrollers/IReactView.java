package com.reactnativenavigation.viewcontrollers;

import android.view.MotionEvent;
import android.view.View;

import com.reactnativenavigation.interfaces.ScrollEventListener;

public interface IReactView extends Destroyable {

    boolean isReady();

    View asView();

    void sendComponentStart();

    void sendComponentStop();

    void sendOnNavigationButtonPressed(String buttonId);

    ScrollEventListener getScrollEventListener();

    void dispatchTouchEventToJs(MotionEvent event);
}
