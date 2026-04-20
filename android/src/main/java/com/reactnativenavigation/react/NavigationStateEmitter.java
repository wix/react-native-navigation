package com.reactnativenavigation.react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;

import java.util.List;
import java.util.Map;

public class NavigationStateEmitter {
    public static void emitStateChanged(Navigator navigator, EventEmitter eventEmitter, String commandName, String commandId) {
        Map<String, Object> state = navigator.getNavigationState();
        WritableMap writableState = mapToWritable(state);
        eventEmitter.emitNavigationStateChanged(writableState, commandName, commandId);
    }

    @SuppressWarnings("unchecked")
    private static WritableMap mapToWritable(Map<String, Object> map) {
        WritableMap writableMap = Arguments.createMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                writableMap.putNull(key);
            } else if (value instanceof String) {
                writableMap.putString(key, (String) value);
            } else if (value instanceof Integer) {
                writableMap.putInt(key, (Integer) value);
            } else if (value instanceof Double) {
                writableMap.putDouble(key, (Double) value);
            } else if (value instanceof Boolean) {
                writableMap.putBoolean(key, (Boolean) value);
            } else if (value instanceof Map) {
                writableMap.putMap(key, mapToWritable((Map<String, Object>) value));
            } else if (value instanceof List) {
                writableMap.putArray(key, listToWritable((List<Object>) value));
            }
        }
        return writableMap;
    }

    @SuppressWarnings("unchecked")
    private static WritableArray listToWritable(List<Object> list) {
        WritableArray writableArray = Arguments.createArray();
        for (Object value : list) {
            if (value == null) {
                writableArray.pushNull();
            } else if (value instanceof String) {
                writableArray.pushString((String) value);
            } else if (value instanceof Integer) {
                writableArray.pushInt((Integer) value);
            } else if (value instanceof Double) {
                writableArray.pushDouble((Double) value);
            } else if (value instanceof Boolean) {
                writableArray.pushBoolean((Boolean) value);
            } else if (value instanceof Map) {
                writableArray.pushMap(mapToWritable((Map<String, Object>) value));
            } else if (value instanceof List) {
                writableArray.pushArray(listToWritable((List<Object>) value));
            }
        }
        return writableArray;
    }
}
