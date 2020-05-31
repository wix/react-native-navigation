package com.reactnativenavigation.views.touch;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.views.ComponentLayout;

import androidx.annotation.VisibleForTesting;

public class OverlayTouchDelegate {
    private final Rect hitRect = new Rect();
    private Bool interceptTouchOutside = new NullBool();
    private ComponentLayout component;
    private IReactView reactView;

    public void setInterceptTouchOutside(Bool interceptTouchOutside) {
        this.interceptTouchOutside = interceptTouchOutside;
    }

    public OverlayTouchDelegate(ComponentLayout component, IReactView reactView) {
        this.component = component;
        this.reactView = reactView;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return interceptTouchOutside.hasValue() && event.getActionMasked() == MotionEvent.ACTION_DOWN ?
                handleDown(event) :
                component.superOnInterceptTouchEvent(event);
    }

    @VisibleForTesting
    public boolean handleDown(MotionEvent event) {
        if (isTouchInsideOverlay(event)) return component.superOnInterceptTouchEvent(event);
        return interceptTouchOutside.isFalse();
    }

    private boolean isTouchInsideOverlay(MotionEvent ev) {
        getOverlayView().getHitRect(hitRect);
        return hitRect.contains((int) ev.getRawX(), (int) ev.getRawY());
    }

    private View getOverlayView() {
        return reactView.asView().getChildCount() > 0 ? reactView.asView().getChildAt(0) : reactView.asView();
    }
}
