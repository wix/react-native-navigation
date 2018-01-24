package com.reactnativenavigation.react;

import android.support.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import static com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

public class NavigationEvent {
	private static final String onAppLaunched = "RNN.appLaunched";
	private static final String componentDidAppear = "RNN.componentDidAppear";
	private static final String componentDidDisappear = "RNN.componentDidDisappear";
	private static final String onNavigationButtonPressed = "RNN.navigationButtonPressed";
    private static final String componentLifecycle = "RNN.componentLifecycle";

	private final RCTDeviceEventEmitter emitter;

	public NavigationEvent(ReactContext reactContext) {
		this.emitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
	}

	public void appLaunched() {
		emit(onAppLaunched);
	}

	public void componentDidDisappear(String id, String componentName) {
		emit(componentDidDisappear, id);
		emit(componentLifecycle, getLifecycleEventData(id, componentName, "didDisappear"));
	}

	public void componentDidAppear(String id, String componentName) {
		emit(componentDidAppear, id);
        emit(componentLifecycle, getLifecycleEventData(id, componentName, "didAppear"));
	}

    @NonNull
    private WritableMap getLifecycleEventData(String id, String componentName, String didAppear) {
        WritableMap map = Arguments.createMap();
        map.putString("componentId", id);
        map.putString("componentName", componentName);
        map.putString("event", didAppear);
        return map;
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
