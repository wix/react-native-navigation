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
		WritableMap data = Arguments.createMap();
		data.putString("componentId", id);
		data.putString("componentName", componentName);
		emit(componentDidDisappear, data);
	}

	public void componentDidAppear(String id, String componentName) {
		WritableMap data = Arguments.createMap();
		data.putString("componentId", id);
		data.putString("componentName", componentName);
		emit(componentDidAppear, data);
	}

    public void emitOnNavigationButtonPressed(String id, String buttonId) {
		WritableMap params = Arguments.createMap();
		params.putString("componentId", id);
		params.putString("buttonId", buttonId);

        WritableMap data = Arguments.createMap();
        data.putString("name", buttonPressedEvent);
        data.putMap("params", params);
		emit(nativeEvent, data);
	}

    public void emitBottomTabSelected(int unselectedTabIndex, int selectedTabIndex) {
        WritableMap data = Arguments.createMap();
        data.putInt("unselectedTabIndex", unselectedTabIndex);
        data.putInt("selectedTabIndex", selectedTabIndex);
        emit(nativeEvent, data);
    }

    public void emitCommandCompletedEvent(String commandId, long completionTime) {
        WritableMap map = Arguments.createMap();
        map.putString("commandId", commandId);
        map.putDouble("completionTime", completionTime);
        emit(commandCompleted, map);
    }

	private void emit(String eventName) {
		emit(eventName, Arguments.createMap());
	}

	private void emit(String eventName, WritableMap data) {
		emitter.emit(eventName, data);
	}
}
