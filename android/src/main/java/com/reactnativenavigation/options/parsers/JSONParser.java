package com.reactnativenavigation.options.parsers;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class JSONParser {
    public JSONObject parse(ReadableMap map) {
        try {
            ReadableMapKeySetIterator it = map.keySetIterator();
            JSONObject result = new JSONObject();
            while (it.hasNextKey()) {
                String key = it.nextKey();
                switch (map.getType(key)) {
                    case String:
                        result.put(key, map.getString(key));
                        break;
                    case Number:
                        result.put(key, parseNumber(map, key));
                        break;
                    case Boolean:
                        result.put(key, map.getBoolean(key));
                        break;
                    case Array:
                        result.put(key, parse(map.getArray(key)));
                        break;
                    case Map:
                        result.put(key, parse(map.getMap(key)));
                        break;
                    default:
                        break;
                }
            }
            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray parse(ReadableArray arr) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr.size(); i++) {
            switch (arr.getType(i)) {
                case String:
                    result.put(arr.getString(i));
                    break;
                case Number:
                    result.put(parseNumber(arr, i));
                    break;
                case Boolean:
                    result.put(arr.getBoolean(i));
                    break;
                case Array:
                    result.put(parse(arr.getArray(i)));
                    break;
                case Map:
                    result.put(parse(arr.getMap(i)));
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    private static Object parseNumber(ReadableMap map, String key) {
        try {
            Double doubleValue = map.getDouble(key);
            if(doubleValue % 1 == 0){
                return map.getInt(key);
            }
            return doubleValue;
        } catch (Exception e) {
            return map.getInt(key);
        }
    }

    private static Object parseNumber(ReadableArray arr, int index) {
        try {
            Double doubleValue = arr.getDouble(index);
            if(doubleValue % 1 == 0){
                return arr.getInt(index);
            }
            return doubleValue;
        } catch (Exception e) {
            return arr.getInt(index);
        }
    }

    public static WritableMap convert(JSONObject jsonObject) {
        WritableMap map = Arguments.createMap();

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.opt(key);
            if (value instanceof JSONObject) {
                map.putMap(key, convert((JSONObject) value));
            } else if (value instanceof JSONArray) {
                map.putArray(key, convert((JSONArray) value));
            } else if (value instanceof Boolean) {
                map.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                map.putInt(key, (Integer) value);
            } else if (value instanceof Double) {
                map.putDouble(key, (Double) value);
            } else if (value instanceof String) {
                map.putString(key, (String) value);
            } else {
                map.putString(key, value.toString());
            }
        }

        return map;
    }

    public static WritableArray convert(JSONArray jsonArray) {
        WritableArray array = Arguments.createArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.opt(i);
            if (value instanceof JSONObject) {
                array.pushMap(convert((JSONObject) value));
            } else if (value instanceof JSONArray) {
                array.pushArray(convert((JSONArray) value));
            } else if (value instanceof Boolean) {
                array.pushBoolean((Boolean) value);
            } else if (value instanceof Integer) {
                array.pushInt((Integer) value);
            } else if (value instanceof Double) {
                array.pushDouble((Double) value);
            } else if (value instanceof String)  {
                array.pushString((String) value);
            } else {
                array.pushString(value.toString());
            }
        }

        return array;
    }
}
