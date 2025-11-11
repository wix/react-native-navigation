package com.reactnativenavigation.options.parsers;

import com.reactnativenavigation.options.params.FloatParam;
import com.reactnativenavigation.options.params.NullFloatParam;

import org.json.JSONObject;

public class FloatParser {
    public static FloatParam parse(JSONObject json, String number) {
        return json.has(number) ? new FloatParam((float) json.optDouble(number)) : new NullFloatParam();
    }
}
