package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.NullColor;

import org.json.JSONObject;

public class ColorParser {
    public static Colour parse(JSONObject json, String color) {
        return json.has(color) ? new Colour(json.optInt(color)) : new NullColor();
    }
}
