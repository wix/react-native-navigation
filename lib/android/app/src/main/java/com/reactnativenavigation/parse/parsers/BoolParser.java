package com.reactnativenavigation.parse.parsers;

import android.support.annotation.NonNull;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;

import org.json.JSONObject;

public class BoolParser {
    public static @NonNull Bool parse(@NonNull JSONObject json, @NonNull String bool) {
        return json.has(bool) ? new Bool(json.optBoolean(bool)) : new NullBool();
    }
}
