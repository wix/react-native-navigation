package com.reactnativenavigation.parse.parsers;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
}
