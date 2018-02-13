package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;

import org.json.JSONObject;

public class BoolParser {
    public static Bool parse(JSONObject json, String bool) {
        return json.has(bool) ? new Bool(json.optBoolean(bool)) : new NullBool();
    }
}
