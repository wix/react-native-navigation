package com.reactnativenavigation.parse;

import org.json.JSONObject;

public class NumberParser {
    public static Number parse(JSONObject json, String number) {
        return json.has(number) ? new Number(json.optInt(number)) : new NullNumber();
    }
}
