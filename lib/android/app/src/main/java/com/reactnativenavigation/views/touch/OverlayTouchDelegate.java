package com.reactnativenavigation.views.touch;

import android.graphics.Rect;
import android.support.annotation.VisibleForTesting;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.reactnativenavigation.viewcontrollers.IReactView;

public class OverlayTouchDelegate {
    private final Rect hitRect = new Rect();
    private IReactView reactView;
    private boolean interceptTouchOutside;

    public OverlayTouchDelegate(IReactView reactView) {
        this.reactView = reactView;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return interceptTouchOutside && isDown(event) && handleDown(event);
    }

    private boolean isDown(MotionEvent event) {
        return event.getActionMasked() == MotionEvent.ACTION_DOWN;
    }

    @VisibleForTesting
    public boolean handleDown(MotionEvent event) {
        ((ViewGroup) reactView.asView()).getChildAt(0).getHitRect(hitRect);
        reactView.dispatchTouchEventToJs(event);
        return isTouchOutside(event);
    }

    private boolean isTouchOutside(MotionEvent ev) {
        return !hitRect.contains((int) ev.getRawX(), (int) ev.getRawY());
    }

    public void setInterceptTouchOutside(boolean interceptTouchOutside) {
        this.interceptTouchOutside = interceptTouchOutside;
    }
}
