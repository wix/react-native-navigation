package com.reactnativenavigation.react;

import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentLP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.RestrictTo;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactHost;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.interfaces.fabric.ReactSurface;
import com.facebook.react.runtime.ReactSurfaceImpl;
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
    private static final String TAG = "RNN.ReactView";
    /**
     * Upper bound on deferred lifecycle retries (~20s at ~60fps-style scheduling).
     * After this we emit with React context even if {@link #isRendered()} is still false,
     * matching legacy behavior for edge cases.
     */
    private static final int MAX_DEFERRED_LIFECYCLE_RETRIES = 1200;

    private final String componentId;
    private final String componentName;
    private boolean isAttachedToReactInstance = false;

    private final ReactSurface reactSurface;

    private boolean pendingWillAppear;
    private boolean pendingDidAppear;
    private ComponentType pendingWillType;
    private ComponentType pendingDidType;
    private int deferredLifecycleRetryCount;
    private boolean deferredLifecycleFlushPosted;

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
        clearDeferredLifecycleEvents();
        reactSurface.stop();
    }

    /**
     * Replace the surface's initial props. Useful for components that need to
     * receive runtime updates from native (e.g. bottom tab item components).
     * No-op when the underlying surface implementation does not support
     * runtime prop updates.
     */
    public void setProps(Bundle props) {
        if (reactSurface instanceof ReactSurfaceImpl) {
            ((ReactSurfaceImpl) reactSurface).updateInitProps(props);
        }
    }

    public void sendComponentWillStart(ComponentType type) {
        this.post(() -> handleSendComponentWillStart(type));
    }

    public void sendComponentStart(ComponentType type) {
        this.post(() -> handleSendComponentStart(type));
    }

    public void sendComponentStop(ComponentType type) {
        clearDeferredLifecycleEvents();
        ReactContext currentReactContext = getReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).emitComponentDidDisappear(componentId, componentName, type);
        }
    }

    private void handleSendComponentWillStart(ComponentType type) {
        if (!shouldDeferLifecycleEmit()) {
            emitComponentWillAppear(type);
            return;
        }
        pendingWillAppear = true;
        pendingWillType = type;
        scheduleFlushDeferredLifecycleEvents();
    }

    private void handleSendComponentStart(ComponentType type) {
        if (!shouldDeferLifecycleEmit()) {
            emitComponentDidAppear(type);
            return;
        }
        pendingDidAppear = true;
        pendingDidType = type;
        scheduleFlushDeferredLifecycleEvents();
    }

    /**
     * Wait until the Fabric surface has mounted content so JS has typically committed the screen and
     * {@code bindComponent} can receive appear events (parity with iOS {@code RNNReactView} pending appear).
     */
    private boolean shouldDeferLifecycleEmit() {
        return getReactContext() == null || !isRendered();
    }

    private void scheduleFlushDeferredLifecycleEvents() {
        if (deferredLifecycleFlushPosted) return;
        deferredLifecycleFlushPosted = true;
        post(() -> {
            deferredLifecycleFlushPosted = false;
            flushDeferredLifecycleEvents();
        });
    }

    private void flushDeferredLifecycleEvents() {
        if (!pendingWillAppear && !pendingDidAppear) {
            deferredLifecycleRetryCount = 0;
            return;
        }

        boolean strictReady = !shouldDeferLifecycleEmit();
        if (!strictReady) {
            if (++deferredLifecycleRetryCount <= MAX_DEFERRED_LIFECYCLE_RETRIES) {
                scheduleFlushDeferredLifecycleEvents();
                return;
            }
            deferredLifecycleRetryCount = 0;
            if (getReactContext() == null) {
                Log.w(TAG, "Deferred component lifecycle events dropped (React context not ready)");
                clearDeferredLifecycleEvents();
                return;
            }
            // Degraded: context exists but surface not reporting children yet — emit once (legacy timing).
        } else {
            deferredLifecycleRetryCount = 0;
        }

        if (pendingWillAppear) {
            pendingWillAppear = false;
            emitComponentWillAppear(pendingWillType);
        }
        if (pendingDidAppear) {
            pendingDidAppear = false;
            emitComponentDidAppear(pendingDidType);
        }
    }

    private void clearDeferredLifecycleEvents() {
        pendingWillAppear = false;
        pendingDidAppear = false;
        deferredLifecycleRetryCount = 0;
        deferredLifecycleFlushPosted = false;
    }

    private void emitComponentWillAppear(ComponentType type) {
        ReactContext currentReactContext = getReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).emitComponentWillAppear(componentId, componentName, type);
        }
    }

    private void emitComponentDidAppear(ComponentType type) {
        ReactContext currentReactContext = getReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).emitComponentDidAppear(componentId, componentName, type);
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
        ViewGroup view = reactSurface.getView();
        if (view != null) {
            return view.getChildCount() >= 1;
        }

        return false;
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
        return ((ReactApplication) getContext().getApplicationContext()).getReactHost();
    }

    private ReactContext getReactContext() {
        return getReactHost().getCurrentReactContext();
    }
}
