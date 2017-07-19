package com.reactnativenavigation.bridge;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.BaseScreenParams;
import com.reactnativenavigation.react.ReactGateway;

public class EventEmitter {
    private ReactGateway reactGateway;

    public EventEmitter(ReactGateway reactGateway) {
        this.reactGateway = reactGateway;
    }

    public void sendWillAppearEvent(BaseScreenParams params) {
        sendScreenChangedEvent("willAppear", params.getNavigatorEventId());
        sendGlobalScreenChangedEvent("willAppear", params.timestamp, params.screenId);
    }

    public void sendDidAppearEvent(BaseScreenParams params) {
        sendScreenChangedEvent("didAppear", params.getNavigatorEventId());
        sendGlobalScreenChangedEvent("didAppear", params.timestamp, params.screenId);
    }

    public void sendWillDisappearEvent(BaseScreenParams params) {
        sendScreenChangedEvent("willDisappear", params.getNavigatorEventId());
        sendGlobalScreenChangedEvent("willDisappear", params.timestamp, params.screenId);
    }

    public void sendDidDisappearEvent(BaseScreenParams params) {
        sendScreenChangedEvent("didDisappear", params.getNavigatorEventId());
        sendGlobalScreenChangedEvent("didDisappear", params.timestamp, params.screenId);
    }

    private void sendScreenChangedEvent(String eventId, String navigatorEventId) {
        WritableMap map = Arguments.createMap();
        map.putString("type", "ScreenChangedEvent");
        sendNavigatorEvent(eventId, navigatorEventId, map);
    }

    private void sendGlobalScreenChangedEvent(String type, double timestamp, String screenId) {
        WritableMap map = Arguments.createMap();
        map.putDouble("startTime", timestamp);
        map.putDouble("endTime", System.currentTimeMillis());
        map.putString("screen", screenId);
        sendNavigatorEvent(type, map);
    }

    public void sendNavigatorEvent(String eventId, String navigatorEventId) {
        if (!NavigationApplication.instance.isReactContextInitialized()) {
            return;
        }
        reactGateway.getReactEventEmitter().sendNavigatorEvent(eventId, navigatorEventId);
    }

    public void sendNavigatorEvent(String eventId, String navigatorEventId, WritableMap data) {
        if (!NavigationApplication.instance.isReactContextInitialized()) {
            return;
        }
        reactGateway.getReactEventEmitter().sendNavigatorEvent(eventId, navigatorEventId, data);
    }

    public void sendEvent(String eventId, String navigatorEventId) {
        if (!NavigationApplication.instance.isReactContextInitialized()) {
            return;
        }
        reactGateway.getReactEventEmitter().sendEvent(eventId, navigatorEventId);
    }

    public void sendNavigatorEvent(String eventId, WritableMap arguments) {
        if (!NavigationApplication.instance.isReactContextInitialized()) {
            return;
        }
        reactGateway.getReactEventEmitter().sendEvent(eventId, arguments);
    }

    public void sendEvent(String eventId) {
        if (!NavigationApplication.instance.isReactContextInitialized()) {
            return;
        }
        reactGateway.getReactEventEmitter().sendEvent(eventId, Arguments.createMap());
    }

    public void sendAppLaunchedEvent() {
        if (!NavigationApplication.instance.isReactContextInitialized()) {
            return;
        }
        reactGateway.getReactEventEmitter().sendEvent("RNN.appLaunched", Arguments.createMap());
    }
}
