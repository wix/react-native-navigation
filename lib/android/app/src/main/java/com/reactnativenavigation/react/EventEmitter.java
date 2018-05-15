package com.reactnativenavigation.react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import static com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

public class EventEmitter {
	private static final String onAppLaunched = "RNN.appLaunched";
	private static final String componentDidAppear = "RNN.componentDidAppear";
	private static final String componentDidDisappear = "RNN.componentDidDisappear";
	private static final String nativeEvent = "RNN.nativeEvent";
    private static final String commandCompleted = "RNN.commandCompleted";
    private static final String buttonPressedEvent = "buttonPressed";

    private final RCTDeviceEventEmitter emitter;

    EventEmitter(ReactContext reactContext) {
		this.emitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
	}

	public void appLaunched() {
		emit(onAppLaunched);
	}

	public void componentDidDisappear(String id, String componentName) {
		WritableMap event = Arguments.createMap();
		event.putString("componentId", id);
		event.putString("componentName", componentName);
		emit(componentDidDisappear, event);
	}

	public void componentDidAppear(String id, String componentName) {
		WritableMap event = Arguments.createMap();
		event.putString("componentId", id);
		event.putString("componentName", componentName);
		emit(componentDidAppear, event);
	}

    public void emitOnNavigationButtonPressed(String id, String buttonId) {
		WritableMap params = Arguments.createMap();
		params.putString("componentId", id);
		params.putString("buttonId", buttonId);

        WritableMap event = Arguments.createMap();
        event.putString("name", buttonPressedEvent);
        event.putMap("params", params);
		emit(nativeEvent, event);
	}

    public void emitBottomTabSelected(int unselectedTabIndex, int selectedTabIndex) {
        WritableMap params = Arguments.createMap();
        params.putInt("unselectedTabIndex", unselectedTabIndex);
        params.putInt("selectedTabIndex", selectedTabIndex);

        WritableMap event = Arguments.createMap();
        event.putString("name", "bottomTabSelected");
        event.putMap("params", params);
        emitter.emit(nativeEvent, event);
    }

    public void emitCommandCompletedEvent(String commandId, long completionTime) {
        WritableMap event = Arguments.createMap();
        event.putString("commandId", commandId);
        event.putDouble("completionTime", completionTime);
        emitter.emit(commandCompleted, event);
    }

	private void emit(String eventName) {
		emit(eventName, Arguments.createMap());
	}

	private void emit(String eventName, WritableMap data) {
		emitter.emit(eventName, data);
	}
}
