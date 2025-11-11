package com.reactnativenavigation.react;

import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentLP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.RestrictTo;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactHost;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.interfaces.fabric.ReactSurface;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.common.UIManagerType;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.IReactView;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener;
import com.reactnativenavigation.views.component.Renderable;

@SuppressLint("ViewConstructor")
public class ReactView extends FrameLayout implements IReactView, Renderable {
    private final String componentId;
    private final String componentName;
    private boolean isAttachedToReactInstance = false;

    private final ReactSurface reactSurface;

    public ReactView(final Context context, String componentId, String componentName) {
        super(context);
        this.componentId = componentId;
        this.componentName = componentName;

        final Bundle opts = new Bundle();
        opts.putString("componentId", componentId);
        reactSurface = getReactHost().createSurface(context, componentName, opts);
        addView(reactSurface.getView(), matchParentLP());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    public void start() {
        if (isAttachedToReactInstance) return;
        isAttachedToReactInstance = true;
        reactSurface.start();
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
        reactSurface.stop();
    }

    public void sendComponentWillStart(ComponentType type) {
        this.post(()->{
            ReactContext currentReactContext = getReactContext();
            if (currentReactContext != null)
                new EventEmitter(currentReactContext).emitComponentWillAppear(componentId, componentName, type);
        });
    }

    public void sendComponentStart(ComponentType type) {
        this.post(()->{
            ReactContext currentReactContext = getReactContext();
            if (currentReactContext != null) {
                new EventEmitter(currentReactContext).emitComponentDidAppear(componentId, componentName, type);
            }
        });
    }

    public void sendComponentStop(ComponentType type) {
        ReactContext currentReactContext = getReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).emitComponentDidDisappear(componentId, componentName, type);
        }
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        ReactContext currentReactContext = getReactContext();
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
        View view = reactSurface.getView();
        if (view != null) {
            view.onTouchEvent(event);
        }
    }

    @Override
    public boolean isRendered() {
        return getChildCount() >= 1;
    }

    public EventDispatcher getEventDispatcher() {
        ReactContext reactContext = getReactContext();
        return reactContext == null ? null : UIManagerHelper.getEventDispatcher(reactContext, UIManagerType.FABRIC);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public String getComponentName() {
        return componentName;
    }

    private ReactHost getReactHost() {
        return  ((ReactApplication)getContext().getApplicationContext()).getReactHost();
    }

    private ReactContext getReactContext() {
        return getReactHost().getCurrentReactContext();
    }
}
