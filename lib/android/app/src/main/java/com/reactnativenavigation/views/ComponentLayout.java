package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.viewcontrollers.TitleBarButtonController;
import com.reactnativenavigation.views.touch.OverlayTouchDelegate;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentLP;

@SuppressLint("ViewConstructor")
public class ComponentLayout extends CoordinatorLayout implements ReactComponent, TitleBarButtonController.OnClickListener {

    private ReactView reactView;
    private final OverlayTouchDelegate touchDelegate;

    public ComponentLayout(Context context, ReactView reactView) {
		super(context);
		this.reactView = reactView;
        addView(reactView.asView(), matchParentLP());
        touchDelegate = new OverlayTouchDelegate(this, reactView);
    }

    @Override
    public boolean isReady() {
        return reactView.isReady();
    }

    @Override
    public ViewGroup asView() {
        return this;
    }

    @Override
    public void destroy() {
        reactView.destroy();
    }

    public void start() {
        reactView.start();
    }

	public void sendComponentStart() {
		reactView.sendComponentStart(ComponentType.Component);
	}

	public void sendComponentStop() {
		reactView.sendComponentStop(ComponentType.Component);
	}

    public void applyOptions(Options options) {
        touchDelegate.setInterceptTouchOutside(options.overlayOptions.interceptTouchOutside);
    }

    public void setInterceptTouchOutside(Bool interceptTouchOutside) {
        touchDelegate.setInterceptTouchOutside(interceptTouchOutside);
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        reactView.sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public ScrollEventListener getScrollEventListener() {
        return reactView.getScrollEventListener();
    }

    @Override
    public void dispatchTouchEventToJs(MotionEvent event) {
        reactView.dispatchTouchEventToJs(event);
    }

    @Override
    public boolean isRendered() {
        return reactView.isRendered();
    }

    @Override
    public void onPress(String buttonId) {
        reactView.sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return touchDelegate.onInterceptTouchEvent(ev);
    }

    public boolean superOnInterceptTouchEvent(MotionEvent event) {
        return super.onInterceptTouchEvent(event);
    }
}
