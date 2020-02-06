package com.reactnativenavigation.react.events;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import javax.annotation.Nullable;

import static com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

public class EventEmitter {
    private static final String AppLaunched = "RNN.AppLaunched";
    private static final String CommandCompleted = "RNN.CommandCompleted";
    private static final String BottomTabSelected = "RNN.BottomTabSelected";
    private static final String BottomTabPressed = "RNN.BottomTabPressed";
    private static final String ComponentDidAppear = "RNN.ComponentDidAppear";
    private static final String ComponentDidDisappear = "RNN.ComponentDidDisappear";
    private static final String NavigationButtonPressed = "RNN.NavigationButtonPressed";
    private static final String ModalDismissed = "RNN.ModalDismissed";
    private static final String ScreenPopped = "RNN.ScreenPopped";
    @Nullable
    private ReactContext reactContext;

    public EventEmitter(@Nullable ReactContext reactContext) {
        this.reactContext = reactContext;
    }

    public void appLaunched() {
        emit(EventEmitter.AppLaunched, Arguments.createMap());
    }

    public void emitComponentDidDisappear(String id, String componentName, ComponentType type) {
        WritableMap event = Arguments.createMap();
        event.putString("componentId", id);
        event.putString("componentName", componentName);
        event.putString("componentType", type.getName());
        emit(ComponentDidDisappear, event);
    }

    public void emitComponentDidAppear(String id, String componentName, ComponentType type) {
        WritableMap event = Arguments.createMap();
        event.putString("componentId", id);
        event.putString("componentName", componentName);
        event.putString("componentType", type.getName());
        emit(ComponentDidAppear, event);
    }

    public void emitOnNavigationButtonPressed(String id, String buttonId) {
        WritableMap event = Arguments.createMap();
        event.putString("componentId", id);
        event.putString("buttonId", buttonId);
        emit(NavigationButtonPressed, event);
    }

    public void emitBottomTabSelected(int unselectedTabIndex, int selectedTabIndex) {
        WritableMap event = Arguments.createMap();
        event.putInt("unselectedTabIndex", unselectedTabIndex);
        event.putInt("selectedTabIndex", selectedTabIndex);
        emit(BottomTabSelected, event);
    }

    public void emitBottomTabPressed(int tabIndex) {
        WritableMap event = Arguments.createMap();
        event.putInt("tabIndex", tabIndex);
        emit(BottomTabPressed, event);
    }

    public void emitCommandCompleted(String commandName, String commandId, long completionTime) {
        WritableMap event = Arguments.createMap();
        event.putString("commandName", commandName);
        event.putString("commandId", commandId);
        event.putDouble("completionTime", completionTime);
        emit(CommandCompleted, event);
    }

    public void emitModalDismissed(String id, String componentName, int modalsDismissed) {
        WritableMap event = Arguments.createMap();
        event.putString("componentId", id);
        event.putString("componentName", componentName);
        event.putInt("modalsDismissed", modalsDismissed);
        emit(ModalDismissed, event);
    }

    public void emitScreenPoppedEvent(String componentId) {
        WritableMap event = Arguments.createMap();
        event.putString("componentId", componentId);
        emit(ScreenPopped, event);
    }

    private void emit(String eventName, WritableMap data) {
        if (reactContext == null) {
            Log.e("RNN", "Could not send event " + eventName + ". React context is null!");
            return;
        }
        RCTDeviceEventEmitter emitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
        emitter.emit(eventName, data);
    }
}
