package com.reactnativenavigation.react;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.react.events.EventEmitter;

import androidx.annotation.NonNull;

public class NavigationReactInitializer implements ReactInstanceManager.ReactInstanceEventListener {

	private final ReactInstanceManager reactInstanceManager;
	private final DevPermissionRequest devPermissionRequest;
	private boolean waitingForAppLaunchEvent = true;
	private boolean isActivityReadyForUi = false;

	NavigationReactInitializer(ReactInstanceManager reactInstanceManager, boolean isDebug) {
		this.reactInstanceManager = reactInstanceManager;
		this.devPermissionRequest = new DevPermissionRequest(isDebug);
	}

	void onActivityCreated() {
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
        emitAppLaunched(context);
	}
}
