package com.reactnativenavigation.options.parsers;

import com.reactnativenavigation.options.params.NullPixelDensity;
import com.reactnativenavigation.options.params.PixelDensity;

import org.json.JSONObject;

public class PixelDensityParser {
    public static PixelDensity parse(JSONObject json, String number) {
        return json.has(number) ? new PixelDensity(json.optInt(number)) : new NullPixelDensity();
    }
}
