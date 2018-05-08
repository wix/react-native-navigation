package com.reactnativenavigation.react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Map;

import static com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

public class EventEmitter {
	private static final String onAppLaunched = "RNN.appLaunched";
	private static final String componentDidAppear = "RNN.componentDidAppear";
	private static final String componentDidDisappear = "RNN.componentDidDisappear";
	private static final String nativeEvent = "RNN.nativeEvent";
    private static final String buttonPressedEvent = "buttonPressed";

    private final RCTDeviceEventEmitter emitter;

    EventEmitter(ReactContext reactContext) {
		this.emitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
	}

	public void appLaunched() {
		emit(onAppLaunched);
	}

	public void componentDidDisappear(String id, String componentName) {
		WritableMap map = Arguments.createMap();
		map.putString("componentId", id);
		map.putString("componentName", componentName);
		emit(componentDidDisappear, map);
	}

	public void componentDidAppear(String id, String componentName) {
		WritableMap map = Arguments.createMap();
		map.putString("componentId", id);
		map.putString("componentName", componentName);
		emit(componentDidAppear, map);
	}

    public void emitOnNavigationButtonPressed(String id, String buttonId) {
		WritableMap params = Arguments.createMap();
		params.putString("componentId", id);
		params.putString("buttonId", buttonId);

        WritableMap map = Arguments.createMap();
        map.putString("name", buttonPressedEvent);
        map.putMap("params", params);

		emit(nativeEvent, map);
	}

    public void emitBottomTabSelected(int unselectedTabIndex, int selectedTabIndex) {
        WritableMap map = Arguments.createMap();
        map.putInt("unselectedTabIndex", unselectedTabIndex);
        map.putInt("selectedTabIndex", selectedTabIndex);
        emit(nativeEvent, map);
    }

    public void navigationEvent(Map<String, Object> data) {
        emit(nativeEvent, Arguments.makeNativeMap(data));
    }

	private void emit(String eventName) {
		emit(eventName, Arguments.createMap());
	}

	private void emit(String eventName, WritableMap data) {
		emitter.emit(eventName, data);
	}
}
