package com.reactnativenavigation.options.parsers;

import com.reactnativenavigation.options.params.NullNumber;
import com.reactnativenavigation.options.params.Number;

import org.json.JSONObject;

public class NumberParser {
    public static Number parse(JSONObject json, String number) {
        return number != null && json.has(number) ? new Number(json.optInt(number)) : nullNumber();
    }

    public static Number nullNumber() {
        return new NullNumber();
    }
}
