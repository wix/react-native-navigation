package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.parse.params.FloatParam;
import com.reactnativenavigation.parse.params.NullFloatParam;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.Number;

import org.json.JSONObject;

public class FloatParser {
    public static FloatParam parse(JSONObject json, String number) {
        return json.has(number) ? new FloatParam((float) json.optDouble(number)) : new NullFloatParam();
    }
}
