package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.DontApplyColour;
import com.reactnativenavigation.parse.params.NullColor;

import org.json.JSONObject;

public class ColorParser {
    public static Colour parse(JSONObject json, String color) {
        if (json.has(color)) {
            return json.opt(color) instanceof Integer ? new Colour(json.optInt(color)) : new DontApplyColour();
        }
        return new NullColor();
    }
}
