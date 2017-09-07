package com.reactnativenavigation.bridge;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.BaseScreenParams;
import com.reactnativenavigation.react.ReactGateway;
import com.reactnativenavigation.screens.NavigationType;

public class EventEmitter {
    private ReactGateway reactGateway;

    public EventEmitter(ReactGateway reactGateway) {
        this.reactGateway = reactGateway;
    }

    public void sendWillAppearEvent(BaseScreenParams params, NavigationType type) {
        sendScreenChangedEventToJsScreen("willAppear", params.getNavigatorEventId());
        sendGlobalScreenChangedEvent("willAppear", params.timestamp, params.screenId, type);
    }

    public void sendDidAppearEvent(BaseScreenParams params, NavigationType type) {
        sendScreenChangedEventToJsScreen("didAppear", params.getNavigatorEventId());
        sendGlobalScreenChangedEvent("didAppear", params.timestamp, params.screenId, type);
    }

    public void sendWillDisappearEvent(BaseScreenParams params, NavigationType type) {
        sendScreenChangedEventToJsScreen("willDisappear", params.getNavigatorEventId());
        sendGlobalScreenChangedEvent("willDisappear", params.timestamp, params.screenId, type);
    }

    public void sendDidDisappearEvent(BaseScreenParams params, NavigationType type) {
        sendScreenChangedEventToJsScreen("didDisappear", params.getNavigatorEventId());
        sendGlobalScreenChangedEvent("didDisappear", params.timestamp, params.screenId, type);
    }

    private void sendScreenChangedEventToJsScreen(String eventId, String navigatorEventId) {
        WritableMap map = Arguments.createMap();
        map.putString("type", "ScreenChangedEvent");
        sendNavigatorEvent(eventId, navigatorEventId, map);
    }

    private void sendGlobalScreenChangedEvent(String eventId, double timestamp, String screenId, NavigationType type) {
        WritableMap map = Arguments.createMap();
        map.putDouble("startTime", timestamp);
        map.putDouble("endTime", System.currentTimeMillis());
        map.putString("screen", screenId);
        map.putString("commandType", String.valueOf(type));
        sendNavigatorEvent(eventId, map);
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
