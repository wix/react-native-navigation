package com.reactnativenavigation.react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import static com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

public class EventEmitter {
	private static final String AppLaunched = "RNN.AppLaunched";
	private static final String CommandCompleted = "RNN.CommandCompleted";
	private static final String NativeEvent = "RNN.NativeEvent";
	private static final String ComponentEvent = "RNN.ComponentEvent";

	// componentEvents:
	private static final String ComponentDidAppear = "ComponentDidAppear";
	private static final String ComponentDidDisappear = "ComponentDidDisappear";
	private static final String NavigationButtonPressed = "NavigationButtonPressed";
	private static final String SearchBarUpdated = "SearchBarUpdated";
	private static final String SearchBarCancelPressed = "SearchBarCancelPressed";

	private final RCTDeviceEventEmitter emitter;

	EventEmitter(ReactContext reactContext) {
		this.emitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
	}

	public void appLaunched() {
		emit(AppLaunched);
	}

	public void componentDidDisappear(String id, String componentName) {
		WritableMap event = Arguments.createMap();
		event.putString("componentId", id);
		event.putString("componentName", componentName);
		event.putString("type", ComponentDidAppear);
		emit(ComponentEvent, event);
	}

	public void componentDidAppear(String id, String componentName) {
		WritableMap event = Arguments.createMap();
		event.putString("componentId", id);
		event.putString("componentName", componentName);
		event.putString("type", ComponentDidDisappear);
		emit(ComponentEvent, event);
	}

	public void emitOnNavigationButtonPressed(String id, String buttonId) {
		WritableMap event = Arguments.createMap();
		event.putString("componentId", id);
		event.putString("buttonId", buttonId);
		event.putString("type", NavigationButtonPressed);
		emit(ComponentEvent, event);
	}

	public void emitBottomTabSelected(int unselectedTabIndex, int selectedTabIndex) {
		WritableMap params = Arguments.createMap();
		params.putInt("unselectedTabIndex", unselectedTabIndex);
		params.putInt("selectedTabIndex", selectedTabIndex);

		WritableMap event = Arguments.createMap();
		event.putString("name", "bottomTabSelected");
		event.putMap("params", params);
		emit(NativeEvent, event);
	}

	public void emitCommandCompletedEvent(String commandId, long completionTime) {
		WritableMap event = Arguments.createMap();
		event.putString("commandId", commandId);
		event.putDouble("completionTime", completionTime);
		emit(CommandCompleted, event);
	}

	private void emit(String eventName) {
		emit(eventName, Arguments.createMap());
	}

	private void emit(String eventName, WritableMap data) {
		emitter.emit(eventName, data);
	}
}
