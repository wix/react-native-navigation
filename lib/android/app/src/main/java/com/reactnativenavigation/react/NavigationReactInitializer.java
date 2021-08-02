package com.reactnativenavigation.react;

import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.ReactShadowNode;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIImplementation;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.UIManagerModuleListener;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcherListener;
import com.facebook.react.views.modal.ReactModalHostManager;
import com.facebook.react.views.modal.ReactModalHostView;
import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.LogKt;

import androidx.annotation.NonNull;

public class NavigationReactInitializer implements ReactInstanceManager.ReactInstanceEventListener {

    private final ReactInstanceManager reactInstanceManager;
    private final DevPermissionRequest devPermissionRequest;
    private boolean waitingForAppLaunchEvent = true;
    private boolean isActivityReadyForUi = false;
    NavigationActivity _activity;

    NavigationReactInitializer(ReactInstanceManager reactInstanceManager, boolean isDebug) {
        this.reactInstanceManager = reactInstanceManager;
        this.devPermissionRequest = new DevPermissionRequest(isDebug);
    }

    void onActivityCreated(NavigationActivity activity) {
        _activity = activity;
        reactInstanceManager.addReactInstanceEventListener(this);
        waitingForAppLaunchEvent = true;
    }

    void onActivityResumed(NavigationActivity activity) {
        if (devPermissionRequest.shouldAskPermission(activity)) {
            devPermissionRequest.askPermission(activity);
        } else {
            reactInstanceManager.onHostResume(activity, activity);
            isActivityReadyForUi = true;
            prepareReactApp();
        }
    }

    void onActivityPaused(NavigationActivity activity) {
        isActivityReadyForUi = false;
        if (reactInstanceManager.hasStartedCreatingInitialContext()) {
            reactInstanceManager.onHostPause(activity);
        }
    }

    void onActivityDestroyed(NavigationActivity activity) {
        reactInstanceManager.removeReactInstanceEventListener(this);
        if (reactInstanceManager.hasStartedCreatingInitialContext()) {
            reactInstanceManager.onHostDestroy(activity);
        }
    }

    private void prepareReactApp() {
        if (shouldCreateContext()) {
            reactInstanceManager.createReactContextInBackground();
        } else if (waitingForAppLaunchEvent) {
            if (reactInstanceManager.getCurrentReactContext() != null) {
                emitAppLaunched(reactInstanceManager.getCurrentReactContext());
            }
        }
    }

    private void emitAppLaunched(@NonNull ReactContext context) {
        if (!isActivityReadyForUi) return;
        waitingForAppLaunchEvent = false;
        new EventEmitter(context).appLaunched();
    }

    private boolean shouldCreateContext() {
        return !reactInstanceManager.hasStartedCreatingInitialContext();
    }

    @Override
    public void onReactContextInitialized(final ReactContext context) {
        final UIManagerModule nativeModule = context.getNativeModule(UIManagerModule.class);
        nativeModule.getUIImplementation().setLayoutUpdateListener(root -> {
            final View view = nativeModule.resolveView(root.getReactTag());
            if (view != null && view instanceof ReactModalHostView) {
                LogKt.logd("view layout updated for view " + view.getClass().getSimpleName(), "XEXE");
            }
            LogKt.logd("onLayoutUpdated: " + root.getViewClass() + ",reactTag "
                    + root.getReactTag(), "onLayoutUpdated");
        });

        nativeModule.getEventDispatcher().addListener(event -> {
            final String eventName = event.getEventName();
            if ("topRequestClose".equals(eventName) || "topShow".equals(eventName)) {
                LogKt.logd("Modal view tag is " + event.getViewTag(), "ModalXX");
            }
        });
        emitAppLaunched(context);
    }
}
