package com.reactnativenavigation.react;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactHost;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.interfaces.fabric.ReactSurface;
import com.facebook.react.runtime.ReactSurfaceView;
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
        ReactHost host = ((ReactApplication)context.getApplicationContext()).getReactHost();
        reactSurface = host.createSurface(context, componentName, opts);
        addView(reactSurface.getView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    public void start() {
        if (isAttachedToReactInstance) return;
        isAttachedToReactInstance = true;

        reactSurface.start();
    }

    private ReactSurfaceView getReactSurfaceView() {
        return (ReactSurfaceView) reactSurface.getView();
    }

    @Override
    public boolean isReady() {
        return isAttachedToReactInstance;
    }

    @Override
    public ViewGroup asView() {
        return this;
    }

    @Override
    public void destroy() {
        reactSurface.stop();
    }

    public void sendComponentWillStart(ComponentType type) {
        post(() -> {
            ReactContext currentReactContext = getReactSurfaceView().getCurrentReactContext();
            if (currentReactContext != null)
                new EventEmitter(currentReactContext).emitComponentWillAppear(componentId, componentName, type);
        });
    }

    public void sendComponentStart(ComponentType type) {
       post(() -> {
           ReactContext currentReactContext = getReactSurfaceView().getCurrentReactContext();
            if (currentReactContext != null) {
                new EventEmitter(currentReactContext).emitComponentDidAppear(componentId, componentName, type);
            }
        });
    }

    public void sendComponentStop(ComponentType type) {
        ReactContext currentReactContext = getReactSurfaceView().getCurrentReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).emitComponentDidDisappear(componentId, componentName, type);
        }
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        ReactContext currentReactContext = getReactSurfaceView().getCurrentReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).emitOnNavigationButtonPressed(componentId, buttonId);
        }
    }

    @Override
    public ScrollEventListener getScrollEventListener() {
        return new ScrollEventListener(getEventDispatcher());
    }

    public void dispatchTouchEventToJs(MotionEvent event) {
        getReactSurfaceView().onTouchEvent(event);
    }

    @Override
    public boolean isRendered() {
        return getReactSurfaceView().getChildCount() >= 1;
    }

    @Nullable
    public EventDispatcher getEventDispatcher() {
        ReactContext currentReactContext = getReactSurfaceView().getCurrentReactContext();
        if (currentReactContext == null) {
            return null;
        }

        return UIManagerHelper.getEventDispatcher(currentReactContext, UIManagerType.FABRIC);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public String getComponentName() {
        return componentName;
    }
}
