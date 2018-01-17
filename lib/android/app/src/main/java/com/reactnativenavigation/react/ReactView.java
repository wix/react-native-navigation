package com.reactnativenavigation.react;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.JSTouchDispatcher;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.viewcontrollers.ComponentViewController;

@SuppressLint("ViewConstructor")
public class ReactView extends ReactRootView implements ComponentViewController.IReactView {

	private final ReactInstanceManager reactInstanceManager;
	private final String componentId;
	private final String componentName;
	private boolean isAttachedToReactInstance = false;
    private final JSTouchDispatcher jsTouchDispatcher;

    public ReactView(final Context context, ReactInstanceManager reactInstanceManager, String componentId, String componentName) {
		super(context);
		this.reactInstanceManager = reactInstanceManager;
		this.componentId = componentId;
		this.componentName = componentName;
		jsTouchDispatcher = new JSTouchDispatcher(this);
		start();
	}

	private void start() {
		setEventListener(reactRootView -> {
            reactRootView.setEventListener(null);
            isAttachedToReactInstance = true;
        });
		final Bundle opts = new Bundle();
		opts.putString("componentId", componentId);
		startReactApplication(reactInstanceManager, componentName, opts);
	}

	@Override
	public boolean isReady() {
		return isAttachedToReactInstance;
	}

	@Override
	public View asView() {
		return this;
	}

	@Override
	public void destroy() {
		unmountReactApplication();
	}

	@Override
	public void sendComponentStart() {
		new NavigationEvent(reactInstanceManager.getCurrentReactContext()).componentDidAppear(componentId);
	}

	@Override
	public void sendComponentStop() {
		new NavigationEvent(reactInstanceManager.getCurrentReactContext()).componentDidDisappear(componentId);
	}

    @Override
	public void sendOnNavigationButtonPressed(String buttonId) {
		new NavigationEvent(reactInstanceManager.getCurrentReactContext()).sendOnNavigationButtonPressed(componentId, buttonId);
	}

    @Override
    public ScrollEventListener getScrollEventListener() {
        return new ScrollEventListener(getEventDispatcher());
    }

    @Override
    public void dispatchTouchEventToJs(MotionEvent event) {
        jsTouchDispatcher.handleTouchEvent(event, getEventDispatcher());
    }

    public EventDispatcher getEventDispatcher() {
        ReactContext reactContext = reactInstanceManager.getCurrentReactContext();
        return reactContext == null ? null : reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
    }
}
