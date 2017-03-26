package com.reactnativenavigation.bridge;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.react.ReactGateway;

public class EventEmitter {
    private ReactGateway reactGateway;

    public EventEmitter(ReactGateway reactGateway) {
        this.reactGateway = reactGateway;
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
}
