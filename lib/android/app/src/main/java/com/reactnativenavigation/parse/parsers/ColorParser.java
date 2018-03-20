package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.NullColor;

import org.json.JSONObject;

public class ColorParser {
    public static Color parse(JSONObject json, String color) {
        return json.has(color) ? new Color(json.optInt(color)) : new NullColor();
    }
}
