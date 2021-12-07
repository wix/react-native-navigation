package com.reactnativenavigation.react;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.JSTouchDispatcher;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.IReactView;
import com.reactnativenavigation.views.component.Renderable;

import androidx.annotation.RestrictTo;

@SuppressLint("ViewConstructor")
public class ReactView extends ReactRootView implements IReactView, Renderable {

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
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    public void start() {
        if (isAttachedToReactInstance) return;
        isAttachedToReactInstance = true;
        final Bundle opts = new Bundle();
        opts.putString("componentId", componentId);
        startReactApplication(reactInstanceManager, componentName, opts);
    }

    @Override
    public boolean isReady() {
        return isAttachedToReactInstance;
    }

    @Override
    public ReactView asView() {
        return this;
    }

    @Override
    public void destroy() {
        unmountReactApplication();
    }

    public void sendComponentWillStart(ComponentType type) {
        this.post(()->{
            if (this.reactInstanceManager == null) return;
            ReactContext currentReactContext = reactInstanceManager.getCurrentReactContext();
            if (currentReactContext != null)
                new EventEmitter(currentReactContext).emitComponentWillAppear(componentId, componentName, type);
        });
    }

    public void sendComponentStart(ComponentType type) {
        this.post(()->{
            if (this.reactInstanceManager == null) return;
            ReactContext currentReactContext = reactInstanceManager.getCurrentReactContext();
            if (currentReactContext != null) {
                new EventEmitter(currentReactContext).emitComponentDidAppear(componentId, componentName, type);
            }
        });
    }

    public void sendComponentStop(ComponentType type) {
        if (this.reactInstanceManager == null) return;
        ReactContext currentReactContext = reactInstanceManager.getCurrentReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).emitComponentDidDisappear(componentId, componentName, type);
        }
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        if (this.reactInstanceManager == null) return;
        ReactContext currentReactContext = reactInstanceManager.getCurrentReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).emitOnNavigationButtonPressed(componentId, buttonId);
        }
    }

    @Override
    public ScrollEventListener getScrollEventListener() {
        return new ScrollEventListener(getEventDispatcher());
    }

    @Override
    public void dispatchTouchEventToJs(MotionEvent event) {
        jsTouchDispatcher.handleTouchEvent(event, getEventDispatcher());
    }

    @Override
    public boolean isRendered() {
        return getChildCount() >= 1;
    }

    public EventDispatcher getEventDispatcher() {
        ReactContext reactContext = reactInstanceManager.getCurrentReactContext();
        return reactContext == null ? null : reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public String getComponentName() {
        return componentName;
    }
}
