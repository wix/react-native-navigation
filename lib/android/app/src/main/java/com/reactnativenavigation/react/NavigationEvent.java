package com.reactnativenavigation.react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

public class NavigationEvent {
	private static final String onAppLaunched = "RNN.appLaunched";
	private static final String componentDidAppear = "RNN.componentDidAppear";
	private static final String componentDidDisappear = "RNN.componentDidDisappear";
	private static final String onNavigationButtonPressed = "RNN.navigationButtonPressed";

	private final RCTDeviceEventEmitter emitter;

	public NavigationEvent(ReactContext reactContext) {
		this.emitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
	}

	public void appLaunched() {
		emit(onAppLaunched);
	}

	public void componentDidDisappear(String id) {
		emit(componentDidDisappear, id);
	}

	public void componentDidAppear(String id) {
		emit(componentDidAppear, id);
	}

	public void sendOnNavigationButtonPressed(String id, String buttonId) {
		WritableMap map = Arguments.createMap();
		map.putString("componentId", id);
		map.putString("buttonId", buttonId);

		emit(onNavigationButtonPressed, map);
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
