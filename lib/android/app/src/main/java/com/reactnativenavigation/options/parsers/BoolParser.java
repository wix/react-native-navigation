package com.reactnativenavigation.options.parsers;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;

import org.json.JSONObject;

import java.util.Arrays;

import static com.reactnativenavigation.utils.CollectionUtils.first;

public class BoolParser {
    public static Bool parse(JSONObject json, String key) {
        return json.has(key) ? new Bool(json.optBoolean(key)) : new NullBool();
    }

    public static Bool parseFirst(JSONObject json, String... keys) {
        String first = first(Arrays.asList(keys), json::has);
        return first != null ? new Bool(json.optBoolean(first)) : new NullBool();
    }
}
