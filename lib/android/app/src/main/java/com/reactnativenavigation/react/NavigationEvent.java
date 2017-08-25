package com.reactnativenavigation.react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import static com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

public class NavigationEvent {
	private static final String onAppLaunched = "RNN.appLaunched";
	private static final String containerDidAppear = "RNN.containerDidAppear";
	private static final String containerDidDisappear = "RNN.containerDidDisappear";
	private static final String onNavigationButtonPressed = "RNN.navigationButtonPressed";

	private final RCTDeviceEventEmitter emitter;

	public NavigationEvent(ReactContext reactContext) {
		this.emitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
	}

	public void appLaunched() {
		emit(onAppLaunched);
	}

	public void containerDidDisappear(String id) {
		emit(containerDidDisappear, id);
	}

	public void containerDidAppear(String id) {
		emit(containerDidAppear, id);
	}

	public void sendOnNavigationButtonPressed(String id, String buttonId) {
		//TODO!
		//emit(onNavigationButtonPressed, id);
	}

	private void emit(String eventName) {
		emit(eventName, Arguments.createMap());
	}

	private void emit(String eventName, WritableMap data) {
		emitter.emit(eventName, data);
	}

	private void emit(String eventName, String param) {
		emitter.emit(eventName, param);
	}
}
