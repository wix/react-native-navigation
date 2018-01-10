package com.reactnativenavigation.parse;

import org.json.JSONObject;

public class ColorParser {
    public static Color parse(JSONObject json, String color) {
        return json.has(color) ? new Color(json.optInt(color)) : new NullColor();
    }
}
