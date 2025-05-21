package com.reactnativenavigation.react;

import com.facebook.react.ReactHost;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.react.events.EventEmitter;

import androidx.annotation.NonNull;

public class NavigationReactInitializer implements ReactInstanceManager.ReactInstanceEventListener {

    private final ReactHost reactHost;
    private boolean waitingForAppLaunchEvent = true;
    private boolean isActivityReadyForUi = false;

    NavigationReactInitializer(ReactHost reactHost) {
        this.reactHost = reactHost;
    }

    void onActivityCreated() {
        reactHost.addReactInstanceEventListener(this);
        waitingForAppLaunchEvent = true;
    }

    void onActivityResumed(NavigationActivity activity) {

            reactHost.onHostResume(activity, activity);
            isActivityReadyForUi = true;
            prepareReactApp();
    }

    void onActivityPaused(NavigationActivity activity) {
        isActivityReadyForUi = false;
        reactHost.onHostPause(activity);
    }

    void onActivityDestroyed(NavigationActivity activity) {
        reactHost.removeReactInstanceEventListener(this);
        reactHost.onHostDestroy(activity);
    }

    private void prepareReactApp() {
        reactHost.start();
        if (waitingForAppLaunchEvent) {
            if (reactHost.getCurrentReactContext() != null) {
                emitAppLaunched(reactHost.getCurrentReactContext());
            }
        }
    }

    private void emitAppLaunched(@NonNull ReactContext context) {
        if (!isActivityReadyForUi) return;
        waitingForAppLaunchEvent = false;
        new EventEmitter(context).appLaunched();
    }

    @Override
    public void onReactContextInitialized(final ReactContext context) {
        emitAppLaunched(context);
    }
}