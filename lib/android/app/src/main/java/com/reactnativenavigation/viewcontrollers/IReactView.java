package com.reactnativenavigation.viewcontrollers;

import android.view.MotionEvent;
import android.view.View;

import com.reactnativenavigation.interfaces.ScrollEventListener;

public interface IReactView extends Destroyable {

    boolean isReady();

    View asView();

    void sendOnNavigationButtonPressed(String buttonId);

    ScrollEventListener getScrollEventListener();

    void dispatchTouchEventToJs(MotionEvent event);

    boolean isRendered();

    void sendOnPIPStateChanged(String prevState, String newState);

    void sendOnPIPButtonPressed(String buttonId);
}
