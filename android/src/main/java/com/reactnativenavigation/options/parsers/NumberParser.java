package com.reactnativenavigation.options.parsers;

import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.Number;

import org.json.JSONObject;

public class NumberParser {
    public static Number parse(JSONObject json, String number) {
        return json.has(number) ? new Number(json.optInt(number)) : new NullNumber();
    }
}
