package com.reactnativenavigation.parse;

import org.json.JSONObject;

public class TextParser {
    public static Text parse(JSONObject json, String text) {
        return json.has(text) ? new Text(json.optString(text)) : new NullText();
    }
}
