package com.reactnativenavigation.bridge;

import android.os.Bundle;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

public class BundleConverter {
    public static Bundle toBundle(ReadableMap map) {
        Bundle bundle = new Bundle();
        ReadableMapKeySetIterator it = map.keySetIterator();
        while (it.hasNextKey()) {
            String key = it.nextKey();
            switch (map.getType(key)) {
                case Null:
                    break;
                case Boolean:
                    bundle.putBoolean(key, map.getBoolean(key));
                    break;
                case Number:
                    putNumber(bundle, map, key);
                    break;
                case String:
                    bundle.putString(key, map.getString(key));
                    break;
                case Map:
                    bundle.putBundle(key, toBundle(map.getMap(key)));
                    break;
                case Array:
                    bundle.putBundle(key, toBundle(map.getArray(key)));
                    break;
                default:
                    break;
            }
        }
        return bundle;
    }

    private static void putNumber(Bundle bundle, ReadableMap map, String key) {
        try {
            bundle.putInt(key, map.getInt(key));
        } catch (Exception e) {
            bundle.putDouble(key, map.getDouble(key));
        }
    }

    public static Bundle toBundle(ReadableArray array) {
        Bundle bundle = new Bundle();
        for (int i = 0; i < array.size(); i++) {
            String key = String.valueOf(i);
            switch (array.getType(i)) {
                case Null:
                    break;
                case Boolean:
                    bundle.putBoolean(key, array.getBoolean(i));
                    break;
                case Number:
                    bundle.putDouble(key, array.getDouble(i));
                    break;
                case String:
                    bundle.putString(key, array.getString(i));
                    break;
                case Map:
                    bundle.putBundle(key, toBundle(array.getMap(i)));
                    break;
                case Array:
                    bundle.putBundle(key, toBundle(array.getArray(i)));
                    break;
                default:
                    break;
            }
        }
        return bundle;
    }
}
