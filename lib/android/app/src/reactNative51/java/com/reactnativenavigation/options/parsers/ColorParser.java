package com.reactnativenavigation.options.parsers;

import android.content.Context;

import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.DontApplyColour;
import com.reactnativenavigation.options.params.NullColor;

import org.json.JSONObject;

public class ColorParser {
    public static Colour parse(Context context, JSONObject json, String color) {
        if (json.has(color)) {
            return json.opt(color) instanceof Integer ? new Colour(json.optInt(color)) : new DontApplyColour();
        }
        return new NullColor();
    }
}
