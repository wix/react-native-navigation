package com.reactnativenavigation.react;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.reactnativenavigation.NavigationActivity;

public class NavigationReactInitializer implements ReactInstanceManager.ReactInstanceEventListener {

	private final ReactInstanceManager reactInstanceManager;
	private final DevPermissionRequest devPermissionRequest;
	private boolean waitingForAppLaunchEvent = true;

	public NavigationReactInitializer(ReactInstanceManager reactInstanceManager, boolean isDebug) {
		this.reactInstanceManager = reactInstanceManager;
		this.devPermissionRequest = new DevPermissionRequest(isDebug);
	}

	public void onActivityCreated(final NavigationActivity activity) {
		reactInstanceManager.addReactInstanceEventListener(this);
		waitingForAppLaunchEvent = true;
	}

	public void onActivityResumed(NavigationActivity activity) {
		if (devPermissionRequest.shouldAskPermission(activity)) {
			devPermissionRequest.askPermission(activity);
		} else {
			reactInstanceManager.onHostResume(activity, activity);
			prepareReactApp();
		}
	}

	public void onActivityPaused(NavigationActivity activity) {
		if (reactInstanceManager.hasStartedCreatingInitialContext()) {
			reactInstanceManager.onHostPause(activity);
		}
	}

	public void onActivityDestroyed(NavigationActivity activity) {
		reactInstanceManager.removeReactInstanceEventListener(this);
		if (reactInstanceManager.hasStartedCreatingInitialContext()) {
			reactInstanceManager.onHostDestroy(activity);
		}
	}

	private void prepareReactApp() {
		if (shouldCreateContext()) {
			reactInstanceManager.createReactContextInBackground();
		}
	}

	private void emitAppLaunched(final ReactContext context) {
		if (waitingForAppLaunchEvent) {
			waitingForAppLaunchEvent = false;
			new NavigationEvent(context).appLaunched();
		}
	}

	private boolean shouldCreateContext() {
		return !reactInstanceManager.hasStartedCreatingInitialContext();
	}

	@Override
	public void onReactContextInitialized(final ReactContext context) {
		emitAppLaunched(context);
	}
}
